package com.example.furniturestore.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.furniturestore.QuanLiSanPham;
import com.example.furniturestore.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Fragment_doimatkhau extends Fragment {
    private EditText edtNewpassword,edtConfirm;
    private Button btnConfirm;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_doimatkhau, container, false);
        edtNewpassword=view.findViewById(R.id.edtNewpassword);
        edtConfirm=view.findViewById(R.id.edtconfirm);
        btnConfirm=view.findViewById(R.id.btnConfirm);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmpassword();
            }
        });

        return view;
    }

    private void confirmpassword() {
        String confirmpass = edtConfirm.getText().toString().trim();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String newPassword = edtNewpassword.getText().toString().trim();
        if (TextUtils.isEmpty(confirmpass) || TextUtils.isEmpty(newPassword)) {
            Toast.makeText(getContext(), "Vui lòng điền đủ thông tin!", Toast.LENGTH_SHORT).show();
            ;
            return;
        }
        if (!confirmpass.equals(newPassword)) {
            Toast.makeText(getContext(), "Mật khẩu không khớp!", Toast.LENGTH_SHORT).show();
            return;
        }
        ;
        user.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getContext(), "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                    edtNewpassword.setText("");
                    edtConfirm.setText("");

                } else {
                    Toast.makeText(getContext(), "Đổi mật khẩu không thành công :((", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
