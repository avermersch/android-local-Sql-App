package com.example.formation.localsqlapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.formation.database.DatabaseHandler;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView contactListView;
    private List<Map<String, String>> contactList;
    private Integer selectedIndex;
    private Map<String, String> selectedPerson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Référence au widget   ListView sur le layout
        contactListView = findViewById(R.id.contactListView);
        contactListInit();
    }

    private void contactListInit() {
        //Récupération de la liste des contacts
        contactList = this.getAllContacts();

        //Création d'un contactArrayAdapter
        ContactArrayAdapter contactAdapter = new ContactArrayAdapter(
                this, contactList
        );

        //Définition de l'adapter de notre listView
        contactListView.setAdapter(contactAdapter);

        //Définition d'un écouteur d'évènement pour OnItemClickListener
        contactListView.setOnItemClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Ajout des entrées du fichier main_option_menu
        //au menu contextuel de l'activité
        getMenuInflater().inflate(R.menu.main_option_menu, menu);

        return true;
    }


    //Gestion du choix d'un élément de menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent Intention = new Intent(this, FormActivity.class);
        switch (item.getItemId()) {
            case R.id.mainMenuOptionDelete:
                this.deleteSelectedContact();
                break;
            case R.id.mainMenuOptionEdit:
                this.editSelectedContact();
                break;
        }

        return true;
    }

    //Affichage du formulaire de contact pour modification
    private void editSelectedContact() {
        //Création d'une intention
        Intent intention = new Intent(this, FormActivity.class);

        //Passage des paramètres à l'intention
        intention.putExtra("name",this.selectedPerson.get("name"));
        intention.putExtra("first_name", this.selectedPerson.get("first_name"));
        intention.putExtra( "email", this.selectedPerson.get("email"));
        intention.putExtra("id", this.selectedPerson.get("id"));

        //Lancement de l'activité FormActivity
        startActivityForResult(intention, 1);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == 1 && resultCode ==RESULT_OK){
            Toast.makeText(this,"Mise à jour effectuée", Toast.LENGTH_SHORT).show();
            //Réinitialisation de la liste
            this.contactListInit();
        }
    }

    //Suppression du contact selectionné
    private void deleteSelectedContact(){
        if (this.selectedIndex != null){
            try{
                //Définition de la requête sql et des paramètres
                String sql = "DELETE FROM contacts WHERE id=?";
                String[] params = {this.selectedPerson.get("id")};
                //Exécution de la requête
                DatabaseHandler db = new DatabaseHandler(this);
                db.getWritableDatabase().execSQL(sql, params);

                //Réinitialisation de la liste des contatcs
                this.contactListInit();
            } catch (SQLiteException ex){
            Toast.makeText(this,
                    "Impossible de supprimer",
                    Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(this,
                    "Vous devez selectionner un contact",
                    Toast.LENGTH_LONG).show();

        }
    }
    /**
     * lancement de l'activité formulaire au clic sur un bouton
     * @param view
     */
    public void onAddContact(View view) {
            Intent FormIntent = new Intent(this.getBaseContext(), FormActivity.class) ;
            startActivity(FormIntent);
    }
    private List<Map<String, String>> getAllContacts(){
        //Instanciation de la connexion à la base de données
        DatabaseHandler db = new DatabaseHandler(this);

        //Exécution de la requête de sélection
        Cursor cursor = db.getReadableDatabase().rawQuery("SELECT name, first_name, email,id FROM contacts", null);

        //Instanciation de la liste qui recevra les données
        List<Map<String, String>> contactList = new ArrayList<>();


        //Parcours du curseur
        while(cursor.moveToNext()){
            Map<String, String> contactCols = new HashMap<>();
            //Remplissage du tablleau associatif en fonction des données
            contactCols.put("name", cursor.getString(0));
            contactCols.put("first_name",cursor.getString(1));
            contactCols.put("email", cursor.getString(2));
            contactCols.put("id", cursor.getString(3));

            //Ajout du map à la liste
            contactList.add(contactCols);
        }

        return contactList;
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l){
        this.selectedIndex = position;
        this.selectedPerson = contactList.get(position);
        Toast.makeText(this, "Ligne" + position +"cliquée", Toast.LENGTH_SHORT).show();
    }

}