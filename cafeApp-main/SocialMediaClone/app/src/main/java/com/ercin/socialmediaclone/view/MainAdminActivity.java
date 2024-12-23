package com.ercin.socialmediaclone.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;

import com.ercin.socialmediaclone.databinding.ActivityMainAdminBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainAdminActivity extends AppCompatActivity {

    ActivityMainAdminBinding binding;
    String brancheFirst;
    String brancheSecond;
    FirebaseFirestore firebaseFirestore;
    DocumentReference documentReference;
    Map<String, Object> updates = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainAdminBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        firebaseFirestore = FirebaseFirestore.getInstance();
        documentReference = firebaseFirestore.collection("Status").document("eciZ2MeIMXTxAfqzXZL6");


        updates.put("1. magaza", brancheFirst);
        updates.put("2. magaza", brancheSecond);

        binding.FirstBranchStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    brancheFirst = "Açık";
                }else{
                    brancheFirst = "Kapalı";
                }
            }
        });

        binding.SecondBranchStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    brancheSecond = "Açık";
                }else{
                    brancheSecond = "Kapalı";
                }
            }
        });
    }

    public void updateBranchesStatus (View view){

        updates.put("1. magaza", brancheFirst);
        updates.put("2. magaza", brancheSecond);

        documentReference.update(updates).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                System.out.println("eklendi");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("eklenemedi");
            }
        });

    }

    public void goAddTypeActivity (View view){
        Intent intent = new Intent(MainAdminActivity.this, AddCoffeeTypeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    public void addNewProductBtn (View view){
        Intent intent = new Intent(MainAdminActivity.this, AddNewProductActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    public void goAddInnovationActivity (View view){

        try {
            Intent intent = new Intent(MainAdminActivity.this, AddNewInnovationActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }catch (Exception e){
            System.out.println("hata : " + e.getLocalizedMessage());
        }





    }
}