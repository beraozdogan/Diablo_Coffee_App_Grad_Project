package com.ercin.socialmediaclone.view;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ercin.socialmediaclone.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ercin.socialmediaclone.databinding.ActivityBranchesBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class BranchesActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityBranchesBinding binding;
    private ConstraintLayout subeLayout1, subeLayout2;
    FirebaseFirestore firebaseFirestore;
    DocumentReference documentReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityBranchesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottomSubeler);

        subeLayout1 = binding.sube1;
        subeLayout2 = binding.sube2;

        firebaseFirestore = FirebaseFirestore.getInstance();
        documentReference = firebaseFirestore.collection("Status").document("eciZ2MeIMXTxAfqzXZL6");


        documentReference.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            String first = documentSnapshot.getString("1. magaza");
                            String second = documentSnapshot.getString("2. magaza");
                            binding.firstBranchStatus.setText(first);
                            binding.secondBranchStatus.setText(second);
                        } else {
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });





        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        subeLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LatLng diablo1 = new LatLng(38.582808, 26.967207);
                mMap.addMarker(new MarkerOptions().position(diablo1).title("DİABLO"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(diablo1, 15));

            }
        });

        subeLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LatLng diablo2 = new LatLng(38.578760, 26.977520);
                mMap.addMarker(new MarkerOptions().position(diablo2).title("DİABLO"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(diablo2, 15));

            }
        });


        bottomNavigationView.setOnItemSelectedListener(item -> {

            switch (item.getItemId()){
                case R.id.bottomHome:
                    startActivity(new Intent(getApplicationContext(), MainPageActivity.class));
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    finish();
                    return true;

                case R.id.bottomMenu:
                    startActivity(new Intent(getApplicationContext(), MenuActivity.class));
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    finish();
                    return true;

                case R.id.bottomSubeler:
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

       LatLng izmir = new LatLng(38.582808,26.967207);
       mMap.addMarker(new MarkerOptions().position(izmir).title("SEYREK"));
       mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(izmir, 14));

    }

}