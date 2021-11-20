package com.example.furniturestore.Module;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.furniturestore.Interface.IClickitemListener;
import com.example.furniturestore.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SanPhamAdapter extends RecyclerView.Adapter<SanPhamAdapter.SanPhamViewHolder>{

    private List<SanPham> mListBanan;
    private IClickitemListener iClickitemListener;
    public SanPhamAdapter(IClickitemListener listener, List<SanPham> mListBanan) {

        this.mListBanan = mListBanan;
        this.iClickitemListener=listener;
    }
    @NonNull
    @Override
    public SanPhamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sanpham,parent,false);
        return new SanPhamViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SanPhamViewHolder holder, int position) {
        SanPham sanPham =mListBanan.get(position);
        if(sanPham ==null)return;
        holder.txt_banan.setText("Tên: "+ sanPham.getName());
        holder.txt_loaigo.setText("Gỗ: "+ sanPham.getLoaigo());
        holder.txt_gia.setText("Giá: "+ sanPham.getGia());
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("SanPham").child(sanPham.getId()).child("Image").child("0");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String uri= (String) snapshot.getValue();
                Picasso.get().load(uri).into(holder.img_banan);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iClickitemListener.onClickItem(sanPham);
            }
        });

    }

    @Override
    public int getItemCount() {
        if(mListBanan!=null) return mListBanan.size();
        return 0;
    }

    public class SanPhamViewHolder extends  RecyclerView.ViewHolder {
        private TextView txt_banan, txt_gia, txt_loaigo;
        private ImageView img_banan;
        private CardView cardView;

        public SanPhamViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_banan = itemView.findViewById(R.id.txt_banan);
            txt_gia = itemView.findViewById(R.id.txt_gia);
            txt_loaigo = itemView.findViewById(R.id.txt_loaigo);
            img_banan = itemView.findViewById(R.id.img_banan);
            cardView = itemView.findViewById(R.id.cardview_banan);


        }

    }
}
