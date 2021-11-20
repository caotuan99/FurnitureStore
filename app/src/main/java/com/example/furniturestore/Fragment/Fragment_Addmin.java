package com.example.furniturestore.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.furniturestore.Interface.IClickUser;
import com.example.furniturestore.MessengerActivity;
import com.example.furniturestore.Module.User;
import com.example.furniturestore.Module.UserAdapter;
import com.example.furniturestore.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class Fragment_Addmin extends Fragment {
    private RecyclerView rcv_tuvan;
    private UserAdapter userAdapter;
    private List<User>userList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_addmin, container, false);
        rcv_tuvan=view.findViewById(R.id.rcv_chat);
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        rcv_tuvan.setLayoutManager(new LinearLayoutManager(getContext()));
        RecyclerView.ItemDecoration itemDecoration= new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL);
        rcv_tuvan.addItemDecoration(itemDecoration);
        DatabaseReference reference=database.getReference("User");
        userList= new ArrayList<>();
        userAdapter= new UserAdapter(userList ,new IClickUser() {
            @Override
            public void onClickUser(User user) {
                Intent intent= new Intent(getContext(), MessengerActivity.class);
                Bundle bundle= new Bundle();
                bundle.putSerializable("userchat",user);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        rcv_tuvan.setAdapter(userAdapter);
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                User user= snapshot.getValue(User.class);
                if(user==null) return;
                if(user.getAddmin()!=null && user.getAddmin().equals("Yes")) userList.add(user);
                userAdapter.notifyDataSetChanged();
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

        return view;
    }
}
