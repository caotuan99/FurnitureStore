package com.example.furniturestore.Module;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.furniturestore.Interface.IClickDonHang;
import com.example.furniturestore.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class QlDonHangAdapter extends RecyclerView.Adapter<QlDonHangAdapter.DonHangViewHolder>{

    private List<DonHang> donHangList;
    private Context context;
    private IClickDonHang iClickDonHang;
    public QlDonHangAdapter(List<DonHang> donHangs, Context context,IClickDonHang iClickDonHang) {

        this.donHangList = donHangs;
        this.context =context;
        this.iClickDonHang=iClickDonHang;
    }
    @NonNull
    @Override
    public DonHangViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ql_don_hang,parent,false);
        return new DonHangViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DonHangViewHolder holder, int position) {
        DonHang donHang=donHangList.get(position);
        int i=position;
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
        holder.btnxuli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iClickDonHang.onClickXuli(donHang,i);
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
       private Button btnxuli;

        public DonHangViewHolder(@NonNull View itemView) {
            super(itemView);
            rcv_donhang = itemView.findViewById(R.id.rcv_donhang);
            tvTongtien = itemView.findViewById(R.id.tvTongtien);
            btnxuli=itemView.findViewById(R.id.btnxuli);





        }

    }
    public void xulidonhang(int i){
        donHangList.remove(i);
        notifyDataSetChanged();
    }
}
