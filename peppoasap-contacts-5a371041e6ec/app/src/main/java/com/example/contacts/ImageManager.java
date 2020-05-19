package com.example.contacts;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageManager {

    Context context;

    public ImageManager(Context c){
        this.context = c;
    }

    public String saveToInternalStorage(Bitmap bitmapImage){
        ContextWrapper cw = new ContextWrapper(context);

        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);

        File mypath = new File(directory,"profile.png");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        }catch(Exception e) {
            e.printStackTrace();
        }finally{
            try {
                fos.close();
            } catch(IOException e){
                e.printStackTrace();
            }
        }

        Log.e("DIRECTORY PATH::::", directory.getAbsolutePath());
        return directory.getAbsolutePath();
    }

    public Bitmap loadImageFromStorage(String path){
        try{
            File f = new File(path, "profile.png");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            return b;
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }

        return null;
    }

}
