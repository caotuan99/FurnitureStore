package com.example.furniturestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.furniturestore.Module.SanPham;
import com.example.furniturestore.Module.ImageAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import java.util.ArrayList;
import java.util.List;

import gun0912.tedbottompicker.TedBottomPicker;
import gun0912.tedbottompicker.TedBottomSheetDialogFragment;
import me.relex.circleindicator.CircleIndicator3;

public class ThemSanPham extends AppCompatActivity {
    private ImageView imgup;
    private EditText edtLoaigo,edtGia;
    private Spinner spnName;
    private Button btnThemsp;
    private CircleIndicator3 circleIndicator3;
    private ViewPager2 viewPager2;
    private ProgressDialog progressDialog;
    public List<Uri> uriList,muriList;
    private ImageAdapter imageAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_san_pham);
        imgup=findViewById(R.id.imgup);
        spnName=findViewById(R.id.spnName);
        setDataSpiner();
        edtLoaigo=findViewById(R.id.edtLoaigo);
        edtGia=findViewById(R.id.edtGia);
        viewPager2=findViewById(R.id.viewpager_2);
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        CompositePageTransformer compositePageTransformer= new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        viewPager2.setPageTransformer(compositePageTransformer);
        circleIndicator3=findViewById(R.id.indicator);
        btnThemsp=findViewById(R.id.btnThemSp);
        uriList=new ArrayList<>();
        muriList=new ArrayList<>();

      //  progressDialog=new ProgressDialog(this);
        imgup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reQuestPermission();

            }
        });
        btnThemsp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(edtGia.getText().toString())
                        ||TextUtils.isEmpty(edtLoaigo.getText().toString())){
                    Toast.makeText(ThemSanPham.this,"Vui lòng điền đầy đủ thông tin!!!",Toast.LENGTH_LONG).show();
                    return;
                }
           //     progressDialog.show();
                upDataonFirebase();
             //   progressDialog.dismiss();
                finish();
            }
        });
    }
    private void setDataSpiner(){
        String[] data={"Bàn ăn","Bàn ghế","Tủ kệ"};
        ArrayAdapter arrayAdapter=new ArrayAdapter(ThemSanPham.this, android.R.layout.simple_spinner_item,data);
        spnName.setAdapter(arrayAdapter);
        spnName.setSelection(0);

    }
    private void upDataonFirebase() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        String timestamp =String.valueOf(System.currentTimeMillis());
        StorageReference storageRef = storage.getReference().child("Image").child(timestamp);
       // progressDialog.show();
        List<String> mlist=new ArrayList<>();
        for (int i=0;i< muriList.size();i++){
            int finalI = i;
            storageRef.child(String.valueOf(i)).putFile(muriList.get(i)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> uriTask=taskSnapshot.getStorage().getDownloadUrl();
                    while (!uriTask.isSuccessful());
                    Uri dowloaduri=uriTask.getResult();
                    DatabaseReference ref=FirebaseDatabase.getInstance().getReference("SanPham");
                    ref.child(timestamp).child("Image").child(String.valueOf(finalI)).setValue(dowloaduri.toString());
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplication(),"Lỗi Upload",Toast.LENGTH_LONG).show();
                }
            });

        }
        String name=spnName.getSelectedItem().toString();
            SanPham sanPham =new SanPham(timestamp,name,edtLoaigo.getText().toString()
                    ,edtGia.getText().toString(),muriList.size());
            DatabaseReference ref=FirebaseDatabase.getInstance().getReference("SanPham");
            ref.child(timestamp).setValue(sanPham);
        }


    private void reQuestPermission() {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
              openImagePicker();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(ThemSanPham.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };
        TedPermission.create()
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();
    }

    private void openImagePicker() {
        List<Uri> selectedUriList;
        TedBottomPicker.with(ThemSanPham.this)
                .setPeekHeight(1600)
                .showTitle(false)
                .setCompleteButtonText("Done")
                .setEmptySelectionText("No Select")
                .setSelectedUriList(uriList)
                .showMultiImage(new TedBottomSheetDialogFragment.OnMultiImageSelectedListener() {
                    @Override
                    public void onImagesSelected(List<Uri> uriList) {
                        // here is selected image uri list
                        imageAdapter=new ImageAdapter(uriList);
                        muriList=uriList;
                        viewPager2.setAdapter(imageAdapter);
                        imageAdapter.notifyDataSetChanged();
                        circleIndicator3.setViewPager(viewPager2 );
                    }
                });

    }

}