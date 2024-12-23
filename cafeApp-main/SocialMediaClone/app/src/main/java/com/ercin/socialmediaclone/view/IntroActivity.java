package com.ercin.socialmediaclone.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.ercin.socialmediaclone.R;
import com.ercin.socialmediaclone.databinding.ActivityIntroBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class IntroActivity extends AppCompatActivity {

    private static int splashTime = 2500;
    Animation topAnim, bottomAnim;
    ImageView imageTop, imageBottom;
    ActivityIntroBinding binding;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    String userUId;
    DocumentReference documentReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = ActivityIntroBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        firebaseAuth =  FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        imageTop = binding.efekt1;
        imageBottom = binding.efekt2;

        imageTop.setAnimation(topAnim);
        imageBottom.setAnimation(bottomAnim);


        if (firebaseUser != null) {
            userUId = firebaseAuth.getCurrentUser().getUid();
            documentReference = firebaseFirestore.collection("Users").document(userUId);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                String isAdmin = documentSnapshot.getString("is admin");
                                if (isAdmin != null && isAdmin.equals("1")) {
                                    Intent intent = new Intent(IntroActivity.this, MainAdminActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Intent intent = new Intent(IntroActivity.this, MainPageActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            } else {
                                // Kullanıcının belirli bir profil belgesi olmadığında yapılacak işlemler
                                // Örneğin, hata mesajı göstermek veya profil oluşturma ekranına yönlendirmek
                                Toast.makeText(IntroActivity.this, "Profil belgesi bulunamadı.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure( Exception e) {
                            // Belge alınamadığında yapılacak işlemler
                            Toast.makeText(IntroActivity.this, "Belge alınamadı: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }, splashTime);
        } else {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(IntroActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, splashTime);




        }

    }
}

