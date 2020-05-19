package com.example.contacts;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class TableControllerContacts extends DatabaseHandler {

    public TableControllerContacts(Context context){
        super(context);
    }

    public boolean createProfile(String name, String surname, String phone/*, String imagePath*/){
        ContentValues values = new ContentValues();

        values.put("firstname", name);
        values.put("surname", surname);
        values.put("phone", phone);
        //values.put("profile_image", imagePath);

        SQLiteDatabase db = this.getWritableDatabase();

        boolean createSuccesfull = db.insert("profile", null, values) > 0;
        db.close();

        return createSuccesfull;
    }

    public String[] readProfile(){
        String[] data = new String[4];
        String sql = "SELECT * FROM profile";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(sql, null);
        if(cursor.moveToFirst()){
            data[0] = cursor.getString(cursor.getColumnIndex("firstname"));
            data[1] = cursor.getString(cursor.getColumnIndex("surname"));
            data[2] = cursor.getString(cursor.getColumnIndex("phone"));
            //data[3] = cursor.getString(cursor.getColumnIndex("profile_image"));
        }
        cursor.close();
        db.close();
        return data;
    }

    public boolean create(Contact contact){
        ContentValues values = new ContentValues();

        values.put("firstname", contact.getName());
        values.put("surname", contact.getSurname());
        values.put("phone", contact.getPhone());
        values.put("email", contact.getEmail());
        values.put("profile_image", contact.getContactPic());

        SQLiteDatabase db = this.getWritableDatabase();

        boolean createSuccesfull = db.insert("contacts", null, values) > 0;
        db.close();

        return createSuccesfull;
    }

    public List<Contact> read(){
        List<Contact> contactList = new ArrayList<>();

        String sql = "SELECT * FROM contacts ORDER BY id DESC";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(sql, null);

        if(cursor.moveToFirst()){
            do{
                Contact contact = new Contact(null,null,null,null,null);
                contact.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex("id"))));
                contact.setName(cursor.getString(cursor.getColumnIndex("firstname")));
                contact.setSurname(cursor.getString(cursor.getColumnIndex("surname")));
                contact.setPhone(cursor.getString(cursor.getColumnIndex("phone")));
                contact.setEmail(cursor.getString(cursor.getColumnIndex("email")));
                //contact.setContactPic(cursor.getString(cursor.getColumnIndex("profile_image")));
                //if(contact.getContactPic() != null)
                  //  Log.e("PICTURE DATA: ", contact.getContactPic());

                contactList.add(contact);
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return contactList;
    }

    public boolean edit(int id, String surname, String name, String phone, String email/*, String contactPic*/){
        boolean edit_successful = false;
        ContentValues contentValues = new ContentValues();
        contentValues.put("surname", surname);
        contentValues.put("firstname", name);
        contentValues.put("phone", phone);
        contentValues.put("email", email);
        //contentValues.put("profile_image", contactPic);

        SQLiteDatabase db = this.getWritableDatabase();
        edit_successful = db.update("contacts", contentValues,"id = '" + id + "'", null) > 0;
        db.close();
        return edit_successful;
    }

    public boolean delete(int id){
        boolean delete_successful = false;

        insertDeleted(id);

        SQLiteDatabase db = this.getWritableDatabase();
        delete_successful = db.delete("contacts", "id = '" + id + "'", null) > 0;
        if(!delete_successful){
            db.delete("deleted", "id = '" + id + "'", null);
        }
        db.close();

        return delete_successful;
    }

    public boolean insertDeleted(int id){
        boolean insert_successful = false;
        Contact contact = null;
        String sql = "SELECT * FROM contacts WHERE id ='" + id + "'";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(sql, null);

        if(cursor.moveToFirst()){
                contact = new Contact(null,null,null,null,null);
                contact.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex("id"))));
                contact.setName(cursor.getString(cursor.getColumnIndex("firstname")));
                contact.setSurname(cursor.getString(cursor.getColumnIndex("surname")));
                contact.setPhone(cursor.getString(cursor.getColumnIndex("phone")));
                contact.setEmail(cursor.getString(cursor.getColumnIndex("email")));
                //contact.setContactPic(cursor.getString(cursor.getColumnIndex("profile_image")));
        }
        cursor.close();

        db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("firstname", contact.getName());
        values.put("surname", contact.getSurname());
        values.put("phone", contact.getPhone());
        values.put("email", contact.getEmail());

        insert_successful = db.insert("deleted", null, values) > 0;
        if(insert_successful)
            Log.e("DELETED INSERTED::::", contact.getSurname());
        db.close();

        return insert_successful;
    }

    public List<Contact> readDeleted(){
        List<Contact> contactList = new ArrayList<>();

        String sql = "SELECT * FROM deleted ORDER BY id DESC";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(sql, null);

        if(cursor.moveToFirst()){
            do{
                Contact contact = new Contact(null,null,null,null,null);
                contact.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex("id"))));
                contact.setName(cursor.getString(cursor.getColumnIndex("firstname")));
                contact.setSurname(cursor.getString(cursor.getColumnIndex("surname")));
                contact.setPhone(cursor.getString(cursor.getColumnIndex("phone")));
                contact.setEmail(cursor.getString(cursor.getColumnIndex("email")));
                contact.setContactPic(cursor.getString(cursor.getColumnIndex("profile_image")));
                //if(contact.getContactPic() != null)
                  //  Log.e("PICTURE DATA: ", contact.getContactPic());

                contactList.add(contact);
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return contactList;
    }

    public boolean deleteDeleted(int id){
        boolean delete_successful = false;

        SQLiteDatabase db = this.getWritableDatabase();
        delete_successful = db.delete("deleted", "id = '" + id + "'", null) > 0;
        db.close();

        return delete_successful;
    }

    public void dropDeleted(){
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "DELETE FROM deleted";
        db.execSQL(sql);
    }

    public int lastInsertId(){
        int id = -1;
        String sql = "SELECT last_insert_rowid()";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(sql, null);
        if(c.moveToFirst())
            id = Integer.parseInt(c.getString(0));

        return id;
    }
}
