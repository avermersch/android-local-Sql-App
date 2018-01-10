package com.example.formation.localsqlapp;

        import android.app.ActionBar;
        import android.content.ContentValues;
        import android.content.Intent;
        import android.database.sqlite.SQLiteException;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.TextView;
        import android.widget.Toast;

        import fr.formation.database.DatabaseHandler;

public class FormActivity extends AppCompatActivity {

    private EditText nameEditText;
    private EditText first_nameEditText;
    private EditText emailEditText;
    private String contactId;
    private Button buttonValider;

   /* private TextView textViewNom;
    private TextView textViewPrenom;
    private TextView textViewEmail;*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        ActionBar actionBar = getActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //Récupération bouton
        buttonValider = (Button)findViewById(R.id.buttonValid);

        /*récupération zone d'affichage
        textViewNom = (TextView)findViewById(R.id.textView);
        textViewPrenom = (TextView)findViewById(R.id.textView1);
        textViewEmail = (TextView)findViewById(R.id.textView2);*/


       // Récupération des données
        Intent intention = getIntent();
        String name = intention.getStringExtra("name");
        String first_name = intention.getStringExtra("first_name");
        String email = intention.getStringExtra("email");
        String id = intention.getStringExtra("id");

        //Récupération de l'id dans une variable globale
        this.contactId = id;
        //Référence aux editText
        this.emailEditText = findViewById(R.id.editTextEmail);
        this.nameEditText = findViewById(R.id.editTextNom);
        this.first_nameEditText = findViewById(R.id.editTextPrenom);

        //Afficher des données dans les champs de saisie
        this.first_nameEditText.setText(first_name);
        this.nameEditText.setText(name);
        this.emailEditText.setText(email);
    }

    public void onValid(View v) {
        Button clickedButton = (Button) v;

        //Recuperation de la saisie de l'utilisateur
        String editTextNom = ((EditText) findViewById(R.id.editTextNom)).getText().toString();
        String editTextPrenom = ((EditText) findViewById(R.id.editTextPrenom)).getText().toString();
        String editTextEmail = ((EditText) findViewById(R.id.editTextEmail)).getText().toString();

        //Instanciation de la base de données
        DatabaseHandler db = new DatabaseHandler(this);

        //Définition desw données à insérer
        ContentValues insertValues = new ContentValues();
        insertValues.put("first_name", editTextNom);
        insertValues.put("name", editTextPrenom);
        insertValues.put("email", editTextEmail);

        //Insertion des données
        try {

            if (this.contactId != null){
                //Mise à jour d'un contact existant
                String[] params = {contactId};
                db.getWritableDatabase().update(
                        "contacts",
                        insertValues,
                        "id=?",
                        params);
                setResult(RESULT_OK);
                finish();

            }else {
                //Insertion d'un nouveau contact
                db.getWritableDatabase().insert("contacts", null, insertValues);
                Toast.makeText(this, "Insertion OK", Toast.LENGTH_SHORT).show();

                //Réinitialisation des champs du formulaire
                ((EditText) findViewById(R.id.editTextNom)).setText("");
                ((EditText) findViewById(R.id.editTextPrenom)).setText("");
                ((EditText) findViewById(R.id.editTextEmail)).setText("");
                }
        } catch (SQLiteException ex) {
            Log.e("SQL EXCEPTION", ex.getMessage());
            Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
        }
    }
}
