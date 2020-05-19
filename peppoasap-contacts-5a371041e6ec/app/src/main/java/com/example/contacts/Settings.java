package com.example.contacts;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Settings extends AppCompatActivity {

    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor edit;

    private RadioGroup radioGroup;
    private RadioButton sortByName;
    private RadioButton sortBySurname;
    private ImageButton close;

    public String order = "surname";

    private List<Contact> deletedList;
    private RecyclerView deletedContacts;
    private DeletedContactAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private Button clean;
    private TextView elements_number;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        elements_number = findViewById(R.id.elements_number);
        //RECYCLE VIEW SET
        deletedContacts = this.findViewById(R.id.deleted_contact_list);
        deletedContacts.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this);
        deletedContacts.setLayoutManager(linearLayoutManager);

        context = getApplicationContext();
        radioGroup = this.findViewById(R.id.radio_group);
        sortByName = this.findViewById(R.id.sort_name);
        sortBySurname = this.findViewById(R.id.sort_surname);
        close = this.findViewById(R.id.close_button);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        sharedPreferences = context.getSharedPreferences("sort", MODE_PRIVATE);
        order = sharedPreferences.getString("order", "name");
        edit = sharedPreferences.edit();

        if(order.equals("surname"))
            sortBySurname.setChecked(true);
        else if(order.equals("name"))
            sortByName.setChecked(true);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.sort_name:
                        order = "name";
                        edit.putString("order", order);
                        break;
                    case R.id.sort_surname:
                        order = "surname";
                        edit.putString("order", order);
                        break;
                }
                edit.apply();
            }
        });

        clean = findViewById(R.id.clear_deleted);
        clean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TableControllerContacts(context).dropDeleted();
                deletedList = new TableControllerContacts(getApplicationContext()).readDeleted();
                adapter = new DeletedContactAdapter(context, deletedList, elements_number);
                deletedContacts.setAdapter(adapter);
                Toast.makeText(context, "ALL IS CLEAR!", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        deletedList = new ArrayList<>();
        deletedList = new TableControllerContacts(getApplicationContext()).readDeleted();
        sortByName();
        adapter = new DeletedContactAdapter(this, deletedList, elements_number);
        deletedContacts.setAdapter(adapter);
    }

    public void sortByName(){
        Collections.sort(deletedList, new Comparator<Contact>() {
            @Override
            public int compare(Contact c1, Contact c2) {
                if(c1.getName() != null && c2.getName() != null)
                    return c1.getName().compareTo(c2.getName());
                else if(c1.getName() == null && c2.getName() == null )
                    return 0;
                else if(c1.getName() == null)
                    return -1;
                else
                    return 1;
            }
        });
    }
}
