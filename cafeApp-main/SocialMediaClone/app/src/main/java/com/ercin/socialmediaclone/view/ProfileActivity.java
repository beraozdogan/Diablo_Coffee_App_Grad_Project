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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ercin.socialmediaclone.R;
import com.ercin.socialmediaclone.Helper.FirebaseHelper;
import com.ercin.socialmediaclone.databinding.ActivityProfileBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.ktx.Firebase;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.security.spec.ECField;

public class ProfileActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;
    String userUid;
    ActivityProfileBinding binding;
    FirebaseHelper firebaseHelper;
    private static final int REQUEST_CODE_PICK_IMAGE = 101;
    IntentResult result;
    int puan;
    int sonPuan;
    int giftAmount = 0;
    boolean qrScannerForPoint = false;
    boolean qrScannerForGift = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        firebaseHelper = new FirebaseHelper();


        PuanGuncelle();



        firebaseHelper.getProfilePhotoUrl(userUid, new OnSuccessListener<String>() {
            @Override
            public void onSuccess(String photoUrl) {

                if (!"null".equals(photoUrl))
                {

                    Glide.with(ProfileActivity.this)
                            .load(photoUrl)
                            .into(binding.profilePhoto);
                }else{

                    Toast.makeText(getApplicationContext(), "Profil Fotoğrafı Ekleyebilirsiniz", Toast.LENGTH_LONG).show();

                }
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("hata" + e.getLocalizedMessage());
            }
        });


        firebaseHelper.getGiftAmount(userUid, new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String s) {

                            binding.userGifts.setText(s);
                            giftAmount = Integer.parseInt(s);

                    }
                }, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });


                BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottomProfil);

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
                    startActivity(new Intent(getApplicationContext(), BranchesActivity.class));
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    finish();
                    return true;

                case R.id.bottomProfil:
                    return true;
            }
            return false;
        });
    }

    public void btnLogOut (View view){

        firebaseAuth.signOut();
        Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();

    }

    private void PuanGuncelle(){

        DocumentReference documentReference = firebaseFirestore.collection("Users").document(userUid);

        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    String puanString = documentSnapshot.getString("puan");
                    binding.ownStar.setText(puanString);

                    int ownStar = Integer.parseInt((String) binding.ownStar.getText());

                    if (ownStar == 50){
                        binding.tebriklerTxtView.setVisibility(View.VISIBLE);
                        System.out.println(binding.tebriklerTxtView.getText());
                    }else{
                        binding.tebriklerTxtView.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });
    }

    public void addProfilePhotoBtn (View view){

        if (ContextCompat.checkSelfPermission(ProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Snackbar.make(findViewById(android.R.id.content), "Galeriye erişim izni gerekiyor.", Snackbar.LENGTH_LONG)
                    .setAction("İzin Ver", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ActivityCompat.requestPermissions(ProfileActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_PICK_IMAGE);
                        }
                    })
                    .show();
        } else {
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
            Uri photoUri = data.getData();
            String userId = firebaseUser.getUid().toString();
            firebaseHelper.saveProfilePhoto(photoUri, userId);
        }else{
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

            if (result != null){
                if (result.getContents() != null){
                    if (qrScannerForPoint){
                        firebaseHelper.getQrCode(new OnSuccessListener<String>() {
                            @Override
                            public void onSuccess(String s) {
                                final String qrCode = s.toString();

                                    String qrResult = result.getContents();

                                    if (qrResult != null && qrResult.equals(qrCode)){
                                        puanEkle();
                                        Toast.makeText(ProfileActivity.this, "Tebrikler Puanınız Başarıyla Eklendi", Toast.LENGTH_SHORT).show();
                                        qrScannerForPoint = false;
                                    }
                            }
                        }, new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
                    }else if (qrScannerForGift){

                        firebaseHelper.removeGiftAmount(userUid, new OnSuccessListener<String>() {
                            @Override
                            public void onSuccess(String s) {
                                binding.userGifts.setText(s);
                                Toast.makeText(ProfileActivity.this,"Afiyet olsun", Toast.LENGTH_LONG).show();
                                qrScannerForGift = false;
                            }
                        }, new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
                    }
                }
            }
        }
    }

    private void puanEkle(){

        DocumentReference documentReference = firebaseFirestore.collection("Users").document(userUid);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String puanString = documentSnapshot.getString("puan");

                   puan = (puanString != null) ? Integer.parseInt(puanString) : 0;

                    if (puan == 45) {
                        puan = 0;
                        documentReference.update("puan", String.valueOf(puan));
                        sonPuan = puan;
                        binding.tebriklerTxtView.setVisibility(View.VISIBLE);

                        try {
                            firebaseHelper.uploadGiftAmount(userUid);
                            giftAmount++;
                            binding.userGifts.setText(String.valueOf(giftAmount));
                        }catch (Exception e){
                            System.out.println("hata  bu sekilde : " + e.getLocalizedMessage());
                        }


                    }else {
                        puan += 5;
                        documentReference.update("puan", String.valueOf(puan));
                        sonPuan = puan;
                    }
                    binding.ownStar.setText(String.valueOf(sonPuan));
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }

    public void openQrScanner(View view){

        qrScannerForPoint = true;

        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setPrompt("Lütfen Qr kodu okutunuz");
        intentIntegrator.setOrientationLocked(false);
        intentIntegrator.initiateScan();


    }
    public void hediyeHarcaBtn (View view){

        qrScannerForGift = true;

        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setPrompt("Lütfen Qr kodu okutunuz");
        intentIntegrator.setOrientationLocked(false);
        intentIntegrator.initiateScan();
    }

}