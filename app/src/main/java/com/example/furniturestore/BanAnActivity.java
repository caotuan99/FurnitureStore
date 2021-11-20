package com.example.furniturestore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.furniturestore.Interface.IClickitemListener;
import com.example.furniturestore.Module.SanPham;
import com.example.furniturestore.Module.SanPhamAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class BanAnActivity extends AppCompatActivity {
    private List<SanPham> mListBanan;
    private SanPhamAdapter banAnAdapter;
    private RecyclerView rcv_banan;
    private Spinner spntheloai;
    private String sanpham="Tất cả";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ban_an);
        rcv_banan=findViewById(R.id.rcv_banan);
        spntheloai=findViewById(R.id.spntheloai);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(BanAnActivity.this,2);
        rcv_banan.setLayoutManager(gridLayoutManager);
        Intent intent=getIntent();
        setDataTheloai();
        setAdapter();
        sanpham=intent.getStringExtra("sanpham");
        if(sanpham.equals("Bàn ăn")) spntheloai.setSelection(0);
        else if(sanpham.equals("Bàn ghế")) spntheloai.setSelection(1);
        else if(sanpham.equals("Tủ kệ")) spntheloai.setSelection(2);
    }

    private void guitt(SanPham sanPham) {
        Intent intent= new Intent(BanAnActivity.this, SanPhamActivity.class);
        Bundle bundle=new Bundle();
        bundle.putSerializable("ban_an", sanPham);
        intent.putExtras(bundle);
        startActivity(intent);
    }
    private void setDataTheloai(){
        String[] data={"Bàn ăn","Bàn ghế","Tủ kệ","Tất cả"};
        ArrayAdapter arrayAdapter=new ArrayAdapter(BanAnActivity.this, android.R.layout.simple_spinner_item,data);
        spntheloai.setAdapter(arrayAdapter);
        spntheloai.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(!sanpham.equals(spntheloai.getSelectedItem().toString())){
                    sanpham=spntheloai.getSelectedItem().toString();
                    setAdapter();

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
    private void setAdapter() {
        mListBanan=new ArrayList<>();
        banAnAdapter= new SanPhamAdapter(new IClickitemListener() {
            @Override
            public void onClickItem(SanPham sanPham) {
                guitt(sanPham);
            }
        }, mListBanan);
        rcv_banan.setAdapter(banAnAdapter);
        FirebaseDatabase database= FirebaseDatabase.getInstance();
        DatabaseReference reference=database.getReference("SanPham");
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                SanPham sanPham = snapshot.getValue(SanPham.class);
                if(sanPham !=null&& (sanpham.equals(sanPham.getName())||sanpham.equals("Tất cả")))  mListBanan.add(sanPham);
                banAnAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
