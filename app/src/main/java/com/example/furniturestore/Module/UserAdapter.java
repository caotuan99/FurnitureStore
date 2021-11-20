package com.example.furniturestore.Module;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.furniturestore.Interface.IClickUser;
import com.example.furniturestore.R;

import com.squareup.picasso.Picasso;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private List<User> userList;
    private IClickUser iClickUser;

    public UserAdapter(List<User> userList,IClickUser iClickUser) {
        this.userList = userList;
        this.iClickUser=iClickUser;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user,parent,false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user=userList.get(position);
        if(user==null) return;
        if(user.getAvatar()!=null)
        Picasso.get().load(user.getAvatar()).into(holder.avataruser);
        holder.txtUserName.setText(user.getName());
        holder.txtemail.setText(user.getEmail());
        holder.btnuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iClickUser.onClickUser(user);
            }
        });


    }

    @Override
    public int getItemCount() {
        if(userList!=null) return userList.size();
        return 0;
    }

    public class UserViewHolder extends RecyclerView.ViewHolder{
        private ImageView avataruser;
        private TextView txtUserName,txtemail;
        private ConstraintLayout btnuser;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            avataruser=itemView.findViewById(R.id.avataruser);
            txtUserName=itemView.findViewById(R.id.txtUserName);
            txtemail=itemView.findViewById(R.id.txtemail);
            btnuser=itemView.findViewById(R.id.user);
        }
    }
}
