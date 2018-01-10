package fr.formation.database;


import android.database.Cursor;
import android.database.sqlite.SQLiteException;

import com.example.formation.localsqlapp.model.Contact;

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

        //Hydratation du contatc
        if(cursor.moveToNext()){
            contact.setId(cursor.getLong(0));
            contact.setName(cursor.getString(1));
            contact.setFirst_name(cursor.getString(2));
            contact.setEmail(cursor.getString(3));
        }

        //Fermeture du curseur
        cursor.close();

        return contact;
    }
}
