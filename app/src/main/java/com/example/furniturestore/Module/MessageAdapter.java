package com.example.furniturestore.Module;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.furniturestore.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MassageViewHolder> {
    private List<Chat> chatList;
    public static final int MSG_LEFT=0;
    public static final int MSG_RIGHT=1;
    private String uriImg;
    private Context context;
    private FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();

    public MessageAdapter(List<Chat> chatList, String uriImg, Context context) {
        this.chatList = chatList;
        this.uriImg = uriImg;
        this.context = context;
    }

    @NonNull
    @Override
    public MassageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==MSG_RIGHT) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_chat_rigt, parent, false);
            return new MassageViewHolder(view);
        }
        else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_chat_left, parent, false);
            return new MassageViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MassageViewHolder holder, int position) {
        Chat chat= chatList.get(position);
        if (chat==null) return;
        if(position!=chatList.size()-1){
            Chat chat1=chatList.get(position+1);
            if(chat.getReceiver().equals(chat1.getReceiver())&&chat.getSender().equals(chat1.getSender())
            &&chat.getReceiver()!=user.getUid()){
                holder.profile_image.setVisibility(View.INVISIBLE);
            }

        }
        if(uriImg!=null) {
            Picasso.get().load(uriImg).into(holder.profile_image);

        }
        if(chat.getImage()!=null){
            holder.show_img.setVisibility(View.VISIBLE);
            Picasso.get().load(chat.getImage()).into(holder.show_img);
        }
        if(chat.getMessage().equals("")) {
            holder.show_msg.setVisibility(View.GONE);
        }
        holder.show_msg.setText(chat.getMessage());


    }

    @Override
    public int getItemCount() {
        if(chatList!=null) return chatList.size();
        return 0;
    }

    public class MassageViewHolder extends RecyclerView.ViewHolder{
        private TextView show_msg;
        private ImageView profile_image,show_img;

        public MassageViewHolder(@NonNull View itemView) {
            super(itemView);
            show_msg=itemView.findViewById(R.id.show_msg);
            profile_image=itemView.findViewById(R.id.profile_image);
            show_img=itemView.findViewById(R.id.show_img);
            show_img.setVisibility(View.GONE);

        }
    }

    @Override
    public int getItemViewType(int position) {
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        if(chatList.get(position).getSender().equals(user.getUid())) return MSG_RIGHT;
        else return MSG_LEFT;
    }
}
