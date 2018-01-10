package com.example.formation.localsqlapp;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

/**
 * Created by Formation on 09/01/2018.
 */

public class ContactArrayAdapter extends ArrayAdapter {

    private Activity context;
    private List<Map<String, String>> data;
    private LayoutInflater inflater;

    public ContactArrayAdapter(@NonNull Context context,
                               @NonNull List<Map<String ,String>> data) {

        super(context, 0, data);

        this.data = data;
        this.context = (Activity) context;
        this.inflater = this.context.getLayoutInflater();

    }

    @NonNull
    @Override
    public View getView(int position,
                        @Nullable View convertView,
                        @NonNull ViewGroup parent) {
        //Instanciation de la vue
        View view = this.inflater.inflate(R.layout.contact_list_view, parent,false);

        //Récupération des données d'une ligne
        Map<String, String> contactData = this.data.get(position);

        //Liaison entre les données et la vue
        TextView nameTextView = view.findViewById(R.id.listTextViewName);
        nameTextView.setText(contactData.get("name"));

        TextView firstNameTextView = view.findViewById(R.id.listTextViewFirstname);
        firstNameTextView.setText(contactData.get("first_name"));

        TextView emailTextView = view.findViewById(R.id.listTextViewEmail);
        emailTextView.setText(contactData.get("email"));

        return view;
    }

    public void setSelection(Integer selectedIndex) {
    }

}
