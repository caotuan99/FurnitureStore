package com.example.furniturestore.Module;

import java.io.Serializable;
import java.util.List;

public class User implements Serializable {
    private String id,email,name,address,numberphone,avatar,addmin;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User(String id,String email, String name, String address, String numberphone, String avatar) {
        this.id=id;
        this.email = email;
        this.name = name;
        this.address = address;
        this.numberphone = numberphone;
        this.avatar = avatar;
    }

    public User(String id, String email, String name, String address, String numberphone, String avatar, String addmin) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.address = address;
        this.numberphone = numberphone;
        this.avatar = avatar;
        this.addmin = addmin;
    }

    public String getAddmin() {
        return addmin;
    }

    public void setAddmin(String addmin) {
        this.addmin = addmin;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public User() {
    }

    public User(String id,String email, String name, String address, String numberphone) {
        this.id=id;
        this.email = email;
        this.name = name;
        this.address = address;
        this.numberphone = numberphone;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNumberphone() {
        return numberphone;
    }

    public void setNumberphone(String numberphone) {
        this.numberphone = numberphone;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", numberphone='" + numberphone + '\'' +
                '}';
    }
}
