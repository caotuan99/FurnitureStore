package com.example.furniturestore.Interface;

import com.example.furniturestore.Module.SanPham;
import com.example.furniturestore.Module.User;

public interface IClickUserManager {
    public void BanUser(User user);
    public  void AddAddmin(User user);
    public void unBanUser(User user);
    public void DelAddmin(User user);
}
