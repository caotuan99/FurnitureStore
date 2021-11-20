package com.example.furniturestore.Fragment;

import android.Manifest;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.furniturestore.MainActivity;
import com.example.furniturestore.Module.Image;
import com.example.furniturestore.Module.ImageAdapter;
import com.example.furniturestore.Module.User;
import com.example.furniturestore.R;
import com.example.furniturestore.ThemSanPham;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;
import com.squareup.picasso.Picasso;

import java.util.List;

import gun0912.tedbottompicker.TedBottomPicker;
import gun0912.tedbottompicker.TedBottomSheetDialogFragment;

public class Fragment_account extends Fragment {
    private EditText edtTen,edtDiachi,edtSdt;
    private TextView tvEmail;
    private Button btnUpdate;
    private ImageView avatarUser;
    private Uri imgUri;
    FirebaseUser muser=FirebaseAuth.getInstance().getCurrentUser();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_account, container, false);
        edtTen=view.findViewById(R.id.edtTen);
        edtDiachi=view.findViewById(R.id.edtDiachi);
        edtSdt=view.findViewById(R.id.edtSdt);
        tvEmail=view.findViewById(R.id.tvEmail);
        btnUpdate=view.findViewById(R.id.btnUpdate);
        avatarUser=view.findViewById(R.id.avatarUser);
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference reference=database.getReference("User").child(muser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user=snapshot.getValue(User.class);
                if(user==null) return;
                edtTen.setText(user.getName());
                edtDiachi.setText(user.getAddress());
                edtSdt.setText(user.getNumberphone());
                tvEmail.setText(user.getEmail());
                if(user.getAvatar()==null||user.getAvatar().length()<=0) return;
                Picasso.get().load(user.getAvatar()).into(avatarUser);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(edtTen.getText().toString())||TextUtils.isEmpty(edtSdt.getText().toString())||TextUtils.isEmpty(edtDiachi.getText().toString())){
                    Toast.makeText(getContext(),"Vui lòng điền đầy đủ thông tin ",Toast.LENGTH_SHORT).show();
                }
                updateUser();
                Toast.makeText(getContext(),"Cập nhật thành công",Toast.LENGTH_SHORT).show();
            }
        });
        avatarUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            reQuestPermission();

            }
        });

        return view;
    }

    private void updateUser() {
        if(imgUri!=null) {
            updateImage();

        }
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference reference=database.getReference("User").child(muser.getUid());
        reference.child("name").setValue(edtTen.getText().toString());
        reference.child("address").setValue(edtDiachi.getText().toString());
        reference.child("numberphone").setValue(edtSdt.getText().toString());
    }

    private void updateImage() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference().child("Avatar").child(muser.getUid());
        storageRef.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask=taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isSuccessful());
                Uri dowloaduri=uriTask.getResult();
                FirebaseDatabase database=FirebaseDatabase.getInstance();
                DatabaseReference reference=database.getReference("User").child(muser.getUid()).child("avatar");
                reference.setValue(dowloaduri.toString());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(),"Gửi ảnh không thành công",Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void reQuestPermission() {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                openImagePicker();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(getContext(), "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };
        TedPermission.create()
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();
    }

    private void openImagePicker() {
        TedBottomPicker.with(getActivity())
                .show(new TedBottomSheetDialogFragment.OnImageSelectedListener() {
                    @Override
                    public void onImageSelected(Uri uri) {
                        // here is selected image uri
                        avatarUser.setImageURI(uri);
                        imgUri=uri;
                    }
                });

    }


}
