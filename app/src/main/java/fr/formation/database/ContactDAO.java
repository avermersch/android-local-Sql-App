package fr.formation.database;


import android.database.Cursor;
import android.database.sqlite.SQLiteException;

import com.example.formation.localsqlapp.model.Contact;

import java.util.ArrayList;
import java.util.List;

public class ContactDAO {
    private DatabaseHandler db;

    public ContactDAO(DatabaseHandler db) {
        this.db = db;
    }

    public Contact findOneById(int id) throws SQLiteException{
        //Exécution de la requête
        String[] params = {String.valueOf(id)};
        String sql = "SELECT id, name, first_name, email FROM contacts WHERE id=?";
        Cursor cursor = this.db.getReadableDatabase()
                .rawQuery(sql, params);
        //Instanciation d'un contact
        Contact contact = new Contact();

        //Hydratation du contact
        if(cursor.moveToNext()){
            contact = hydrateContact(cursor);
        }

        //Fermeture du curseur
        cursor.close();

        return contact;
    }

    private Contact hydrateContact(Cursor cursor) {
        Contact contact = new Contact();

        contact.setId(cursor.getLong(0));
        contact.setName(cursor.getString(1));
        contact.setFirst_name(cursor.getString(2));
        contact.setEmail(cursor.getString(3));

        return contact;
    }

    /**
     * @return List<Contact une liste de contacts
     */
    public List<Contact> findAll() throws SQLiteException{
        //Instanciation de la liste des contacts
        List<Contact> contactList = new ArrayList<>();

        //Exection de la requête sql
        String sql = "SELECT id, name, first_name, email FROM contacts";
        Cursor cursor = this.db.getReadableDatabase().rawQuery(sql, null);
        //Boucle sur le curseur
        while(cursor.moveToNext()){
            contactList.add(this.hydrateContact(cursor));
        }

        //Fermeture du curseur
        cursor.close();

        return contactList;
    }

    /**
     * Suppression d'un contact en fonction de sa clef primaire
     * @param id
     * @throws SQLiteException
     */
    public void deleteOneById(Long id) throws SQLiteException{
        String[] params = {id.toString()};
        String sql = "DELETE FROM contacts WHERE id=?";
        this.db.getWritableDatabase().execSQL(sql, params);
    }
}
