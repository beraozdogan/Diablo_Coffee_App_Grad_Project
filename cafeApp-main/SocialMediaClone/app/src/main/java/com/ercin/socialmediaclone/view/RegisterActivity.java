package com.ercin.socialmediaclone.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.ercin.socialmediaclone.Helper.FirebaseHelper;
import com.ercin.socialmediaclone.databinding.ActivityRegisterBinding;
import com.ercin.socialmediaclone.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RegisterActivity extends AppCompatActivity {

    ActivityRegisterBinding binding;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser;
    User user;
    String userUid;
    FirebaseHelper firebaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

    }

    public void signUp(View view){

        if (!binding.inputPassword.getText().toString().equals(binding.inputPasswordAgain.getText().toString()))
        {

            Toast.makeText(getApplicationContext(), "şifre onaylanmadı", Toast.LENGTH_LONG).show();


        }else if (binding.inputPassword.getText().toString() == "" || binding.inputPasswordAgain.getText().toString() == "" || binding.inputEmail.getText().toString() == "" || binding.inputName.getText().toString() == "" || binding.inputSurname.getText().toString() == "" || binding.inputPhoneNumber.getText().toString() == ""){


            System.out.println("buraya geldi2");

            Toast.makeText(getApplicationContext(), "boş alan bırakmayınız", Toast.LENGTH_LONG).show();
            binding.inputPassword.setText("");
            binding.inputPasswordAgain.setText("");

        }else{


            firebaseHelper = new FirebaseHelper();
            user = new User();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
            Date date = new Date();

            user.setName(binding.inputName.getText().toString());
            user.setSurname(binding.inputSurname.getText().toString());
            user.setPassword(binding.inputPassword.getText().toString());
            user.setEmail(binding.inputEmail.getText().toString());
            user.setPhoneNumber(binding.inputPhoneNumber.getText().toString());
            user.setSingUpTime(simpleDateFormat.format(date));
            user.setPoint("0");
            user.setProfilePhotoUrl("null");
            user.setIsAdmin("0");
            user.setHediyeler("0");

            firebaseHelper.signUp(user, new FirebaseHelper.OnSignUpCompleteListener() {
                @Override
                public void onSignUpComplete(boolean success, String errorMessage) {
                    if (success){
                        System.out.println("kayıt başarılı");
                        Intent intent = new Intent(RegisterActivity.this, MainPageActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();

                    }else{

                        System.out.println("kayıt başarısız : " + errorMessage);

                    }
                }
            });

        }

    }

    public void haveAccount (View view){

        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(intent);

    }

}