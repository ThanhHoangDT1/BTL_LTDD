package com.androidexam.cafeapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidexam.cafeapp.R;
import com.androidexam.cafeapp.activity.RegisterActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class LoginActivity extends AppCompatActivity {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://cafeapp-e03fc-default-rtdb.firebaseio.com/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText email = findViewById(R.id.inputEmail);
        final EditText password = findViewById(R.id.inputPassword);
        final Button login = findViewById(R.id.buttonSignIn);
        final TextView signup = findViewById(R.id.textCreateNewAccount);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String emailtxt = email.getText().toString();
                final String passwordtxt = password.getText().toString();


                if (emailtxt.isEmpty() || passwordtxt.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please enter your email or password", Toast.LENGTH_SHORT).show();
                } else {
                     databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                         @Override
                         public void onDataChange(@NonNull DataSnapshot snapshot) {
                             if(snapshot.hasChild(emailtxt)) {
                                 final String getPassword = snapshot.child(emailtxt).child("password").getValue(String.class);
                                 final String getRole = snapshot.child(emailtxt).child("role").getValue(String.class);

                                 if(getPassword.equals(passwordtxt)) {
                                     Toast.makeText(LoginActivity.this,"Successfully Logged in",Toast.LENGTH_SHORT).show();
                                     if(getRole.equals("employee")) {
                                         startActivity(new Intent(LoginActivity.this, EmployeeActivity.class));
                                     } else if (getRole.equals("manager")) {
                                         startActivity(new Intent(LoginActivity.this, ManagerActivity.class));
                                     }
                                     finish();
                                 } else {
                                     Toast.makeText(LoginActivity.this,"Wrong Password",Toast.LENGTH_SHORT).show();
                                 }
                             }
                             else {
                                 Toast.makeText(LoginActivity.this,"Wrong Password",Toast.LENGTH_SHORT).show();
                             }
                         }

                         @Override
                         public void onCancelled(@NonNull DatabaseError error) {

                         }
                     });
                }
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

    }
}



