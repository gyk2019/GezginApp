package com.example.gezginapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.gezginapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText etUserEmail, etUserPassword;
    Button registerButton, loginButton;
    private FirebaseAuth mAuth;
    String userEmail, userPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUserEmail = findViewById(R.id.et_user_email_login);
        etUserPassword = findViewById(R.id.et_user_password_login);
        registerButton = findViewById(R.id.button_go_register);
        loginButton = findViewById(R.id.button_login);
        mAuth = FirebaseAuth.getInstance();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userEmail = etUserEmail.getText().toString().trim();
                gotoRegister(userEmail);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userEmail = etUserEmail.getText().toString().trim();
                userPassword = etUserPassword.getText().toString().trim();
                
                if(!userEmail.isEmpty() && !userPassword.isEmpty()) {
                    login(userEmail, userPassword);
                } else {
                    Toast.makeText(getApplicationContext(), "E-posta ya da parola boş bırakılamaz!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void login(String userEmail, String userPassword) {
        mAuth.signInWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                    Log.d("login", "signInWithEmailAndPassword:successful");
                } else {
                    Log.w("login", "signInWithEmailAndPassword:failure");
                }
            }
        });
    }

    private void gotoRegister(String userEmail) {
        Intent registerIntent = new Intent(getApplicationContext(), RegisterActivity.class);
        startActivity(registerIntent);
    }

}
