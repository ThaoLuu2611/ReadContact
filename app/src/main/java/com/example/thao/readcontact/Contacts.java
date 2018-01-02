package com.example.thao.readcontact;

/**
 * Created by luu.phuong.thao on 10/10/2016.
 */

public class Contacts {
    String id;
    String phone;
    String name;
    String email;
    public Contacts(String id, String name, String phone, String email){
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
    }
    public String getId(){
        return id;
    }

    public String getName(){
        return name;
    }
    public String getPhone(){
        return phone;
    }

    public String getEmail(){
        return email;
    }


}
