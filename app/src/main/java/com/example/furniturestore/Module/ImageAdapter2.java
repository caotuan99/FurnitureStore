package com.example.furniturestore.Module;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.furniturestore.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageAdapter2 extends RecyclerView.Adapter<ImageAdapter2.ImageAdapterViewHolder> {
    private List<String> uriList;

    public ImageAdapter2(List<String> uriList) {
        this.uriList = uriList;
    }

    @NonNull
    @Override
    public ImageAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_imgsp,parent,false);
        return new ImageAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageAdapterViewHolder holder, int position) {
        String uri=uriList.get(position);
        if(uri==null) return;
        Picasso.get().load(uri).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        if(uriList!=null)return uriList.size();
        return 0;
    }

    public class ImageAdapterViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        public ImageAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.image);
        }
    }
}
