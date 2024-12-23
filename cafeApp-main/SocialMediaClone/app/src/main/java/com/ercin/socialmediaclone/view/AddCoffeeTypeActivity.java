package com.ercin.socialmediaclone.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ercin.socialmediaclone.Helper.FirebaseHelper;
import com.ercin.socialmediaclone.databinding.ActivityAddCoffeeTypeBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;

public class AddCoffeeTypeActivity extends AppCompatActivity {

    ActivityAddCoffeeTypeBinding binding;
    FirebaseHelper firebaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddCoffeeTypeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
    }

    public void goBackBtn (View view){
        Intent intent = new Intent(AddCoffeeTypeActivity.this, MainAdminActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    public void addTypeBtn (View view){

        String türAdi = binding.coffeeTypeTxt.getText().toString();
        firebaseHelper = new FirebaseHelper();

        firebaseHelper.addNewType(türAdi, new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                binding.coffeeTypeTxt.setText("");
                System.out.println("yeni tür eklendi");
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("tür eklenirken hata oluştu : " + e);
            }
        });

    }

}