package com.example.furniturestore.Module;

public class DonHang {
    private String id,uid,tien,diachi,std;

    public DonHang(String id, String uid, String tien, String diachi, String std) {
        this.id = id;
        this.uid = uid;
        this.tien = tien;
        this.diachi = diachi;
        this.std = std;
    }

    public DonHang() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTien() {
        return tien;
    }

    public void setTien(String tien) {
        this.tien = tien;
    }

    public String getDiachi() {
        return diachi;
    }

    public void setDiachi(String diachi) {
        this.diachi = diachi;
    }

    public String getStd() {
        return std;
    }

    public void setStd(String std) {
        this.std = std;
    }
}
