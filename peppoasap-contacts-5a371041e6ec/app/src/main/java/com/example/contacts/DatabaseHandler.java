package com.example.contacts;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    protected static final String DB_NAME = "ContactsDB";
    private static final int DB_VERSION = 1;
    private Context context;
    private List<Contact> contactList;

    public DatabaseHandler(Context context){
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE contacts " +
                    "(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "firstname TEXT, " +
                    "surname TEXT, " +
                    "phone TEXT," +
                    "email TEXT," +
                    "profile_image TEXT )";

        db.execSQL(sql);

        sql = "CREATE TABLE profile " +
                "(firstname TEXT, " +
                "surname TEXT, " +
                "phone TEXT," +
                "email TEXT," +
                "profile_image TEXT )";

        db.execSQL(sql);

        sql = "CREATE TABLE deleted " +
                "(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "firstname TEXT, " +
                "surname TEXT, " +
                "phone TEXT," +
                "email TEXT," +
                "profile_image TEXT )";

        db.execSQL(sql);


        Log.e("CARICAMENTO:" , "Caricamento AVVIATO....");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS contacts";
        db.execSQL(sql);
        sql = "DROP TABLE IF EXISTS profile";
        db.execSQL(sql);
        sql = "DROP TABLE IF EXISTS deleted";
        db.execSQL(sql);

        onCreate(db);
    }


}
