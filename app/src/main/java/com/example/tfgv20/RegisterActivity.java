package com.example.tfgv20;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private EditText txtEmail, txtPass, txtConfim, txtUser;
    private Button btnRegister;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();


        txtUser = findViewById(R.id.textUserRegister);
        txtEmail = findViewById(R.id.textEmailRegister);
        txtPass = findViewById(R.id.textPassRegister);
        txtConfim = findViewById(R.id.textConfRegister);
        btnRegister = findViewById(R.id.btnRegRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtEmail.getText().toString().isEmpty() || txtPass.getText().toString().isEmpty() || txtConfim.getText().toString().isEmpty()){
                    Toast.makeText(RegisterActivity.this, "Campos requeridos", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (txtPass.getText().toString().equals(txtConfim.getText().toString())){
                        String email = txtEmail.getText().toString();
                        String password = txtPass.getText().toString();

                        doRegister(email, password);
                    }
                    else {
                        Toast.makeText(RegisterActivity.this, "La contraseña no coincide", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void doRegister(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("REGISTRO", "createUserWithEmail:success");

                            String username = txtUser.getText().toString();

                            FirebaseUser user = mAuth.getCurrentUser();
                            String userID = mAuth.getCurrentUser().getUid();
                            mDatabase.child("users").child(userID).child("firstName").setValue(username);

                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("REGISTRO", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser) {
        if (currentUser != null){
            startActivity(new Intent(this, MainActivity.class));
        }
    }
}
