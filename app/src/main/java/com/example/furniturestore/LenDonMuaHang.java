package com.example.furniturestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.furniturestore.Interface.IEventCartListener;
import com.example.furniturestore.Module.DonHang;
import com.example.furniturestore.Module.GiohangAdapter;
import com.example.furniturestore.Module.MuahangAdapter;
import com.example.furniturestore.Module.SanPham;
import com.example.furniturestore.Module.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LenDonMuaHang extends AppCompatActivity {
    private RecyclerView rcv_muahang;
    private MuahangAdapter muahangAdapter;
    private List<SanPham> listMua=new ArrayList<>();
    private FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
    private EditText edtdiachi,edtnumberphone;
    private TextView txtThanhToan;
    private int Tongtien=0;
    private Button btnDathang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_len_don_mua_hang);
        rcv_muahang=findViewById(R.id.rcv_muahang);
        btnDathang=findViewById(R.id.btnDathang);
        edtdiachi=findViewById(R.id.edtdiachi);
        txtThanhToan=findViewById(R.id.txtThanhToan);
        edtnumberphone=findViewById(R.id.edtnumberphone);
        rcv_muahang.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.ItemDecoration itemDecoration= new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        rcv_muahang.addItemDecoration(itemDecoration);
        Intent intent=this.getIntent();
        Tongtien=intent.getIntExtra("tong",0);
        Bundle bundle=intent.getExtras();
        if(bundle!=null) listMua= (List<SanPham>) bundle.getSerializable("List_Buy");
        muahangAdapter= new MuahangAdapter(listMua);
        rcv_muahang.setAdapter(muahangAdapter);
        muahangAdapter.notifyDataSetChanged();
        setInfor();
        txtThanhToan.setText("đ"+Tongtien);
        btnDathang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dathang(listMua,Tongtien);
                Toast.makeText(getApplication(),"Đặt hàng thành công",Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }

    private void dathang(List<SanPham> listMua, int tongtien) {
        //xoa khoi gio hang
        DatabaseReference reference;
        for(SanPham sanPham:listMua){
            reference=FirebaseDatabase.getInstance().getReference("User").child(user.getUid()).child("Cart");
            reference.child(sanPham.getId()).removeValue();
        }
        //them vao don hang
        String timestamp=String.valueOf(System.currentTimeMillis());
        DonHang donHang= new DonHang(timestamp,user.getUid(),String.valueOf(tongtien),edtdiachi.getText().toString(),edtnumberphone.getText().toString());

        reference=FirebaseDatabase.getInstance().getReference("Don_hang").child(donHang.getId());
        reference.setValue(donHang);

        reference=FirebaseDatabase.getInstance().getReference("Don_hang").child(timestamp).child("List_sp");

        for(SanPham sanPham:listMua){
            reference.child(sanPham.getId()).setValue(sanPham);
        }

    }

    private void setInfor(){
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference reference=database.getReference("User").child(user.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User muser=snapshot.getValue(User.class);
                if(muser==null) return;
                edtdiachi.setText(muser.getAddress());
                edtnumberphone.setText(muser.getNumberphone());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}