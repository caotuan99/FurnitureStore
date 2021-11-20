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

import com.example.furniturestore.QuanLiDonHang;
import com.example.furniturestore.QuanLiSanPham;
import com.example.furniturestore.QuanliUser;
import com.example.furniturestore.R;

public class Fragment_quanli extends Fragment {
    private LinearLayout btnqlBanan,btnqlBanghe,btnqlTuke,btnQuanliUser,btnQlDonHang;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.quanli_fragment, container, false);
        btnqlBanan=view.findViewById(R.id.btnqlBanan);
        btnqlBanghe=view.findViewById(R.id.btnqlBanGhe);
        btnqlTuke=view.findViewById(R.id.btnqlTuKe);
        btnQlDonHang=view.findViewById(R.id.btnQuanLiDonHang);
        btnQlDonHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), QuanLiDonHang.class));
            }
        });
        btnQuanliUser=view.findViewById(R.id.btnQuanliUser);
        btnqlBanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), QuanLiSanPham.class);
                intent.putExtra("sp","Bàn ăn");
                startActivity(intent);
            }
        });
        btnqlBanghe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), QuanLiSanPham.class);
                intent.putExtra("sp","Bàn ghế");
                startActivity(intent);
            }
        });
        btnqlTuke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), QuanLiSanPham.class);
                intent.putExtra("sp","Tủ kệ");
                startActivity(intent);
            }
        });
        btnQuanliUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), QuanliUser.class));
            }
        });
        return view;
    }
}
