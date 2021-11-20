package com.example.furniturestore.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.furniturestore.BanAnActivity;
import com.example.furniturestore.Interface.IEventCartListener;
import com.example.furniturestore.LenDonMuaHang;
import com.example.furniturestore.Module.GiohangAdapter;
import com.example.furniturestore.Module.SanPham;
import com.example.furniturestore.R;
import com.example.furniturestore.SanPhamActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Fragment_giohang extends Fragment {
    private RecyclerView rcv_giohang;
    private List<SanPham> sanPhamList;
    private CheckBox checkBoxall;
    private GiohangAdapter giohangAdapter;
    private TextView txtTongtien;
    private int tongtien,sl;
    private Button btnMuaHang;
    private List<SanPham> listMua;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mview= inflater.inflate(R.layout.giohang_fragment, container, false);
        rcv_giohang=mview.findViewById(R.id.rcv_giohang);
        txtTongtien=mview.findViewById(R.id.txtTongtien);
        btnMuaHang=mview.findViewById(R.id.btnMuaHang);
        checkBoxall=mview.findViewById(R.id.checkboxAll);
        listMua=new ArrayList<>();
        tongtien=0   ; sl=0;
        btnMuaHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getContext(), LenDonMuaHang.class);
                intent.putExtra("tong",tongtien);
                Bundle bundle=new Bundle();
                bundle.putSerializable("List_Buy", (Serializable) listMua);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        rcv_giohang.setLayoutManager(new LinearLayoutManager(getContext()));
        RecyclerView.ItemDecoration itemDecoration= new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL);
        rcv_giohang.addItemDecoration(itemDecoration);
        setAdaper();
        checkBoxall.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });
        return mview;

    }
    private void setAdaper() {

        sanPhamList=new ArrayList<>();
        giohangAdapter=new GiohangAdapter(sanPhamList, new IEventCartListener() {
            @Override
            public void onChecked(SanPham sanPham, boolean isChecked) {
                if(isChecked) {
                    tongtien+=Integer.parseInt(sanPham.getGia());
                    sl++;
                    String s=String.valueOf(sl);
                    txtTongtien.setText(String.valueOf(tongtien)+"đ");
                    btnMuaHang.setText("Mua hàng("+s+")");
                    listMua.add(sanPham);
                } else {
                    sl--;
                    tongtien-=Integer.parseInt(sanPham.getGia());
                    txtTongtien.setText(String.valueOf(tongtien)+"đ");
                    String s=String.valueOf(sl);
                    btnMuaHang.setText("Mua hàng("+s+")");
                    listMua.remove(listMua.indexOf(sanPham));
                }

            }

            @Override
            public void deleteCart(SanPham sanPham) {
                FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                String uid=user.getUid();
                FirebaseDatabase database =FirebaseDatabase.getInstance();
                DatabaseReference ref=database.getReference("User").child(uid).child("Cart").child(sanPham.getId());
                ref.removeValue();
            }

            @Override
            public void onClick(SanPham sanPham) {
                Intent intent= new Intent(getContext(), SanPhamActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("ban_an", sanPham);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        rcv_giohang.setAdapter(giohangAdapter);
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        String uid=user.getUid();
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference ref=database.getReference("User").child(uid).child("Cart");
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                SanPham sanPham =snapshot.getValue(SanPham.class);
                if(sanPham !=null) {
                    sanPhamList.add(sanPham);
                    giohangAdapter.notifyDataSetChanged();


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
}
