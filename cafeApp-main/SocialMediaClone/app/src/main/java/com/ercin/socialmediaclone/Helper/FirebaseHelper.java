package com.ercin.socialmediaclone.Helper;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.ercin.socialmediaclone.model.Innovation;
import com.ercin.socialmediaclone.model.Product;
import com.ercin.socialmediaclone.model.User;
import com.ercin.socialmediaclone.view.MainAdminActivity;
import com.ercin.socialmediaclone.view.MainPageActivity;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.base.FinalizablePhantomReference;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class FirebaseHelper {

    private final FirebaseAuth firebaseAuth;
    private final FirebaseFirestore firebaseFirestore;
    private final FirebaseStorage firebaseStorage;

    public FirebaseHelper() {

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

    }

    public void signUp(User user, OnSignUpCompleteListener listener) {
        firebaseAuth.createUserWithEmailAndPassword(user.getEmail().toString(), user.getPassword().toString()).addOnCompleteListener(task -> {

            if (task.isSuccessful()) {

                FirebaseUser firebaseUser = task.getResult().getUser();
                String uid = firebaseUser.getUid();
                user.setUserUid(uid);
                Map<String, Object> userMap = new HashMap<>();
                userMap.put("userUid", user.getUserUid());
                userMap.put("name", user.getName());
                userMap.put("surname", user.getSurname());
                userMap.put("phone_number", user.getPhoneNumber());
                userMap.put("email", user.getEmail());
                userMap.put("signup_time", user.getSingUpTime());
                userMap.put("puan", user.getPoint());
                userMap.put("profile photo url", user.getProfilePhotoUrl());
                userMap.put("is admin", user.getIsAdmin());
                userMap.put("hediyeler", user.getHediyeler());


                firebaseFirestore.collection("Users").document(uid).set(userMap)

                        .addOnSuccessListener(documentReference -> {
                            listener.onSignUpComplete(true, null);
                        })
                        .addOnFailureListener(e -> {
                            listener.onSignUpComplete(false, e.getMessage());
                        });
            } else {
                listener.onSignUpComplete(false, Objects.requireNonNull(task.getException()).getMessage());
            }
        });
    }

    public interface OnSignUpCompleteListener {
        void onSignUpComplete(boolean success, String errorMessage);
    }

    public void saveProfilePhoto(Uri profilePhotoUri, String userUId) {
        Map<String, Object> data = new HashMap<>();
        data.put("profile photo url", profilePhotoUri.toString());
        firebaseFirestore.collection("Users").document(userUId).update(data);

        StorageReference storageReference = firebaseStorage.getReference().child("profilePhotos/" + UUID.randomUUID().toString());
        storageReference.putFile(profilePhotoUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                System.out.println("yükleme başarılı");

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("pp yükleme hatası" + e.getLocalizedMessage());
            }
        });
    }

    public void getProfilePhotoUrl(String uid, OnSuccessListener<String> successListener, OnFailureListener failureListener) {

        DocumentReference docRef = firebaseFirestore.collection("Users").document(uid);

        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    if (documentSnapshot.get("profile photo url") != "null") {
                        String photoUrl = documentSnapshot.getString("profile photo url");
                        successListener.onSuccess(photoUrl);
                    } else {
                        System.out.println("kullanıcı pp yüklememiş");
                    }
                } else {
                    failureListener.onFailure(new Exception("Belge mevcut değil"));
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                failureListener.onFailure(e);
            }
        });
    }

    public void addNewType(String typeName, OnSuccessListener<DocumentReference> successListener, OnFailureListener failureListener) {

        Map<String, Object> data = new HashMap<>();
        data.put("Ürün Türü", typeName);

        firebaseFirestore.collection("Type").add(data).addOnSuccessListener(successListener)
                .addOnFailureListener(failureListener);


    }

    public void getTypeList(OnSuccessListener<ArrayList<String>> successListener, OnFailureListener failureListener) {

        firebaseFirestore.collection("Type")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<String> types = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String type = document.getString("Ürün Türü");
                                if (type != null) {
                                    types.add(type);
                                }
                            }
                            successListener.onSuccess(types);
                        } else {
                            failureListener.onFailure(Objects.requireNonNull(task.getException()));
                        }
                    }
                });

    }

    public void addNewProduct(String name, String type, String photoUrl, Uri photoUri, String description) {


        DocumentReference documentReference = firebaseFirestore.collection("Products").document();


        Map<String, Object> data = new HashMap<>();
        data.put("ürün adı", name);
        data.put("ürün türü", type);
        data.put("foto url", photoUrl);
        data.put("ürün acıklaması", description);


        documentReference.set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

                uploadProductPhoto(documentReference.getId(), photoUri);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private void uploadProductPhoto(String documentId, Uri photoUri) {

        // Storage'a fotoğrafı yükleme
        String filename = documentId + ".jpg";
        StorageReference storageReference = firebaseStorage.getReference().child("ProductsPhotos/" + filename);
        storageReference.putFile(photoUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri downloadUri) {
                        firebaseFirestore.collection("Products").document(documentId).update("foto url", downloadUri.toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                System.out.println("başarılı");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                System.out.println("hata mesajı foto" + e.getLocalizedMessage());
                            }
                        });
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
    }

    public void getProducts(final OnSuccessListener<ArrayList<Product>> successListener, final OnFailureListener failureListener) {

        CollectionReference productsRef = firebaseFirestore.collection("Products");

        productsRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                ArrayList<Product> productList = new ArrayList<>();
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    String productName = document.getString("ürün adı");
                    String productType = document.getString("ürün türü");
                    String productUrl = document.getString("foto url");
                    String productDescription = document.getString("ürün acıklaması");
                    Product product = new Product(productName, productType, productUrl, productDescription);
                    productList.add(product);
                }
                successListener.onSuccess(productList);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                failureListener.onFailure(e);
            }
        });
    }

    public void addInnovation(Innovation innovation, Uri photoUri) {

        DocumentReference documentReference = firebaseFirestore.collection("Innovations").document();

        Map<String, Object> data = new HashMap<>();
        data.put("yenilik adi", innovation.getInnovationName());
        data.put("yenilik içeriği ", innovation.getInnovationContent());
        data.put("photo url", innovation.getInnovationPhotoUrl());
        data.put("ekleme tarihi", innovation.getSignUpTime());


        documentReference.set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                System.out.println("ljljhfgşjhdsfg");
                uploadInnovationPhoto(documentReference.getId().toString(), photoUri);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("hata başta" + e.getLocalizedMessage());
            }
        });
    }

    private void uploadInnovationPhoto (String documentId, Uri photoUri){

        String fileName = documentId + ".jpg";
        StorageReference storageReference = firebaseStorage.getReference().child("InnovationPhotos/" + fileName);
        storageReference.putFile(photoUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        firebaseFirestore.collection("Innovations").document(documentId).update("photo url", uri.toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                System.out.println("fotoğraf yüklendi her şey başarılı");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                System.out.println("hata burada 1" + e.getLocalizedMessage());
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("hata ikinci yerde " + e.getLocalizedMessage());
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //fotoğraf eklenemedi
                System.out.println("hata ilk yerde " + e.getLocalizedMessage());
            }
        });
    }

    public void getInnovations (final OnSuccessListener<ArrayList<Innovation>> successListener, final OnFailureListener onFailureListener){

        CollectionReference collectionReference = firebaseFirestore.collection("Innovations");
        collectionReference.orderBy("ekleme tarihi", Query.Direction.DESCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                ArrayList<Innovation> innovationList = new ArrayList<>();
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    String name = documentSnapshot.getString("yenilik adi");
                    String icerik = documentSnapshot.getString("yenilik içeriği ");
                    String url = documentSnapshot.getString("photo url");
                    Innovation innovation = new Innovation(name, url, icerik);
                    innovationList.add(innovation);
                }
                successListener.onSuccess(innovationList);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                onFailureListener.onFailure(e);
            }
        });
    }

    public void signIn(String email, String password, final Context context){

        CollectionReference collectionReference = firebaseFirestore.collection("Users");

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {

                String uId = authResult.getUser().getUid();
                DocumentReference documentReference = collectionReference.document(uId);
                documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        if (documentSnapshot.exists()){

                            String isAdmin = documentSnapshot.getString("is admin");
                            Intent intent;
                            if (isAdmin != null && isAdmin.equals("1")) {
                                intent = new Intent(context, MainAdminActivity.class);
                            } else {
                                intent = new Intent(context, MainPageActivity.class);
                            }

                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            context.startActivity(intent);
                        }else{
                            Log.d("firebasehelper", "dosya yok");
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("firebasehelper", "hata" + e.getLocalizedMessage());
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("FirebaseHelper", "hata2: " + e.getLocalizedMessage());
                Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void getName (String uid, OnSuccessListener<String> successListener, OnFailureListener failureListener){

        DocumentReference docRef = firebaseFirestore.collection("Users").document(uid);

        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    if (!documentSnapshot.get("name").equals("null") && documentSnapshot.get("surname") != null) {
                        String name = documentSnapshot.getString("name");
                        successListener.onSuccess(name);
                    } else {
                        System.out.println("kullanıcı pp yüklememiş");
                    }
                } else {
                    failureListener.onFailure(new Exception("Belge mevcut değil"));
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                failureListener.onFailure(e);
            }
        });
    }

    public void getQrCode (OnSuccessListener<String> successListener, OnFailureListener failureListener){

        DocumentReference docRef = firebaseFirestore.collection("QRCode").document("ixbP3U20aFeMU0CSDc6D");

        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    if (!documentSnapshot.get("qr code").equals("null")) {
                        String name = documentSnapshot.getString("qr code");
                        successListener.onSuccess(name);
                    } else {
                        System.out.println("qr yok");
                    }
                } else {
                    failureListener.onFailure(new Exception("Belge mevcut değil"));
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                failureListener.onFailure(e);
            }
        });
    }

    public void uploadGiftAmount (String userUid){

        DocumentReference documentReference = firebaseFirestore.collection("Users").document(userUid);

        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    String hediyeSayisi = documentSnapshot.getString("hediyeler");
                    int hediyeToplam = (hediyeSayisi != null) ? Integer.parseInt(hediyeSayisi) : 0;
                    hediyeToplam = hediyeToplam + 1;
                    documentReference.update("hediyeler", String.valueOf(hediyeToplam));
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    public void getGiftAmount(String userUid, OnSuccessListener<String> successListener, OnFailureListener failureListener){
        DocumentReference documentReference = firebaseFirestore.collection("Users").document(userUid);

        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    String giftAmount = documentSnapshot.getString("hediyeler");
                    successListener.onSuccess(giftAmount);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                failureListener.onFailure(new Exception("document is not exists"));
            }
        });
    }

    public void removeGiftAmount(String userUid, OnSuccessListener<String> successListener, OnFailureListener failureListener){

            DocumentReference documentReference = firebaseFirestore.collection("Users").document(userUid);

            documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {

                    String giftAmount = documentSnapshot.getString("hediyeler");
                    assert giftAmount != null;
                    int tempGiftAmoun = Integer.parseInt(giftAmount);
                    tempGiftAmoun = tempGiftAmoun - 1;
                    documentReference.update("hediyeler", String.valueOf(tempGiftAmoun));
                    successListener.onSuccess(String.valueOf(tempGiftAmoun));
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });

        }






}
