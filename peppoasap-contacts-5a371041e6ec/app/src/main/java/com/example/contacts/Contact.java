package com.example.contacts;

import android.graphics.Bitmap;

import java.io.Serializable;

public class Contact implements Serializable {

    private int id;
    private String name;
    private String surname;
    private String phone;
    private String email;
    private String contactPic;

    public Contact(String name, String surname, String phone, String email, String contactPic){
        this.setName(name);
        this.setSurname(surname);
        this.setPhone(phone);
        this.setEmail(email);
        this.setContactPic(contactPic);
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setContactPic(String contactPic) {
        this.contactPic = contactPic;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getContactPic() {
        return contactPic;
    }
}

