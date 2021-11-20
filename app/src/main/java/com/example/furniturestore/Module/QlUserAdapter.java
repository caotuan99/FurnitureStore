package com.example.furniturestore.Module;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.furniturestore.Interface.IClickUserManager;
import com.example.furniturestore.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class QlUserAdapter extends RecyclerView.Adapter<QlUserAdapter.UserViewHolder> {
    private List<User> userList;
    private IClickUserManager iClickUserManager;

    public QlUserAdapter(List<User> userList, IClickUserManager iClickUserManager) {
        this.userList = userList;
        this.iClickUserManager=iClickUserManager;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_qluser,parent,false);
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
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference reference=database.getReference("User").child(user.getId());
        reference.child("ban").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String ban= (String) snapshot.getValue();
                if(ban==null) {
                    holder.txtUnBan.setVisibility(View.GONE);
                    holder.txtBan.setVisibility(View.VISIBLE);
                    return;
                }
                if(ban.equals("Yes")) {
                    holder.txtBan.setVisibility(View.GONE);
                    holder.txtUnBan.setVisibility(View.VISIBLE);
                }
                else {
                    holder.txtUnBan.setVisibility(View.GONE);
                    holder.txtBan.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        reference.child("addmin").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String addmin= (String) snapshot.getValue();
                if (addmin==null) {
                    holder.txtXoaQuyen.setVisibility(View.GONE);
                    holder.txtCapQuyen.setVisibility(View.VISIBLE);
                    return;
                }
                if(addmin.equals("Yes")) {
                    holder.txtCapQuyen.setVisibility(View.GONE);
                    holder.txtXoaQuyen.setVisibility(View.VISIBLE);
                }
                else {
                    holder.txtXoaQuyen.setVisibility(View.GONE);
                    holder.txtCapQuyen.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.txtBan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iClickUserManager.BanUser(user);
                holder.txtBan.setVisibility(View.GONE);
                holder.txtUnBan.setVisibility(View.VISIBLE);

            }
        });
        holder.txtUnBan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iClickUserManager.unBanUser(user);
                holder.txtUnBan.setVisibility(View.GONE);
                holder.txtBan.setVisibility(View.VISIBLE);
            }
        });
        holder.txtCapQuyen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iClickUserManager.AddAddmin(user);
                holder.txtCapQuyen.setVisibility(View.GONE);
                holder.txtXoaQuyen.setVisibility(View.VISIBLE);
            }
        });
        holder.txtXoaQuyen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iClickUserManager.DelAddmin(user);
                holder.txtXoaQuyen.setVisibility(View.GONE);
                holder.txtCapQuyen.setVisibility(View.VISIBLE);
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
        private TextView txtUserName,txtemail,txtBan,txtUnBan,txtCapQuyen,txtXoaQuyen;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            avataruser=itemView.findViewById(R.id.avataruser);
            txtUserName=itemView.findViewById(R.id.txtUserName);
            txtemail=itemView.findViewById(R.id.txtemail);
            txtBan=itemView.findViewById(R.id.txtBan);
            txtUnBan=itemView.findViewById(R.id.txtUnBan);
            txtCapQuyen=itemView.findViewById(R.id.txtCapQuyen);
            txtXoaQuyen=itemView.findViewById(R.id.txtXoaQuyen);
        }
    }
}
