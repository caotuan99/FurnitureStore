package com.example.furniturestore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.furniturestore.Module.Chat;
import com.example.furniturestore.Module.ImageAdapter;
import com.example.furniturestore.Module.MessageAdapter;
import com.example.furniturestore.Module.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import gun0912.tedbottompicker.TedBottomPicker;
import gun0912.tedbottompicker.TedBottomSheetDialogFragment;

public class MessengerActivity extends AppCompatActivity {
    private ImageView avatar,msgImg;
    private TextView txtName;
    private ImageButton btnSend,btnSendImg;
    private EditText edtMessenger;
    private List<Chat> chatList;
    private RecyclerView rcv_messenger;
    private  FirebaseUser user1;
    private MessageAdapter messageAdapter;
    private Uri uriImg=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger);
        avatar=findViewById(R.id.avatar);
        msgImg=findViewById(R.id.msgImg);
        btnSendImg=findViewById(R.id.btnsendImg);
        txtName=findViewById(R.id.txtName);
        btnSend=findViewById(R.id.btnSend);
        edtMessenger=findViewById(R.id.edtMessenger);
        rcv_messenger=findViewById(R.id.rcv_messenger);
        rcv_messenger.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext());
         linearLayoutManager.setStackFromEnd(true);
        rcv_messenger.setLayoutManager(linearLayoutManager);
        user1= FirebaseAuth.getInstance().getCurrentUser();
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        User user= (User) bundle.getSerializable("userchat");
        if(user.getAvatar()!=null)
        Picasso.get().load(user.getAvatar()).into(avatar);
        txtName.setText(user.getName());
        readMessage(user);

            btnSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        if(!TextUtils.isEmpty(edtMessenger.getText().toString())){
                            sendMessenger(user);

                        }

                    if(uriImg!=null) sendImg(user);
                    if(uriImg!=null||!TextUtils.isEmpty(edtMessenger.getText().toString())) sendlistChat(user);
                        edtMessenger.setText("");
                        msgImg.setVisibility(View.GONE);
                        uriImg=null;


                }
            });

        btnSendImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reQuestPermission();
            }
        });

    }

    private void sendImg(User user) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        String timestamp =String.valueOf(System.currentTimeMillis());
        StorageReference storageRef = storage.getReference().child("Image").child(timestamp+user.getId()+user1.getUid());
        storageRef.putFile(uriImg).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isSuccessful()) ;
                Uri dowloaduri = uriTask.getResult();
                String uirdown = dowloaduri.toString();
                String timestamp=String.valueOf(System.currentTimeMillis());
                HashMap<String,Object> hashMap=new HashMap<>();
                hashMap.put("sender",user1.getUid());
                hashMap.put("receiver",user.getId());
                    hashMap.put("message","");

                hashMap.put("image",uirdown);
                hashMap.put("timestamp",timestamp);
                DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Chats");
                reference.push().setValue(hashMap);

            }
        });
    }

    private void sendlistChat(User user) {
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("User");
        reference.child(user1.getUid()).child("List_chat").child(user.getId()).setValue(user);
        reference= FirebaseDatabase.getInstance().getReference("User");
        reference.child(user1.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User muser= snapshot.getValue(User.class);
                DatabaseReference ref=FirebaseDatabase.getInstance().getReference("User");
                ref.child(user.getId()).child("List_chat").child(muser.getId()).setValue(muser);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
                Toast.makeText(MessengerActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };
        TedPermission.create()
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();
    }
    private void openImagePicker() {
        TedBottomPicker.with(MessengerActivity.this)
                .show(new TedBottomSheetDialogFragment.OnImageSelectedListener() {
                    @Override
                    public void onImageSelected(Uri uri) {
                        // here is selected image uri
                        msgImg.setVisibility(View.VISIBLE);
                        msgImg.setImageURI(uri);
                        uriImg=uri;
                    }
                });

    }
    private void sendMessenger(User user) {
        String timestamp=String.valueOf(System.currentTimeMillis());
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("sender",user1.getUid());
        hashMap.put("receiver",user.getId());
        hashMap.put("message",edtMessenger.getText().toString());
        hashMap.put("timestamp",timestamp);
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Chats");
        reference.push().setValue(hashMap);

    }
    private  void readMessage(User user){
        chatList= new ArrayList<>();
        messageAdapter=new MessageAdapter(chatList,user.getAvatar(),MessengerActivity.this);
        rcv_messenger.setAdapter(messageAdapter);
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Chats");
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Chat chat=snapshot.getValue(Chat.class);
                if((chat.getSender().equals(user.getId())&&chat.getReceiver().equals(user1.getUid()))||
                        (chat.getSender().equals(user1.getUid()))&&chat.getReceiver().equals(user.getId()))
                {
                    chatList.add(chat);
                }
                messageAdapter.notifyDataSetChanged();
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