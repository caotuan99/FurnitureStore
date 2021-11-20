package com.example.furniturestore.Module;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.example.furniturestore.Interface.IEventCartListener;
import com.example.furniturestore.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DonHangAdapter extends RecyclerView.Adapter<DonHangAdapter.DonHangViewHolder>{

    private List<DonHang> donHangList;
    private Context context;
    public DonHangAdapter(List<DonHang> donHangs, Context context) {

        this.donHangList = donHangs;
        this.context =context;

    }
    @NonNull
    @Override
    public DonHangViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.item_don_hang,parent,false);
        return new DonHangViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DonHangViewHolder holder, int position) {
        DonHang donHang=donHangList.get(position);
        if (donHang==null) return;
        holder.rcv_donhang.setLayoutManager(new LinearLayoutManager(context));
        RecyclerView.ItemDecoration itemDecoration= new DividerItemDecoration(context,DividerItemDecoration.VERTICAL);
        holder.rcv_donhang.addItemDecoration(itemDecoration);
        holder.tvTongtien.setText("đ"+donHang.getTien());
        List<SanPham> list=new ArrayList<>();
        MuahangAdapter muahangAdapter= new MuahangAdapter(list);
        holder.rcv_donhang.setAdapter(muahangAdapter);
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Don_hang").child(donHang.getId())
                .child("List_sp");
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                SanPham sanPham= snapshot.getValue(SanPham.class);
                list.add(sanPham);
                muahangAdapter.notifyDataSetChanged();
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
    public int getItemCount() {
        if(donHangList!=null) return donHangList.size();
        return 0;
    }

    public class DonHangViewHolder extends  RecyclerView.ViewHolder {
       private RecyclerView rcv_donhang;
       private TextView tvTongtien;

        public DonHangViewHolder(@NonNull View itemView) {
            super(itemView);
            rcv_donhang = itemView.findViewById(R.id.rcv_donhang);
            tvTongtien = itemView.findViewById(R.id.tvTongtien);





        }

    }
    public void removeCart(int i){
        donHangList.remove(i);
        notifyDataSetChanged();
    }
}
