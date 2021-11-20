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

import com.example.furniturestore.Interface.IEventCartListener;
import com.example.furniturestore.LenDonMuaHang;
import com.example.furniturestore.Module.DonHang;
import com.example.furniturestore.Module.DonHangAdapter;
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

public class Fragment_donhang extends Fragment {
    private RecyclerView rcv_donhang;
    private List<DonHang> donHangList;
    private DonHangAdapter donHangAdapter;
    private FirebaseUser user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mview= inflater.inflate(R.layout.fragment_donhang, container, false);
        rcv_donhang=mview.findViewById(R.id.rcv_cacdonhang);
        donHangList=new ArrayList<>();
        user=FirebaseAuth.getInstance().getCurrentUser();
        donHangAdapter= new DonHangAdapter(donHangList,getContext());
        rcv_donhang.setLayoutManager(new LinearLayoutManager(getContext()));
        RecyclerView.ItemDecoration itemDecoration= new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL);
        rcv_donhang.addItemDecoration(itemDecoration);
        rcv_donhang.setAdapter(donHangAdapter);
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Don_hang");
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                DonHang donHang= snapshot.getValue(DonHang.class);
                if(donHang.getUid().equals(user.getUid()))
                donHangList.add(donHang);
                donHangAdapter.notifyDataSetChanged();
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
        return  mview;



    }
}
