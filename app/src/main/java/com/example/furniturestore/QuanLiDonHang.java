package com.example.furniturestore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.furniturestore.Interface.IClickDonHang;
import com.example.furniturestore.Module.DonHang;
import com.example.furniturestore.Module.DonHangAdapter;
import com.example.furniturestore.Module.QlDonHangAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class QuanLiDonHang extends AppCompatActivity {
    private  RecyclerView rcv_donhang;
    private List<DonHang> donHangList;
    private QlDonHangAdapter qlDonHangAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_li_don_hang);
        rcv_donhang=findViewById(R.id.rcv_cacdonhang);
        donHangList=new ArrayList<>();
        qlDonHangAdapter= new QlDonHangAdapter(donHangList,this, new IClickDonHang() {
            @Override
            public void onClickXuli(DonHang donHang, int i) {
                qlDonHangAdapter.xulidonhang(i);
                DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Don_hang").child(donHang.getId());
                reference.removeValue();
            }
        });

        rcv_donhang.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.ItemDecoration itemDecoration= new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        rcv_donhang.addItemDecoration(itemDecoration);
        rcv_donhang.setAdapter(qlDonHangAdapter);
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Don_hang");
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                DonHang donHang= snapshot.getValue(DonHang.class);

                    donHangList.add(donHang);
                qlDonHangAdapter.notifyDataSetChanged();
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