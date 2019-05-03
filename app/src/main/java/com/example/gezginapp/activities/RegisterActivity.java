package com.example.gezginapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.gezginapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    EditText etUserEmail, etUserPassword, etUserPasswordConfirm;
    Button registerButton;
    private FirebaseAuth mAuth;
    String userEmail, userPassword, userPasswordConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etUserEmail = findViewById(R.id.et_user_email_register);
        etUserPassword = findViewById(R.id.et_user_password_register);
        etUserPasswordConfirm = findViewById(R.id.et_user_password_register_confirm);
        registerButton = findViewById(R.id.button_register);
        mAuth = FirebaseAuth.getInstance();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userEmail = etUserEmail.getText().toString().trim();
                userPassword = etUserPassword.getText().toString().trim();
                userPasswordConfirm = etUserPasswordConfirm.getText().toString().trim();

                if(!userEmail.isEmpty() && !userPassword.isEmpty() && !userPasswordConfirm.isEmpty()) {
                    register(userEmail, userPassword, userPasswordConfirm);
                } else {
                    Toast.makeText(getApplicationContext(), "Kaydolmak için tüm alanları doldurunuz!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void register(String userEmail, String userPassword, String userPasswordConfirm) {
        mAuth.createUserWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    FirebaseUser firebaseUser = mAuth.getCurrentUser();
                    Toast.makeText(getApplicationContext(), "Kayıt işlemi başarıyla tamamlandı", Toast.LENGTH_SHORT).show();
                    Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(loginIntent);
                }
            }
        }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof FirebaseAuthException) {

                    boolean isErrorWeakPassword = ((FirebaseAuthException) e).getErrorCode().equals("ERROR_WEAK_PASSWORD");
                    boolean isErrorInvalidEmail = ((FirebaseAuthException) e).getErrorCode().equals("ERROR_INVALID_EMAIL");
                    boolean isErrorEmailAlreadyInUse = ((FirebaseAuthException) e).getErrorCode().equals("ERROR_EMAIL_ALREADY_IN_USE");

                    if(isErrorWeakPassword) {
                        Toast.makeText(getApplicationContext(), "Eksik ya da hatalı şifre", Toast.LENGTH_SHORT).show();
                    } else if (isErrorInvalidEmail) {
                        Toast.makeText(getApplicationContext(), "Geçersiz e-posta adresi", Toast.LENGTH_SHORT).show();
                    } else if (isErrorEmailAlreadyInUse) {
                        Toast.makeText(getApplicationContext(), "E-posta adresi zaten kayıtlı", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}