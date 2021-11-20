package com.example.furniturestore.Module;

import java.io.Serializable;

public class SanPhamTrongGioHang implements Serializable {
    private String id;
    private String name, loaigo, gia;
    private int image;
    private String check;

    public SanPhamTrongGioHang() {
    }

    public String getCheck() {
        return check;
    }

    public void setCheck(String check) {
        this.check = check;
    }

    public SanPhamTrongGioHang(String id, String name, String loaigo, String gia, int image, String check) {
        this.id = id;
        this.name = name;
        this.loaigo = loaigo;
        this.gia = gia;
        this.image=image;
        this.check=check;
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
