package com.example.gezginapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.gezginapp.R;
import com.example.gezginapp.models.PostModel;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AddPostActivity extends AppCompatActivity {

    private static final int IMAGE_REQUEST = 111;
    ImageView ivPostPicture;
    Button btnAddPostPhoto;
    Button btnSavePost;
    EditText etPostName;
    EditText etPostDescription;
    Uri filePath;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        databaseReference = firebaseDatabase.getReference().child("GezecegimYerler");
        storageReference = firebaseStorage.getReference().child("GezecegimYerler");

        ivPostPicture = (ImageView)findViewById(R.id.iv_post_picture);
        btnAddPostPhoto = (Button)findViewById(R.id.btn_add_post_photo);
        btnSavePost = (Button)findViewById(R.id.btn_save_post);
        etPostName = (EditText) findViewById(R.id.et_post_name);
        etPostDescription = (EditText) findViewById(R.id.et_post_description);

        btnAddPostPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPhoto();
            }
        });
        btnSavePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePost();
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

    // Eklenmek istenen post firebase veritabanına, resim ise firebase storage'a kaydedilir.
    private void savePost() {
        String postId = databaseReference.push().getKey();
        uploadImage(postId);
    }

    // Seçilen resim dosyası, selectPhoto metodundan sonra çalıştı ve resim ekranda gösterildi. Henüz firebase storage'a kaydedilmedi.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filePath = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                ivPostPicture.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Resim dosyasını firebase storage'a yükledikten sonra download linkini oluşturduk ve postModel'i firebase veritabanına kaydettik.
    private void uploadImage(final String postId) {

        if(filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            final StorageReference ref = storageReference.child(postId);
            UploadTask uploadTask = ref.putFile(filePath);

            //add file on Firebase and got Download Link
            ref.putFile(filePath).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()){
                        throw task.getException();
                    }
                    return ref.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()){
                        Uri downUri = task.getResult();
                        addPostModel(downUri, postId);
                    }
                }
            });
        }
    }

    // postModel firebase veritabanına kaydedildi.
    private void addPostModel(Uri downUri, String postId) {
        PostModel postModel = new PostModel();
        postModel.setPostName(etPostName.getText().toString());
        postModel.setPostDescription(etPostDescription.getText().toString());
        postModel.setPostId(postId);
        postModel.setPostUrl(String.valueOf(downUri));
        databaseReference.child(postId).setValue(postModel);

        Intent intent= new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
        finish();
    }
}
