package com.example.contacts;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.Image;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

public class ContactViewer extends AppCompatActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {

    private ImageView picture;
    private TextView surname;
    private TextView name;
    private TextView phone;
    private TextView email;
    private Button more;
    private Button close;
    private String imagePath;
    private ImageButton call;
    private ImageButton sms;
    private ImageButton sendmail;
    private ImageButton share;

    private int id;
    private String tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_viewer);

        close = this.findViewById(R.id.view_close);
        more = this.findViewById(R.id.more_button);
        close.setOnClickListener(this);
        more.setOnClickListener(this);

        surname = this.findViewById(R.id.view_surname);
        name = this.findViewById(R.id.view_name);
        phone = this.findViewById(R.id.view_phone);
        email = this.findViewById(R.id.view_email);
        picture = this.findViewById(R.id.view_picture);
        call = this.findViewById(R.id.view_call);
        sendmail = this.findViewById(R.id.view_sendmail);
        sms = this.findViewById(R.id.view_sms);
        share = this.findViewById(R.id.view_share);

        call.setOnClickListener(this);
        sendmail.setOnClickListener(this);
        sms.setOnClickListener(this);
        share.setOnClickListener(this);

        Intent i = getIntent();
        tag = i.getStringExtra("tag");
        id = i.getIntExtra("id", -1);
        String surname_value = i.getStringExtra("surname");
        String name_value = i.getStringExtra("name");
        String phone_value = i.getStringExtra("phone");
        String email_value = i.getStringExtra("email");
        //String imagePath = i.getStringExtra("imagePath");

        setSurnameView(surname_value);
        setNameview(name_value);
        setPhoneView(phone_value);
        setEmailView(email_value);
        /*if(imagePath != null) {
            this.imagePath = imagePath;
            picture.setImageBitmap(new ImageManager(getApplicationContext()).loadImageFromStorage(imagePath));
        }
        */
    }

    public void setSurnameView(String surname_value){
        if(surname_value != null) {
            if (surname_value.length() > 30)
                surname.setTextSize(14);
            else if (surname_value.length() > 15)
                surname.setTextSize(18);
            else
                surname.setTextSize(22);

            surname.setText(surname_value);
        }
    }

    public void setNameview(String name_value){
        if(name_value != null) {
            if (name_value.length() > 30)
                name.setTextSize(14);
            else if (name_value.length() > 15)
                name.setTextSize(18);
            else
                name.setTextSize(22);

            name.setText(name_value);
        }
    }

    public void setPhoneView(String phone_value){
        if (phone_value != null) {
            if (phone_value.length() > 30)
                phone.setTextSize(14);
            else if (phone_value.length() > 15)
                phone.setTextSize(18);
            else
                phone.setTextSize(22);

            phone.setText(phone_value);
            call.getBackground().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
            sms.getBackground().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
        } else {
            phone.setText(getString(R.string.not_available));
            call.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
            sms.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
        }
    }

    public void setEmailView(String email_value){
        if (email_value != null){
            if (email_value.length() > 30)
                email.setTextSize(14);
            else if (email_value.length() > 15)
                email.setTextSize(18);
            else
                email.setTextSize(22);

            email.setText(email_value);
            sendmail.getBackground().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
        }else {
            email.setText(getString(R.string.not_available));
            sendmail.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.view_close:
                finish();
                break;
            case R.id.more_button:
                showPopup(v);
                more.setBackgroundResource(R.drawable.ic_more_horiz_black_24dp);
                break;
            case R.id.view_call:
                call();
                break;
            case R.id.view_sms:
                sms();
                break;
            case R.id.view_sendmail:
                sendmail();
                break;
            case R.id.view_share:
                share();
                break;
        }
    }

    public void call(){
        if(!phone.getText().equals("Not Available")) {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + phone.getText()));
            this.startActivity(intent);
        }else{
            Toast.makeText(this, "Contact not callable - Missing phone number!", Toast.LENGTH_SHORT).show();
        }
    }

    public void sms(){
        if(!phone.getText().equals("Not Available")) {
            Uri uri = Uri.parse("smsto:" + phone.getText());
            Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
            this.startActivity(intent);
        }else{
            Toast.makeText(this, "Contact unreachable - Missing phone number!", Toast.LENGTH_SHORT).show();
        }
    }

    public void sendmail(){
        if(!email.getText().equals("Not Available")) {
            Uri uri = Uri.parse("mailto:" + email.getText());
            Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
            this.startActivity(intent);
        }else{
            Toast.makeText(this, "Contact unreachable - Missing email address!", Toast.LENGTH_SHORT).show();
        }
    }

    public void share(){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT,
                name.getText() + " " + surname.getText());
        intent.putExtra(android.content.Intent.EXTRA_TEXT,
                name.getText() + " " + surname.getText() + "\n-Phone: " + phone.getText() + "\n-Email: " + email.getText()
        + "\n\nSended by Contacts.");
        startActivity(Intent.createChooser(intent, "Share contact..."));
    }

    public void showPopup(View v){
        PopupMenu popupMenu = new PopupMenu(this, v);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.contact, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.show();
        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {
                more.setBackgroundResource(R.drawable.ic_more_vert_black_32dp);
            }
        });
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_edit:
                Intent  intent = new Intent(this, UpdateContact.class);
                intent.putExtra("id", id);
                intent.putExtra("surname", surname.getText().toString());
                intent.putExtra("name", name.getText().toString());
                intent.putExtra("phone", phone.getText().toString());
                intent.putExtra("email", email.getText().toString());
                intent.putExtra("imagePath", imagePath);
                startActivityForResult(intent,1);
                return true;
            case R.id.menu_delete:
                alertOnDelete();
                return false;

            default:
                return false;
        }
    }

    public void alertOnDelete(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete?").setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        boolean deleted = new TableControllerContacts(getApplicationContext()).delete(id);
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.putExtra("tag", tag);
                        if (deleted) {
                            Toast.makeText(getApplicationContext(),"CONTACT DELETED!", Toast.LENGTH_SHORT).show();
                            finish();
                        } else
                            Toast.makeText(getApplicationContext(), "Error! - Contact is still alive.", Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1)
            if(resultCode == RESULT_OK){
                surname.setText(data.getStringExtra("surname"));
                name.setText(data.getStringExtra("name"));
                phone.setText(data.getStringExtra("phone"));
                email.setText(data.getStringExtra("email"));
                //picture.setImageBitmap(new ImageManager(getApplicationContext()).loadImageFromStorage(data.getStringExtra("imagePath")));
                Toast.makeText(this, "Contact updated - PERFECT!", Toast.LENGTH_SHORT).show();
            }
    }
}
