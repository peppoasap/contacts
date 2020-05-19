package com.example.contacts;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class ProfileCreator extends AppCompatActivity {

    private ImageButton picture;
    private EditText name;
    private EditText surname;
    private EditText phone;
    private Button save;

    private Bitmap profileBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_creator);

        picture = this.findViewById(R.id.add_picture);
        surname = this.findViewById(R.id.profile_surname);
        name = this.findViewById(R.id.profile_name);
        phone = this.findViewById(R.id.profile_phone);
        save = this.findViewById(R.id.profile_save);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String surname_value = surname.getText().toString();
                String name_value = name.getText().toString();
                String phone_value = phone.getText().toString();
                String imagePath = null;

                //if(profileBitmap != null)
                //imagePath = new ImageManager(getApplicationContext()).saveToInternalStorage(profileBitmap);

                if(surname_value.matches("^[a-zA-Z]+(([',. -][a-zA-Z ])?[a-zA-Z]*)*$")) {
                    if (name_value.matches("^[a-zA-Z]+(([',. -][a-zA-Z ])?[a-zA-Z]*)*$")) {
                        if (phone_value.matches("^[+]*[(]{0,1}[0-9]{1,4}[)]{0,1}[-\\s\\./0-9]*$")) {
                            new TableControllerContacts(getApplicationContext()).createProfile(name_value, surname_value, phone_value/*, imagePath*/);
                            Toast.makeText(ProfileCreator.this, "Profile created - THANKS!", Toast.LENGTH_SHORT).show();
                            finish();
                        }else
                        Toast.makeText(ProfileCreator.this, "Phone not valid - RETRY!", Toast.LENGTH_SHORT).show();
                    }else
                    Toast.makeText(ProfileCreator.this, "Name not valid - RETRY!", Toast.LENGTH_SHORT).show();
                }else
                    Toast.makeText(ProfileCreator.this, "Surname not valid - RETRY!", Toast.LENGTH_SHORT).show();
            }
        });

        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                getIntent.setType("image/*");

                Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setType("image/*");

                Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

                startActivityForResult(chooserIntent, 1);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Please, create your profile.", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {

            if (data == null)
                return;

            try {
                InputStream inputStream = this.getContentResolver().openInputStream(data.getData());
                profileBitmap = BitmapFactory.decodeStream(inputStream);
                profileBitmap = Bitmap.createScaledBitmap(profileBitmap, 512, 512, false);
                picture.setImageBitmap(profileBitmap);
                profileBitmap = Bitmap.createScaledBitmap(profileBitmap, 164, 164, false);
                //b =  Bitmap.createScaledBitmap(b, picture.getWidth(), picture.getHeight(), true);
                inputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }
}
