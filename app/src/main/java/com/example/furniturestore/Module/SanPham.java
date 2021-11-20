package com.example.furniturestore.Module;

import java.io.Serializable;

public class SanPham implements Serializable {
    private String id;
    private String name, loaigo, gia;
    private int image;

    public SanPham() {
    }

    public SanPham(String id, String name, String loaigo, String gia, int image) {
        this.id = id;
        this.name = name;
        this.loaigo = loaigo;
        this.gia = gia;
        this.image=image;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLoaigo() {
        return loaigo;
    }

    public void setLoaigo(String loaigo) {
        this.loaigo = loaigo;
    }

    public String getGia() {
        return gia;
    }

    public void setGia(String gia) {
        this.gia = gia;
    }



    @Override
    public String toString() {
        return "SanPham{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", loaigo='" + loaigo + '\'' +
                ", gia='" + gia + '\'' +
                '}';
    }
}
