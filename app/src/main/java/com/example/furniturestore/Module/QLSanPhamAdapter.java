package com.example.furniturestore.Module;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.furniturestore.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class QLSanPhamAdapter extends RecyclerView.Adapter<QLSanPhamAdapter.SanPhamViewHolder>{
    public QLSanPhamAdapter(List<SanPham> mListBanan) {
        this.mListBanan = mListBanan;
    }

    private List<SanPham> mListBanan;
    private SanPham sanPham;
    @NonNull
    @Override
    public SanPhamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sanpham,parent,false);
        return new SanPhamViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SanPhamViewHolder holder, int position) {
        sanPham =mListBanan.get(position);
        if(sanPham ==null)return;
        holder.txt_banan.setText("Loại: "+ sanPham.getName());
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
    }

    @Override
    public int getItemCount() {
        if(mListBanan!=null) return mListBanan.size();
        return 0;
    }

    public class SanPhamViewHolder extends  RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{
        private TextView txt_banan,txt_gia,txt_loaigo;
        private ImageView img_banan;
        private CardView cardView;

        public SanPhamViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_banan=itemView.findViewById(R.id.txt_banan);
            txt_gia=itemView.findViewById(R.id.txt_gia);
            txt_loaigo=itemView.findViewById(R.id.txt_loaigo);
            img_banan=itemView.findViewById(R.id.img_banan);
            cardView=itemView.findViewById(R.id.cardview_banan);
            cardView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Quản lí sản phẩm:");
            menu.add(getAdapterPosition(),100,0,"Thêm sản phẩm");
            menu.add(getAdapterPosition(),101,1,"Sửa sản phẩm");
            menu.add(getAdapterPosition(),102,2,"Xóa sản phẩm");
        }
    }

    public String getIdBanan(){
        return sanPham.getId();
    }
    public void removeBanan(int i){
        mListBanan.remove(i);
        notifyDataSetChanged();
    }

}
