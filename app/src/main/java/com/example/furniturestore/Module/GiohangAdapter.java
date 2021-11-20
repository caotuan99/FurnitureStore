package com.example.furniturestore.Module;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.example.furniturestore.Interface.IEventCartListener;
import com.example.furniturestore.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class GiohangAdapter extends RecyclerView.Adapter<GiohangAdapter.GioHangViewHolder>{

    private List<SanPham> mListBanan;
    private IEventCartListener iEventCartListener;
    public GiohangAdapter(List<SanPham> mListBanan, IEventCartListener listener) {

        this.mListBanan = mListBanan;
        this.iEventCartListener =listener;

    }
    @NonNull
    @Override
    public GioHangViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gio_hang,parent,false);
        return new GioHangViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GioHangViewHolder holder, int position) {
        SanPham sanPham =mListBanan.get(position);
        int i=position;
        if(sanPham ==null)return;
        holder.txt_TenSanpham.setText("Loại: "+ sanPham.getName());
        holder.txtGo.setText("Gỗ: "+ sanPham.getLoaigo());
        holder.txtGiaSanpham.setText("Giá: "+ sanPham.getGia()+"VND");
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("SanPham").child(sanPham.getId()).child("Image").child("0");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String uri= (String) snapshot.getValue();
                Picasso.get().load(uri).into(holder.img_sanpham);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    iEventCartListener.onChecked(sanPham,isChecked);
            }
        });
        holder.layoutdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               removeCart(i);
               iEventCartListener.deleteCart(sanPham);
            }
        });
        holder.layoutSanpham.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iEventCartListener.onClick(sanPham);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(mListBanan!=null) return mListBanan.size();
        return 0;
    }

    public class GioHangViewHolder extends  RecyclerView.ViewHolder {
        private TextView txt_TenSanpham,txtGo, txtGiaSanpham;
        private ImageView img_sanpham;
        private CheckBox checkBox;
        private SwipeRevealLayout swipe;
        private LinearLayout layoutdelete;
        private ConstraintLayout layoutSanpham;
        public GioHangViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_TenSanpham = itemView.findViewById(R.id.txtTenSanpham);
            swipe = itemView.findViewById(R.id.swipe);
            layoutdelete = itemView.findViewById(R.id.layoutdelete);
            txtGo = itemView.findViewById(R.id.txtGo);
            txtGiaSanpham = itemView.findViewById(R.id.txtGiasanpham);
            img_sanpham = itemView.findViewById(R.id.imgsanpham);
            checkBox = itemView.findViewById(R.id.checkbox);
            layoutSanpham = itemView.findViewById(R.id.layoutsanpham);




        }

    }
    public void removeCart(int i){
        mListBanan.remove(i);
        notifyDataSetChanged();
    }
}
