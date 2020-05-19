package com.example.contacts;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private List<Contact> contactList;
    //private List<Contact> filteredList;
    private RecyclerView contactsView;
    private ContactListAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private final int ALL_PERMISSION = 1;
    final String[] permissions = new String[]{Manifest.permission.READ_CONTACTS
            ,Manifest.permission.CALL_PHONE
            ,Manifest.permission.CAMERA};


    private ImageView addButton;
    private ImageView menuButton;
    private ImageView searchButton;
    private LinearLayout search_layout;
    private TextView profile_name;
    private TextView profile_phone;
    private ImageButton profile_picture;
    private android.support.v7.widget.SearchView searchView;

    private SharedPreferences sharedPreferences;
    String order = "surname";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        checkPermission();

        setContentView(R.layout.activity_main);
        contactsView = findViewById(R.id.contacts_list);

        contactsView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this);
        contactsView.setLayoutManager(linearLayoutManager);
        contactList = new ArrayList<>();
       //filteredList = new ArrayList<>();
        //db = new DatabaseHandler(this);

        addButton = this.findViewById(R.id.add_button);
        addButton.setOnClickListener(this);
        menuButton = this.findViewById(R.id.menu_button);
        menuButton.setOnClickListener(this);
        search_layout = this.findViewById(R.id.search_layout);
        searchButton = this.findViewById(R.id.searchbutton);
        searchButton.setOnClickListener(this);
        searchView = this.findViewById(R.id.search_view);

        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    search_layout.setVisibility(View.GONE);
                }
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s);
                return true;
            }
        });

        profile_name = this.findViewById(R.id.profile_name);
        profile_phone = this.findViewById(R.id.profile_phone);
        profile_picture = this.findViewById(R.id.profile_picture);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_button:
                addContact();
                break;
            case R.id.menu_button:
                menuButton.setImageResource(R.drawable.ic_more_horiz_black_24dp);
                menuShow();
                break;
            case R.id.searchbutton:
                search_layout.setVisibility(View.VISIBLE);
                searchView.setIconified(false);
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        contactList = new TableControllerContacts(this).read();
        sortByName();
        adapter = new ContactListAdapter(this, contactList, order);
        contactsView.setAdapter(adapter);
        profileWrite();
    }

    public void profileWrite(){
        String[] profileData = new TableControllerContacts(this).readProfile();
        String profile_name_value = profileData[0] + " " + profileData[1];
        if(profile_name_value.equals("null null"))
            profile_name_value = "Profile Not Created";

        profile_name.setText(profile_name_value);

        if(profileData[2] == null)
            profileData[2] = "-";

        profile_phone.setText(profileData[2]);

        //if(profileData[3] != null)
         //   profile_picture.setImageBitmap(new ImageManager(getApplicationContext()).loadImageFromStorage(profileData[3]));
    }

    @Override
    protected void onResume() {
        super.onResume();
        sharedPreferences = getSharedPreferences("sort", MODE_PRIVATE);
        order = sharedPreferences.getString("order", "name");
        contactList = new TableControllerContacts(this).read();

        if(order.equals("surname")) {
            sortBySurname();
        }else if(order.equals("name")) {
            sortByName();
        }
        adapter = new ContactListAdapter(this, contactList, order);
        contactsView.setAdapter(adapter);

    }

    public void checkPermission(){
        if (Build.VERSION.SDK_INT >= 23) {

            if (checkSelfPermission(Manifest.permission.READ_CONTACTS)
                    != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.CALL_PHONE)
                    != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.WRITE_CONTACTS)
                    != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                Log.v("PERMISSION:::","Permission is NOT granted");

                    ActivityCompat.requestPermissions(this, permissions, ALL_PERMISSION);

                }else{
                Log.v("PERMISSION:::","Permission already be granted");
            }
        }else { //permission is automatically granted on sdk<23 upon installation
            Log.v("PERMISSION:::","Permission is granted under SDK23.");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case ALL_PERMISSION:
                if (grantResults.length > 0 && permissions.length==grantResults.length) {
                    Intent intent = new Intent(this, ProfileCreator.class);
                    startActivity(intent);
                    new ContactLinker(this, contactList, adapter, contactsView).execute();

                    Log.e("PERMISSION OK:::::", "Permission Granted.");
                }else{
                    Log.e("PERMISSION FALSE::::", "Permission Denied.");
                    Toast.makeText(this, "Please, accept permissions!", Toast.LENGTH_SHORT).show();
                    checkPermission();
                }
                break;
        }
    }

    public void sortByName(){
        Collections.sort(contactList, new Comparator<Contact>() {
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

    public void sortBySurname(){
        Collections.sort(contactList, new Comparator<Contact>() {
            @Override
            public int compare(Contact c1, Contact c2) {
                if(c1.getSurname() != null && c2.getSurname() != null)
                    return c1.getSurname().compareTo(c2.getSurname());
                else if(c1.getSurname() == null && c2.getSurname() == null )
                    return 0;
                else if(c1.getSurname() == null)
                    return -1;
                else
                    return 1;
            }
        });
    }

    public void addContact(){
        Intent intent = new Intent(this, AddContactActivity.class);
        startActivityForResult(intent, 1);
    }

    public void menuShow(){
        PopupMenu popupMenu = new PopupMenu(this, menuButton);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.main, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menu_settings:
                        Intent  intent = new Intent(getApplicationContext(), Settings.class);
                        startActivity(intent);
                        return true;
                    case R.id.menu_sync:

                        return true;
                }
                return false;
            }
        });

        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {
                menuButton.setImageResource(R.drawable.ic_more_vert_black_32dp);
            }
        });

        popupMenu.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //FOR ADDING ACTIVITY
        if(requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Contact contact = new Contact(
                        data.getStringExtra("name"),
                        data.getStringExtra("surname"),
                        data.getStringExtra("phone"),
                        data.getStringExtra("email"),
                        null
                        //data.getStringExtra("imagePath")
                        );
                new TableControllerContacts(this).create(contact);
                int id = new TableControllerContacts(this).lastInsertId();
                contact.setId(id);
                contactList.add(contact);
                adapter = new ContactListAdapter(this, contactList, order);
                contactsView.setAdapter(adapter);
                Toast.makeText(this, contact.getName() + " " + contact.getSurname() + " - CREATED!", Toast.LENGTH_SHORT).show();
            }

        }

    }

}
