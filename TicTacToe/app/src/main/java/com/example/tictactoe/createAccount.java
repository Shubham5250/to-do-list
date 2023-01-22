package com.example.tictactoe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class createAccount extends AppCompatActivity implements View.OnClickListener {
    EditText inputEmail,inputPassword,inputNumber,inputName;
    Button signupbtn;
    private static String PREF_NAME = "MyPrefsFile";
    TextView loginTextbtn;
    String  emailpattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    ProgressDialog progressDialog;

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        loginTextbtn = findViewById(R.id.loginTextbtn);

        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputpassword);
        inputName = findViewById(R.id.inputName);
        inputNumber = findViewById(R.id.inputNumber);

        signupbtn = findViewById(R.id.signupbtn);
        signupbtn.setOnClickListener(this::onClick);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

//        signupbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                register_user();
//
//
//            }
//        });


    }

    public void login(View view){
        Intent intent = new Intent(this,login.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.loginTextbtn:
                startActivity(new Intent(this,games.class));
                break;

            case R.id.signupbtn:
//                SharedPreferences sharedPreferences = getSharedPreferences(createAccount.PREF_NAME,0);
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//
//                editor.putBoolean("hasRegistered",true);
//                editor.apply();
//                startActivity(new Intent(createAccount.this,user_profile.class));
//                finish();
                register_user();
                break;
        }
    }

    private void register_user() {
        String name = inputName.getText().toString().trim();
        String number = inputNumber.getText().toString().trim();
        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();

        if(name.isEmpty()){
            inputName.setError("Name is required");
            inputName.requestFocus();
            return;
        }
        if(number.isEmpty()){
            inputNumber.setError("Number is required");
            inputNumber.requestFocus();
            return;
        }
        if(number.length()<10){
            inputNumber.setError("Enter the 10 digit mobile number");
            inputNumber.requestFocus();
            return;
        }
        if(!email.matches(emailpattern)){
            inputEmail.setError("Enter the correct email");
            inputEmail.requestFocus();
            return;

        }

        if(password.isEmpty() || password.length()<8) {
            inputPassword.setError("Please enter the password with atleast 8 characters");
            inputPassword.requestFocus();
            return;

        }

            progressDialog.setMessage("Please wait while we register you!");
            progressDialog.setTitle("Authorizing");
            progressDialog.setCanceledOnTouchOutside(true);
            progressDialog.show();
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                user user = new user(name, email, number);

                                FirebaseDatabase.getInstance().getReference("user")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    progressDialog.dismiss();
                                                    startActivity(new Intent(createAccount.this,user_profile.class));
                                                    Toast.makeText(createAccount.this, "Registration successful..", Toast.LENGTH_SHORT).show();
                                                }
                                                else{
                                                    progressDialog.dismiss();
                                                    Toast.makeText(createAccount.this, "Failed to register! Try again.", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                            }
                            else{
                                progressDialog.dismiss();
                                Toast.makeText(createAccount.this, "Failed to register! Try again.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

    }
}