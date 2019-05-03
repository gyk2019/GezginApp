package com.example.gezginapp.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.gezginapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class AddPhotoActivity extends AppCompatActivity {

    private static final int IMAGE_REQUEST = 111;
    FirebaseAuth mAuth;
    FirebaseStorage firebaseStorage;
    ImageView ivUserSavedPhoto;
    Button btnSelectPhoto;
    Button btnSavePhoto;
    Uri filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_photo);

        mAuth = FirebaseAuth.getInstance();
        firebaseStorage =FirebaseStorage.getInstance();

        ivUserSavedPhoto = (ImageView)findViewById(R.id.iv_user_saved_photo);
        btnSelectPhoto = (Button)findViewById(R.id.btn_select_photo);
        btnSavePhoto = (Button)findViewById(R.id.btn_save_photo);

        showPhoto();
        
        btnSelectPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPhoto();
            }
        });
        btnSavePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePhoto();
            }
        });
    }

    // Firebase storage'da daha önceden ekli resim varsa Picasso kütüphanesiyle ekranda gösterilir.
    private void showPhoto() {
        StorageReference storageReference = firebaseStorage.getReference().child("userProfilePhoto");
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(AddPhotoActivity.this).load(uri).fit().centerCrop().into(ivUserSavedPhoto);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddPhotoActivity.this, "Fotoğraf yükleme işlemi başarısız.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Ekrandan resim/fotoğraf yükle butonuna basıldığında resim seçilebilecek aktivite açıldı.
    private void selectPhoto() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Resim Seçiniz"), IMAGE_REQUEST);
    }

    // Seçilen resim dosyası, selectPhoto metodundan sonra çalıştı ve resim ekranda gösterildi. Henüz firebase storage'a kaydedilmedi.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Picasso.with(AddPhotoActivity.this).load(filePath).centerCrop().into(ivUserSavedPhoto);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Seçilen resim firebase storage'a kaydedildi.
    private void savePhoto() {
        if(filePath != null) {
            StorageReference storageReference = firebaseStorage.getReference();
            storageReference.child("userProfilePhoto").putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(AddPhotoActivity.this, "Fotoğraf başarılı bir şekilde kaydedildi.", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddPhotoActivity.this, "Fotoğraf kaydedilemedi.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}