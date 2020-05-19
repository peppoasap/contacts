package com.example.contacts;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ContactLinker extends AsyncTask<Void, Void, Void>{

    private Context context;
    private List<Contact> contactList;
    private ContactListAdapter adapter;
    private RecyclerView contactsView;

    public ContactLinker(Context context , List<Contact> contactList, ContactListAdapter adapter, RecyclerView contactsView){
        this.context = context;
        this.contactList = contactList;
        this.adapter = adapter;
        this.contactsView = contactsView;
    }



    public void getContacts(){
        ContentResolver cr = context.getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,null, null, null, null);
        if(cur.moveToFirst()) {
            do {
                Contact contact = new Contact(null,null,null,null,null);
                int nameArrLenght = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)).split(" ").length - 1;

                contact.setName(cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)).split(" ")[0]);

                if(nameArrLenght+1 > 1)
                    contact.setSurname(cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)).split(" ")[nameArrLenght]);

                contact.setId(Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID))));

                Cursor phoneCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contact.getId(), null, null);
                if (phoneCur.moveToFirst()) {
                    do {
                        contact.setPhone(phoneCur.getString(phoneCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
                        break;
                    } while (phoneCur.moveToNext());
                    phoneCur.close();
                }

                Cursor emailCur = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = " + contact.getId(), null, null);
                if (emailCur.moveToFirst()) {
                    do {
                        contact.setEmail(emailCur.getString(emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA)));
                        break;
                    } while (emailCur.moveToNext());
                    emailCur.close();
                }

                contactList.add(contact);
            } while (cur.moveToNext());
        }
        cur.close();
        //sortBySurname();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        getContacts();
        return null;
    }

    @Override
    protected void onPostExecute(Void v) {
        super.onPostExecute(v);
        //adapter = new ContactListAdapter(context,contactList);
        //contactsView.setAdapter(adapter);
        for (Contact c: contactList) {
            new TableControllerContacts(context).create(c);
        }
        sortBySurname();
        adapter = new ContactListAdapter(context, contactList, "surname");
        contactsView.setAdapter(adapter);
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
}
