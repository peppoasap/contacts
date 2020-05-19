package com.example.contacts;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

class DeletedViewHolder extends RecyclerView.ViewHolder{

    protected TextView name;
    protected TextView surname;
    protected ImageView restore;
    protected CardView card;
    protected Button clear;

    public DeletedViewHolder(@NonNull final View itemView) {
        super(itemView);
        this.name = itemView.findViewById(R.id.card_name);
        this.surname = itemView.findViewById(R.id.card_surname);
        this.card = itemView.findViewById(R.id.card);
        this.restore = itemView.findViewById(R.id.restore);
    }

}

public class DeletedContactAdapter extends RecyclerView.Adapter<DeletedViewHolder> implements View.OnClickListener {

    private Context context;
    private List<Contact> contactList;
    private LayoutInflater inflater;
    private TextView elements_number;

    public DeletedContactAdapter(Context c, List<Contact> list, TextView elements_number){
        this.context = c;
        this.contactList = list;
        this.inflater = LayoutInflater.from(context);
        this.elements_number = elements_number;
        if(getItemCount() > 0)
            elements_number.setVisibility(View.INVISIBLE);
        else
            elements_number.setVisibility(View.VISIBLE);
    }


    @NonNull
    @Override
    public DeletedViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.deleted_row, viewGroup, false);
        return new DeletedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeletedViewHolder contactViewHolder, final int i) {
        Contact contact = contactList.get(i);
        contactViewHolder.name.setText(contact.getName());
        contactViewHolder.surname.setText(contact.getSurname());
        contactViewHolder.restore.setTag(i);
        contactViewHolder.restore.setOnClickListener(this);

        contactViewHolder.name.setTypeface(contactViewHolder.name.getTypeface(), Typeface.BOLD);
        contactViewHolder.surname.setTypeface(contactViewHolder.surname.getTypeface(), Typeface.NORMAL);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.restore:
                int id = Integer.parseInt(v.getTag().toString());
                Contact c = contactList.get(id);
                if(new TableControllerContacts(context).create(c)) {
                    contactList.remove(id);
                    new TableControllerContacts(context).deleteDeleted(c.getId());
                    Toast.makeText(context, c.getName() + " " + c.getSurname() + " - RESTORED!", Toast.LENGTH_SHORT).show();
                    notifyDataSetChanged();
                    if(getItemCount() > 0)
                        elements_number.setVisibility(View.INVISIBLE);
                    else
                        elements_number.setVisibility(View.VISIBLE);
                }else
                    Toast.makeText(context, "ERROR RESTORING CONTACT - RETRY!", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

}
