package com.example.furniturestore.Interface;

import com.example.furniturestore.Module.SanPham;

public interface IEventCartListener {
    public  void onChecked(SanPham sanPham,boolean isChecked);
    public void deleteCart(SanPham sanPham);
    public void onClick(SanPham sanPham);
}
