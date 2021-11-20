package com.example.furniturestore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.furniturestore.Module.SanPham;
import com.example.furniturestore.Module.QLSanPhamAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class QuanLiSanPham extends AppCompatActivity {
    RecyclerView rcv_qlbanan;
     int CHECK=0;
    Spinner spnSx,spnTheloai;
    private QLSanPhamAdapter banAnAdapter;
    ImageView imgup,imgdow;
    List<SanPham> mListBanan= new ArrayList<>();
    EditText edtGia,edtLoaiGo,edtTenBanAn;
    FloatingActionButton btn_Creatsp;
    String sp="Bàn ăn";
    private Uri imgUri;
    ProgressDialog progressDialog;
    private DatabaseReference roof= FirebaseDatabase.getInstance().getReference("Image");
    private StorageReference reference= FirebaseStorage.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_li_ban_an);
        rcv_qlbanan=findViewById(R.id.rcv_qlbanan);
        spnTheloai=findViewById(R.id.spnTheloai);
        btn_Creatsp=findViewById(R.id.btn_creat);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(QuanLiSanPham.this,2);
        rcv_qlbanan.setLayoutManager(gridLayoutManager);
        setDataTheloai();
         Intent intent=getIntent();
         sp=intent.getStringExtra("sp");
        if(sp.equals("Bàn ăn")) spnTheloai.setSelection(0);
            else if(sp.equals("Bàn ghế")) spnTheloai.setSelection(1);
                 else if(sp.equals("Tủ kệ")) spnTheloai.setSelection(2);
        setAdaper();
        btn_Creatsp.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 startActivity(new Intent(QuanLiSanPham.this,ThemSanPham.class));
             }
         });



    }
    private void setDataTheloai(){
        String[] data={"Bàn ăn","Bàn ghế","Tủ kệ","Tất cả"};
        ArrayAdapter arrayAdapter=new ArrayAdapter(QuanLiSanPham.this, android.R.layout.simple_spinner_item,data);
        spnTheloai.setAdapter(arrayAdapter);
        spnTheloai.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(!sp.equals(spnTheloai.getSelectedItem().toString())){
                    sp=spnTheloai.getSelectedItem().toString();
                    setAdaper();

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
    private void setAdaper() {

        mListBanan=new ArrayList<>();
        banAnAdapter=new QLSanPhamAdapter(mListBanan);
        rcv_qlbanan.setAdapter(banAnAdapter);
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference ref=database.getReference("SanPham");
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                SanPham sanPham =snapshot.getValue(SanPham.class);
                if(sanPham !=null&& (sp.equals(sanPham.getName())||sp.equals("Tất cả"))) {
                    mListBanan.add(sanPham);
                    banAnAdapter.notifyDataSetChanged();


                }
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

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
         super.onContextItemSelected(item);
            switch (item.getItemId()){
            case 100:
                startActivity(new Intent(QuanLiSanPham.this,ThemSanPham.class));
                return true;
             case 101:
                 return true;
             case 102:
                 SanPham sanPham = mListBanan.get(item.getGroupId());
                 FirebaseDatabase database =FirebaseDatabase.getInstance();
                 DatabaseReference ref=database.getReference("SanPham").child(sanPham.getId());
                 ref.removeValue();
                 banAnAdapter.removeBanan(item.getGroupId());
                 FirebaseStorage storage=FirebaseStorage.getInstance();
                 StorageReference storageReference=storage.getReference("Image").child(sanPham.getId());
                 for(int i = 0; i< sanPham.getImage(); i++) storageReference.child(String.valueOf(i)).delete();



                 return true;
        }
        return true;
    }


}