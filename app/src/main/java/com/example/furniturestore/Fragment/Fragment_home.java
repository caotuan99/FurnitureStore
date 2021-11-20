package com.example.furniturestore.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.furniturestore.BanAnActivity;
import com.example.furniturestore.QuanLiSanPham;
import com.example.furniturestore.QuanliUser;
import com.example.furniturestore.R;
import com.example.furniturestore.TuVanActivity;

public class Fragment_home extends Fragment {
    private LinearLayout btnBanan,btnBanghe,btnTuke,btnChat;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.home_fragment, container, false);
        btnBanan=view.findViewById(R.id.btnBanan);
        btnBanghe=view.findViewById(R.id.btnBanGhe);
        btnChat=view.findViewById(R.id.btnChat);
        btnTuke=view.findViewById(R.id.btnTuKe);
        btnBanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), BanAnActivity.class);
                intent.putExtra("sanpham","Bàn ăn");
                startActivity(intent);

            }
        });btnBanghe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), BanAnActivity.class);
                intent.putExtra("sanpham","Bàn ghế");
                startActivity(intent);

            }
        });btnTuke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), BanAnActivity.class);
                intent.putExtra("sanpham","Tủ kệ");
                startActivity(intent);

            }
        });
        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             startActivity(new Intent(getContext(),TuVanActivity.class));
            }
        });
        return view;
    }
}
