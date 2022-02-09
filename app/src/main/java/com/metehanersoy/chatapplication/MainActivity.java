package com.metehanersoy.chatapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    EditText emailText,passwordText;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();

        emailText = findViewById(R.id.user_email_edit_text);
        passwordText = findViewById(R.id.user_password_edit_text);


        FirebaseUser user = auth.getCurrentUser();
        if(user != null){
            Intent intent = new Intent(MainActivity.this,ChatActivity.class);
            startActivity(intent);
        }
    }

    public void signIn(View view){
        if(!emailText.getText().toString().isEmpty() && !passwordText.getText().toString().isEmpty()) {

            auth.signInWithEmailAndPassword(emailText.getText().toString(), passwordText.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {

                    Intent intent = new Intent(MainActivity.this,ChatActivity.class);
                    startActivity(intent);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }
    }
    public void signUp(View view){
        if(!emailText.getText().toString().isEmpty() && !passwordText.getText().toString().isEmpty()) {
            auth.createUserWithEmailAndPassword(emailText.getText().toString(), passwordText.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    Toast.makeText(getApplicationContext(), "Successfull", Toast.LENGTH_SHORT).show();

                    //Intent
                    Intent intent = new Intent(MainActivity.this,ChatActivity.class);
                    startActivity(intent);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(),e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                }
            });
        }
        else{
            Toast.makeText(getApplicationContext(), "Enter email and password", Toast.LENGTH_SHORT).show();
        }
    }


}