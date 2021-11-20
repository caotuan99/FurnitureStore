package com.example.furniturestore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.furniturestore.Module.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class RegisterActivity extends AppCompatActivity {
    EditText edtEmail,edtPassword,edtName,edtAddress,edtNumberphone;
    FirebaseAuth mAuth;
    User user;
    Button btnRegister;;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        edtEmail=findViewById(R.id.edtEmail);
        edtPassword=findViewById(R.id.edtPassword);
        edtName=findViewById(R.id.edtName);
        edtAddress=findViewById(R.id.edtAddress);
        progressDialog= new ProgressDialog(this);
        edtNumberphone=findViewById(R.id.edtNumberPhone);
        btnRegister=findViewById(R.id.btnRegister);
        mAuth=FirebaseAuth.getInstance();
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSignUp();
            }
        });
    }
    private void onClickSignUp(){
        String email=edtEmail.getText().toString().trim();
        String password=edtPassword.getText().toString().trim();
        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            // Toast.makeText(RegisterActivity.this, "Đăng kí thành công.", Toast.LENGTH_SHORT).show();
                            FirebaseUser mUser = mAuth.getCurrentUser();
                            user= new User(mUser.getUid(),email,edtName.getText().toString(),edtAddress.getText().toString(),edtNumberphone.getText().toString());
                            FirebaseDatabase database= FirebaseDatabase.getInstance();
                            DatabaseReference ref= database.getReference("User");
                            String uid=mAuth.getCurrentUser().getUid();
                            ref.child(uid).setValue(user, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                    Toast.makeText(getApplication(),"Đăng kí thành công", Toast.LENGTH_SHORT)
                                            .show();
                                }
                            });
                            startActivity(new Intent(RegisterActivity.this,MainActivity.class));
                            finishAffinity();

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(RegisterActivity.this, "Đăng kí không thành công.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}