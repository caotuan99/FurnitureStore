package com.example.furniturestore.Module;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.furniturestore.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageAdapterViewHolder> {
    private List<Uri> uriList;

    public ImageAdapter(List<Uri> uriList) {
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
        Uri uri=uriList.get(position);
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
