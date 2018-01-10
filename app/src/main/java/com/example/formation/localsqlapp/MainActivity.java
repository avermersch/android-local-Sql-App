package com.example.formation.localsqlapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.formation.localsqlapp.model.Contact;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.formation.database.ContactDAO;
import fr.formation.database.DatabaseHandler;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView contactListView;
    private List<Contact> contactList;
    private Integer selectedIndex;
    private Contact selectedPerson;
    private final String LIFE_CYCLE = "cycledevie";
    private ContactArrayAdapter contactAdapter;

    private DatabaseHandler db;
    private ContactDAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(LIFE_CYCLE, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Instanciation de la connexion de base de données
        this.db = new DatabaseHandler(this);
        //Instantciation du DAO pour les contacts
        this.dao = new ContactDAO(this.db);

        //Référence au widget   ListView sur le layout
        contactListView = findViewById(R.id.contactListView);
        contactListInit();

        //Récupération des données persistées dans le Bundle
        if(savedInstanceState != null){
            //Récupération de l'index de sélection de sauvegarde
            this.selectedIndex = savedInstanceState.getInt("selectedIndex");
            if(this.selectedIndex != null){
                this.selectedPerson = this.contactList.get(this.selectedIndex);
                contactAdapter.setSelection(this.selectedIndex);
                //contactListView.setSelection(this.selectedIndex);
                Log.i(LIFE_CYCLE, "Selection:"+ contactListView.getSelectedItemId());
            }
        }
    }

    private void contactListInit() {
        //Récupération de la liste des contacts
        contactList = this.dao.findAll();

        //Création d'un contactArrayAdapter
        contactAdapter = new ContactArrayAdapter(
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
        intention.putExtra("name",this.selectedPerson.getName());
        intention.putExtra("first_name", this.selectedPerson.getFirst_name());
        intention.putExtra( "email", this.selectedPerson.getEmail());
        intention.putExtra("id", String.valueOf(this.selectedPerson.getId()));

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
                String[] params = {String.valueOf(this.selectedPerson.getId())};
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

    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l){
        this.selectedIndex = position;
        this.selectedPerson = contactList.get(position);
        Toast.makeText(this, "Ligne" + position +"cliquée", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(LIFE_CYCLE, "onStart: ");

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(LIFE_CYCLE, "onPause: ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(LIFE_CYCLE, "onResume: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(LIFE_CYCLE, "onStop: ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(LIFE_CYCLE, "onDestroy: ");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(LIFE_CYCLE, "onRestart: ");
    }

    /**
     * Persistance des données avant la destruction de mon activité
     * @param outState
     */

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("selectedIndex", this.selectedIndex);
        super.onSaveInstanceState(outState);

    }
}