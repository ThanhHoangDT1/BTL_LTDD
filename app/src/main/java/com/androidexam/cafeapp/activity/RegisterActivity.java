package com.androidexam.cafeapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidexam.cafeapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class RegisterActivity extends AppCompatActivity {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://cafeapp-e03fc-default-rtdb.firebaseio.com/");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText name = findViewById(R.id.inputName);
        final EditText email = findViewById(R.id.inputEmail);
        final EditText password = findViewById(R.id.inputPassword);
        final  EditText conpassword = findViewById(R.id.inputConfirmPassword);

        final Button signupbtn = findViewById(R.id.buttonSignUp);
        final TextView signin = findViewById(R.id.textSignIn);

        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String nametxt = name.getText().toString();
                final String  emailtxt = email.getText().toString();
                final String passwordtxt = password.getText().toString();
                final String conpasswordtxt = conpassword.getText().toString();

                if(nametxt.isEmpty() || emailtxt.isEmpty() || passwordtxt.isEmpty()) {
                    Toast.makeText(RegisterActivity.this,"Please fill all fields",Toast.LENGTH_SHORT).show();
                }
                else if(!passwordtxt.equals(conpasswordtxt)) {
                    Toast.makeText(RegisterActivity.this,"Password are not matching",Toast.LENGTH_SHORT).show();
                }
                else {
                    databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.hasChild(emailtxt)) {
                                Toast.makeText(RegisterActivity.this,"Email is already registered",Toast.LENGTH_SHORT).show();
                            }
                            else {
                                databaseReference.child("users").child(emailtxt).child("email").setValue(emailtxt);
                                databaseReference.child("users").child(emailtxt).child("name").setValue(nametxt);
                                databaseReference.child("users").child(emailtxt).child("password").setValue(passwordtxt);

                                Toast.makeText(RegisterActivity.this,"User register successfully",Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(RegisterActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}