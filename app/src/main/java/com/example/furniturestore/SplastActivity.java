package com.example.furniturestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SplastActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splast);
        Handler handler= new Handler();
        boolean b = handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                nextActivity();
            }
        },2000);
    }

    private void nextActivity() {

        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        if(user==null) startActivity(new Intent(this,LoginActivity.class));
        else {
            FirebaseDatabase database=FirebaseDatabase.getInstance();
            DatabaseReference reference=database.getReference("User").child(user.getUid());
            reference.child("ban").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String ban= (String) snapshot.getValue();
                    if(ban==null||!ban.equals("Yes"))  {
                        gotoMain();
                        return;
                    } else {
                        Toast.makeText(getApplication(),"Tài khoản của bạn đã bị cấm",Toast.LENGTH_LONG).show();
                        gotoLogin();
                        return;
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
    public void gotoMain(){
        startActivity(new Intent(this,MainActivity.class));
    }
    private void gotoLogin(){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(this,LoginActivity.class));
        finish();
    }
}