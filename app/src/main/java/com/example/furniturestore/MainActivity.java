package com.example.furniturestore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.furniturestore.Fragment.Fragment_account;
import com.example.furniturestore.Fragment.Fragment_doimatkhau;
import com.example.furniturestore.Fragment.Fragment_donhang;
import com.example.furniturestore.Fragment.Fragment_giohang;
import com.example.furniturestore.Fragment.Fragment_home;
import com.example.furniturestore.Fragment.Fragment_quanli;
import com.example.furniturestore.Module.User;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    private static final int FRAGMENT_HOME=0;
    TextView txtName,txtGmail;
    ImageView img_avatar;

    String addmin="caotuan.lhp@gmail.com";
    private static final int FRAGMENT_GIOHANG=1;
    private static final int FRAGMENT_DONHANG=6;
    private static final int FRAGMENT_ACCOUNT=2 ;
    private static final int FRAGMENT_CHANGEPASSWORD=3 ;
    private static final int FRAGMENT_QUANLI=5;
    private int CHECK_FRAGMENT=0 ;
    NavigationView navigationView;
    DatabaseReference ref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        navigationView=findViewById(R.id.navigtionview);
        String useEmail=user.getEmail();
        checkAddmin();
        txtGmail=navigationView.getHeaderView(0).findViewById(R.id.txtEmail);
        img_avatar=navigationView.getHeaderView(0).findViewById(R.id.img_avatar);
        txtName=navigationView.getHeaderView(0).findViewById(R.id.txtName);
        drawerLayout=findViewById(R.id.drawLayout);
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle= new ActionBarDrawerToggle(this,drawerLayout,toolbar,
                R.string.navigation_drawer_open ,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        showUserInformation();
        replaceFragment(new Fragment_home());
        navigationView.getMenu().findItem(R.id.nav_home).setChecked(true);
        navigationView.setNavigationItemSelectedListener(this);

    }
    private  void  checkAddmin(){
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference reference=database.getReference("User").child(user.getUid()).child("addmin");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String addmin= (String) snapshot.getValue();
                if(addmin==null||!addmin.equals("Yes")) {
                    removeAddmin();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.nav_home){
            if(CHECK_FRAGMENT!=FRAGMENT_HOME){
                CHECK_FRAGMENT=FRAGMENT_HOME;
                replaceFragment(new Fragment_home());
            }
        } else
            if(id==R.id.nav_giohang){
                if(CHECK_FRAGMENT!=FRAGMENT_GIOHANG){
                    CHECK_FRAGMENT=FRAGMENT_GIOHANG;
                    replaceFragment(new Fragment_giohang());
                }
            } else
                if(id==R.id.nav_myprofile){
                    if(CHECK_FRAGMENT!=FRAGMENT_ACCOUNT){
                        CHECK_FRAGMENT=FRAGMENT_ACCOUNT;
                        replaceFragment(new Fragment_account());
                    }
                } else
                    if(id==R.id.nav_changepassword) {
                        if(CHECK_FRAGMENT!=FRAGMENT_CHANGEPASSWORD){
                            CHECK_FRAGMENT=FRAGMENT_CHANGEPASSWORD;
                            replaceFragment(new Fragment_doimatkhau());
                        }
                    }
                    else
                        if(id==R.id.nav_addmin){
                            if(CHECK_FRAGMENT!=FRAGMENT_QUANLI){
                                CHECK_FRAGMENT=FRAGMENT_QUANLI;
                                replaceFragment(new Fragment_quanli());
                                navigationView.getMenu().findItem(R.id.nav_addmin).setChecked(true);
                            }
                        }else
                        if(id==R.id.nav_donhang){
                            if(CHECK_FRAGMENT!=FRAGMENT_DONHANG){
                                CHECK_FRAGMENT=FRAGMENT_DONHANG;
                                replaceFragment(new Fragment_donhang());

                            }
                        }
                    else
                        if(id==R.id.nav_signout){
                            FirebaseAuth.getInstance().signOut();
                            startActivity(new Intent(MainActivity.this,LoginActivity.class));
                            finish();
                        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else
        if(CHECK_FRAGMENT!=FRAGMENT_HOME){
            replaceFragment(new Fragment_home());
            CHECK_FRAGMENT=FRAGMENT_HOME;
        }
        else finishAffinity();
    }
    private void replaceFragment(Fragment fragment){
        FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame,fragment);
        transaction.commit();
    }
    private void showUserInformation(){
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        if(user == null) return;
        String email=user.getEmail();
        String uid=user.getUid();
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        ref=database.getReference("User").child(uid);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user1=snapshot.getValue(User.class);
                if(user1==null) {
                    Toast.makeText(getApplication(),uid,Toast.LENGTH_LONG).show();
                    return;
                }
                txtGmail.setText(user1.getEmail());
                txtName.setText(user1.getName());
                if(user1.getAvatar()!=null) Picasso.get().load(user1.getAvatar()).into(img_avatar);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void removeAddmin() {
        Menu menu = navigationView.getMenu();

        menu.removeItem(R.id.nav_addmin);
    }
}