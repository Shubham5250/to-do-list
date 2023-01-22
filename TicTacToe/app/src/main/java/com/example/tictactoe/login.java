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

public class login extends AppCompatActivity implements View.OnClickListener{
    Button loginbtn;
    TextView signuptxt;
    EditText inputemail, inputpassword;
    ProgressDialog progressDialog;
    String  emailpattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    private  static String PREFS_NAME = "MyPrefsFile";
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        progressDialog = new ProgressDialog(this);
        loginbtn = (Button) findViewById(R.id.loginbtn);
        loginbtn.setOnClickListener(this::onClick);
        inputemail = findViewById(R.id.inputEmail);
        signuptxt = findViewById(R.id.signuptxt);
        signuptxt.setOnClickListener(this::onClick);
        inputpassword = findViewById(R.id.inputpassword);
        
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();






    }



    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.signuptxt:
                startActivity(new Intent(this, createAccount.class));
                break;
            case R.id.loginbtn:

                userLogin();

        }
    }

    private void userLogin() {

        String email = inputemail.getText().toString();
        String password = inputpassword.getText().toString();


        if(email.isEmpty()){
            inputemail.setError("Email is required");
            inputemail.requestFocus();
            return;
        }
        if(password.isEmpty() || password.length()<6){
            inputpassword.setError("Password required with atleast 6 characters");
            return;
        }

        else {

            progressDialog.setMessage("Logging you in!");
            progressDialog.setTitle("Authorizing");
            progressDialog.setCanceledOnTouchOutside(true);
            progressDialog.show();
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        progressDialog.dismiss();

                        Toast.makeText(login.this, "Welcome back,champ!", Toast.LENGTH_SHORT).show();
                        sendUserToNextActivity();


                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(login.this, "Failed to login! Try again.", Toast.LENGTH_SHORT).show();


                    }
                }
            });
        }

    }
    private void sendUserToNextActivity() {

        Intent intent = new Intent(this,user_profile.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }
}