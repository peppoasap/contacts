package com.example.contacts;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class AddContactActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener {

    private ImageButton close;
    private ImageButton picture;
    private EditText surname;
    private EditText name;
    private EditText phone;
    private EditText email;
    private Button save;

    private Bitmap b;
    private String surname_value;
    private String name_value;
    private String phone_value;
    private String email_value;

    public static final int PICK_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        close = this.findViewById(R.id.close_button);
        picture = this.findViewById(R.id.add_picture);
        surname = this.findViewById(R.id.add_surname);
        name = this.findViewById(R.id.add_name);
        phone = this.findViewById(R.id.add_phone);
        email = this.findViewById(R.id.add_email);
        save = this.findViewById(R.id.add_save);

        picture.setOnClickListener(this);
        close.setOnClickListener(this);
        save.setOnClickListener(this);

        surname.setOnFocusChangeListener(this);

        name.setOnFocusChangeListener(this);

        phone.setOnFocusChangeListener(this);

        email.setOnFocusChangeListener(this);

    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch(v.getId()){
            case R.id.add_surname:
                if(!hasFocus)
                checkSurname();
                break;
            case R.id.add_name:
                if(!hasFocus)
                checkName();
                break;
            case R.id.add_phone:
                if(!hasFocus)
                checkPhone();
                break;
            case R.id.add_email:
                if(!hasFocus)
                checkEmail();
                break;
        }
    }

    public boolean checkEmail(){
        Boolean okay = false;

        //EMAIL VALIDATION
        if(email.getText().toString().matches("^\\w+@\\w+\\..{2,3}(.{2,3})?$")) {
            email.setTextColor(Color.BLACK);
            email_value = email.getText().toString();
            okay = true;
        }else if(email.getText().toString().equals("")){
            email.setTextColor(Color.BLACK);
            okay = true;
        }else {
            email.setTextColor(Color.RED);
            okay = false;
        }

        return okay;
    }

    public boolean checkPhone(){
        Boolean okay = false;

        //PHONE VALIDATION
        if(phone.getText().toString().matches("^[+]*[(]{0,1}[0-9]{1,4}[)]{0,1}[-\\s\\./0-9]*$")) {
            phone.setTextColor(Color.BLACK);
            phone_value = phone.getText().toString();
            okay = true;
        }else if(phone.getText().toString().equals("")){
            phone.setTextColor(Color.BLACK);
            okay = true;
        }else {
            phone.setTextColor(Color.RED);
            okay = false;
        }
        return okay;
    }

    public boolean checkName(){
        Boolean okay = false;

        //NAME VALIDATION
        if(name.getText().toString().matches("^[a-zA-Z]+(([',. -][a-zA-Z ])?[a-zA-Z]*)*$")) {
            name.setTextColor(Color.BLACK);
            name_value = name.getText().toString();
            okay = true;
        }else {
            name.setTextColor(Color.RED);
            okay = false;
        }
        return okay;
    }

    public boolean checkSurname(){
        Boolean okay = false;

        //SURNAME VALIDATION
        if(surname.getText().toString().matches("^[a-zA-Z]+(([',. -][a-zA-Z ])?[a-zA-Z]*)*$")) {
            surname.setTextColor(Color.BLACK);
            surname_value = surname.getText().toString();
            okay = true;
        }else{
            surname.setTextColor(Color.RED);
            okay = false;
        }
        return okay;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_picture:
                Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                getIntent.setType("image/*");

                Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setType("image/*");

                Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

                startActivityForResult(chooserIntent, PICK_IMAGE);
                break;
            case R.id.close_button:
                finish();
                break;
            case R.id.add_save:
                if(checkSurname() && checkName() && checkPhone() && checkEmail()){
                    String imagePath = null;
                    //if(b != null)
                     //    imagePath = new ImageManager(getApplicationContext()).saveToInternalStorage(b);

                    Intent intent = new Intent(this, MainActivity.class);
                    intent.putExtra("surname", surname_value);
                    intent.putExtra("name", name_value);
                    intent.putExtra("phone", phone_value);
                    intent.putExtra("email", email_value);
                    //intent.putExtra("imagePath", imagePath);
                    setResult(RESULT_OK, intent);
                    finish();
                    break;
                }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {

                if(data == null)
                    return;

                try {
                    InputStream inputStream = this.getContentResolver().openInputStream(data.getData());
                    b =  BitmapFactory.decodeStream(inputStream);
                    b = Bitmap.createScaledBitmap(b, 512, 512, false);
                    //b =  Bitmap.createScaledBitmap(b, picture.getWidth(), picture.getHeight(), true);
                    picture.setImageBitmap(b);
                    inputStream.close();
                }catch(FileNotFoundException e){
                    e.printStackTrace();
                }catch(IOException e){
                    e.printStackTrace();
                }

        }
    }
}
