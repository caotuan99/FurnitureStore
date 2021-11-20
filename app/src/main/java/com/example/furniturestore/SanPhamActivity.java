package com.example.furniturestore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.furniturestore.Module.SanPham;
import com.example.furniturestore.Module.ImageAdapter2;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator3;

public class SanPhamActivity extends AppCompatActivity {
    private ViewPager2 viewPager2;
    private CircleIndicator3 circleIndicator3;
    private SanPham sanPham;
    private Button btnBuy,btnCart,btnChat;
    private TextView txtName,txtLoaigo,txtGia;
    private List<String> list=new ArrayList<>();
    private ImageAdapter2 imageAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_san_pham);
        viewPager2=findViewById(R.id.viewpager2);
        txtName=findViewById(R.id.txtName);
        txtLoaigo=findViewById(R.id.txtLoaigo);
        txtGia=findViewById(R.id.txtGia);
        btnCart=findViewById(R.id.btnCart);
        btnBuy=findViewById(R.id.btnBuy);
        btnChat=findViewById(R.id.btnChat);
        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SanPhamActivity.this,TuVanActivity.class));
            }
        });
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        CompositePageTransformer compositePageTransformer= new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        viewPager2.setPageTransformer(compositePageTransformer);
        circleIndicator3=findViewById(R.id.circleindicator3);
        imageAdapter=new ImageAdapter2(list);
        viewPager2.setAdapter(imageAdapter);
        getDulieu();
        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCart(sanPham);
            }
        });
        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void  onClick(View v) {
                List<SanPham>listMua=new ArrayList<>();
                listMua.add(sanPham);
                Intent intent= new Intent(SanPhamActivity.this, LenDonMuaHang.class);
                intent.putExtra("tong",Integer.parseInt(sanPham.getGia()));
                Bundle bundle=new Bundle();
                bundle.putSerializable("List_Buy", (Serializable) listMua);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private void addCart(SanPham sanPham) {
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        String uid=user.getUid();
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference reference=database.getReference("User").child(uid);

        reference.child("Cart").child(sanPham.getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                SanPham sanPham1=snapshot.getValue(SanPham.class);
                if(sanPham1!=null){
                    Toast.makeText(SanPhamActivity.this,"Sản phẩm đã được thêm vào giỏ hàng!",Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    reference.child("Cart").child(sanPham.getId()).setValue(sanPham).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(SanPhamActivity.this,"Thêm vào giỏ thành công!",Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SanPhamActivity.this,"Thêm vào giỏ không thành công",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    private void getDulieu(){
        Intent intent=this.getIntent();
        Bundle bundle=intent.getExtras();
        if(bundle!=null)
            sanPham =(SanPham)bundle.getSerializable("ban_an");
        txtName.setText(sanPham.getName());
        txtLoaigo.setText("Gỗ: "+ sanPham.getLoaigo());
        txtGia.setText("Giá: "+ sanPham.getGia()+"Vnd");
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("SanPham").child(sanPham.getId()).child("Image");
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String uri= (String) snapshot.getValue();
                if(uri!=null) list.add(uri);
                imageAdapter.notifyDataSetChanged();
                circleIndicator3.setViewPager(viewPager2 );
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