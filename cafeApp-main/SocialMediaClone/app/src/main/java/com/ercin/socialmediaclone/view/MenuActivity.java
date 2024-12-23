
package com.ercin.socialmediaclone.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.proto.ProtoOutputStream;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ercin.socialmediaclone.Helper.FirebaseHelper;
import com.ercin.socialmediaclone.R;
import com.ercin.socialmediaclone.adapter.MenuChildAdapter;
import com.ercin.socialmediaclone.adapter.MenuParentAdapter;
import com.ercin.socialmediaclone.databinding.ActivityMenuBinding;
import com.ercin.socialmediaclone.model.Product;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends AppCompatActivity {

    ActivityMenuBinding binding;
    FirebaseHelper firebaseHelper;
    ArrayList<Product> productArrayList;
    MenuParentAdapter menuParentAdapter;
    List<String> typeList;
    MenuParentAdapter.OnProductClickListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMenuBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        firebaseHelper = new FirebaseHelper();
        productArrayList = new ArrayList<>();
        typeList = new ArrayList<>();
        firebaseHelper.getTypeList(new OnSuccessListener<ArrayList<String>>() {
            @Override
            public void onSuccess(ArrayList<String> strings) {
                typeList.clear();
                typeList.addAll(strings);
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("hata : " + e.getLocalizedMessage());
            }
        });

        firebaseHelper.getProducts(new OnSuccessListener<ArrayList<Product>>() {
            @Override
            public void onSuccess(ArrayList<Product> productArrayList) {

                binding.parentRecyclerView.setLayoutManager(new LinearLayoutManager(MenuActivity.this));
                menuParentAdapter = new MenuParentAdapter(productArrayList, typeList, MenuActivity.this, listener);
                binding.parentRecyclerView.setAdapter(menuParentAdapter);

            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                System.out.println("sonra hata " + e.getLocalizedMessage());

            }
        });

                listener = new MenuParentAdapter.OnProductClickListener() {
            @Override
            public void onProductClicked(Product product) {

                showPopup(product);

            }
        };


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottomMenu);

        bottomNavigationView.setOnItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.bottomHome:
                    startActivity(new Intent(getApplicationContext(), MainPageActivity.class));
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    finish();
                    return true;

                case R.id.bottomMenu:
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

    public void showPopup(Product product){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.menu_popup, null);

        ImageView productImageView = dialogView.findViewById(R.id.popupImageView);
        TextView productNameTextView = dialogView.findViewById(R.id.popupTittleTxtView);
        TextView productContentView = dialogView.findViewById(R.id.popupContentTextView);

        productNameTextView.setText(product.getProductName().toString());
        productContentView.setText(product.getProductDescription());

        Glide.with(this)
                .load(product.getProductPhotoUrl())
                .into(productImageView);

        builder.setView(dialogView);

        AlertDialog alertDialog = builder.create();

        Animation anim = AnimationUtils.loadAnimation(this, R.anim.popup_open_animation);
        dialogView.startAnimation(anim);

        alertDialog.show();



    }
}