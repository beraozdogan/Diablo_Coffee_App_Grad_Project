package com.ercin.socialmediaclone.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.ercin.socialmediaclone.Helper.FirebaseHelper;
import com.ercin.socialmediaclone.R;
import com.ercin.socialmediaclone.adapter.MainPageAdapter;
import com.ercin.socialmediaclone.databinding.ActivityMainPageBinding;
import com.ercin.socialmediaclone.model.Innovation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;


public class MainPageActivity extends AppCompatActivity {

    ActivityMainPageBinding binding;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;
    String userUid;
    FirebaseHelper firebaseHelper;
    MainPageAdapter mainPageAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainPageBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        firebaseHelper = new FirebaseHelper();




        firebaseHelper.getName(userUid.toString(), new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        binding.customerName.setText("Seni görmek güzel " + s.toString());
                    }
                }, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println(e.getLocalizedMessage());
                    }
                });

        firebaseHelper.getProfilePhotoUrl(firebaseUser.getUid().toString(), new OnSuccessListener<String>() {
            @Override
            public void onSuccess(String photoUrl) {

                if (!"null".equals(photoUrl))
                {
                    Glide.with(MainPageActivity.this)
                            .load(photoUrl)
                            .into(binding.profilePhoto);
                }
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("hata" + e.getLocalizedMessage());
            }
        });


                firebaseHelper.getInnovations(new OnSuccessListener<ArrayList<Innovation>>() {
                    @Override
                    public void onSuccess(ArrayList<Innovation> innovationArrayList) {

                        binding.mainRecyclerView.setLayoutManager(new LinearLayoutManager(MainPageActivity.this));
                        mainPageAdapter = new MainPageAdapter(innovationArrayList, MainPageActivity.this);
                        binding.mainRecyclerView.setAdapter(mainPageAdapter);

                    }
                }, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("son hata : " + e.getLocalizedMessage());
                    }
                });


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottomHome);

        bottomNavigationView.setOnItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.bottomHome:
                    return true;

                case R.id.bottomMenu:
                    startActivity(new Intent(getApplicationContext(), MenuActivity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;

                case R.id.bottomSubeler:
                    startActivity(new Intent(getApplicationContext(), BranchesActivity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;

                case R.id.bottomProfil:
                    startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
            }
            return false;
        });
    }
}