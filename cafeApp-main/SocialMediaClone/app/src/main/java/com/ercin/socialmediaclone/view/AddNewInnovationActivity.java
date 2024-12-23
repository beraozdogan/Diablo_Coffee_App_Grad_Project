


package com.ercin.socialmediaclone.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;

import com.ercin.socialmediaclone.Helper.FirebaseHelper;
import com.ercin.socialmediaclone.R;
import com.ercin.socialmediaclone.databinding.ActivityAddNewInnovationBinding;
import com.ercin.socialmediaclone.model.Innovation;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;

import java.util.Date;

public class AddNewInnovationActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_PICK_IMAGE = 1;
    ActivityAddNewInnovationBinding binding;
    Uri photoUri;
    FirebaseHelper firebaseHelper = new FirebaseHelper();
    Innovation innovation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddNewInnovationBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        //yenilik fotoğrafını al ve firebasye gönder bunu zaten ürün ekleme yerinde yapmıştık aynısını yapcaz bidaha

    }

    public void selectImage (View view){

        if (ContextCompat.checkSelfPermission(AddNewInnovationActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            Snackbar.make(findViewById(android.R.id.content), "Galeriye erişim izni gerekiyor", Snackbar.LENGTH_LONG).setAction("İzin ver", new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ActivityCompat.requestPermissions(AddNewInnovationActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_PICK_IMAGE);

                }
            }).show();
        }else{
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PICK_IMAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
            } else {
                Snackbar.make(findViewById(android.R.id.content), "Galeriye erişim izni reddedildi.", Snackbar.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            photoUri = data.getData();
        }
    }

    public void innovationEkle (View view){

        Innovation innovation = new Innovation(binding.innovationNameTxt.getText().toString(), "null", binding.innovationContentTxt.getText().toString(), new Timestamp(new Date()));
        System.out.println("uri + " + photoUri.toString());
        firebaseHelper.addInnovation(innovation, photoUri);

    }

    public void geriDon (View view){

        Intent intent = new Intent(AddNewInnovationActivity.this, MainAdminActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();

    }


}