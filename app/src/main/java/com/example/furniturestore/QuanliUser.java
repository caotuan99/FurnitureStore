package com.example.furniturestore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.furniturestore.Interface.IClickUserManager;
import com.example.furniturestore.Module.User;
import com.example.furniturestore.Module.QlUserAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class QuanliUser extends AppCompatActivity {
    private RecyclerView rcv_user;
    private ImageView avatar;
    private TextView txtEmailUser;
    private FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
    private List<User> userList;
    private QlUserAdapter qlUserAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quanli_user);
        rcv_user=findViewById(R.id.rcv_user);
        avatar=findViewById(R.id.avatar);
        txtEmailUser=findViewById(R.id.txtEmailUser);
        rcv_user.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.ItemDecoration itemDecoration= new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        rcv_user.addItemDecoration(itemDecoration);
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference reference=database.getReference("User").child(user.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user1=snapshot.getValue(User.class);
                if (user1==null) return;
                if(user1.getAvatar()!=null) Picasso.get().load(user1.getAvatar()).into(avatar);
                txtEmailUser.setText(user1.getEmail());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        setAdapter();

    }
    private void setAdapter(){
        userList=new ArrayList<>();
        qlUserAdapter =new QlUserAdapter(userList, new IClickUserManager() {
            @Override
            public void BanUser(User user) {
                FirebaseDatabase database=FirebaseDatabase.getInstance();
                DatabaseReference reference=database.getReference("User").child(user.getId());
                reference.child("ban").setValue("Yes");
            }

            @Override
            public void AddAddmin(User user) {
                FirebaseDatabase database=FirebaseDatabase.getInstance();
                DatabaseReference reference=database.getReference("User").child(user.getId());
                reference.child("addmin").setValue("Yes");
            }

            @Override
            public void unBanUser(User user) {
                FirebaseDatabase database=FirebaseDatabase.getInstance();
                DatabaseReference reference=database.getReference("User").child(user.getId());
                reference.child("ban").setValue("no");
            }

            @Override
            public void DelAddmin(User user) {
                FirebaseDatabase database=FirebaseDatabase.getInstance();
                DatabaseReference reference=database.getReference("User").child(user.getId());
                reference.child("addmin").setValue("No");
            }
        });
        rcv_user.setAdapter(qlUserAdapter);
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference reference=database.getReference("User");
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                User user1=snapshot.getValue(User.class);
                if(user1!=null) userList.add(user1);
                qlUserAdapter.notifyDataSetChanged();
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
}