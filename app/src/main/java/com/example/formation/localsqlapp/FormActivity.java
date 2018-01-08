package com.example.formation.localsqlapp;

import android.content.ContentValues;
import android.database.sqlite.SQLiteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import fr.formation.database.DatabaseHandler;

public class FormActivity extends AppCompatActivity {
    private TextView textViewNom;
    private TextView textViewPrenom;
    private TextView textViewEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        //récupération zone d'affichage
        textViewNom = (TextView)findViewById(R.id.textView);
        textViewPrenom = (TextView)findViewById(R.id.textView1);
        textViewEmail = (TextView)findViewById(R.id.textView2);


    }

    public void onValid(View v) {
        Button clickedButton = (Button) v;
        //Recuperation de la saisie de l'utilisateur
        String editTextNom = ((EditText) findViewById(R.id.editTextNom)).toString();
        String editTextPrenom = ((EditText) findViewById(R.id.editTextPrenom)).toString();
        String editTextEmail = ((EditText) findViewById(R.id.editTextEmail)).toString();

        //Instanciation de la base de données
        DatabaseHandler db = new DatabaseHandler(this);

        //Définition desw données à insérer
        ContentValues insertValues = new ContentValues();
        insertValues.put("first_name", editTextNom);
        insertValues.put("name", editTextPrenom);
        insertValues.put("email", editTextEmail);

        //Insertion des données
        try {
        db.getWritableDatabase().insert("contacts", null, insertValues);

        }
        catch (SQLiteException ex) {
                Log.e("SQL EXCEPTION", ex.getMessage());
        }
    }
}
