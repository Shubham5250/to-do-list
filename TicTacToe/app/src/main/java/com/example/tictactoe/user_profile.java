package com.example.tictactoe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class user_profile extends AppCompatActivity {

    private Button logoutbtn;
    private FirebaseUser user;
    private DatabaseReference databaseReference;
    private String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        logoutbtn = (Button) findViewById(R.id.logoutbt);

        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(user_profile.this, "Logged out successfully.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(user_profile.this, userLoginCreate_acc.class));
            }
        });

        user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("user");
        userID = user.getUid();


        final TextView fullNames = (TextView) findViewById(R.id.fullName);

        final TextView emails = (TextView) findViewById(R.id.userEmail);
        final TextView numbers = (TextView) findViewById(R.id.userNumber);


        databaseReference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user userProfile = snapshot.getValue(user.class);

                if(userProfile!= null){

                    String fullName = userProfile.fullName;
                    String email = userProfile.email;
                    String number = userProfile.number;

                    fullNames.setText(fullName);
                    emails.setText(email);
                    numbers.setText(number);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(user_profile.this, "Something wrong  ", Toast.LENGTH_SHORT).show();
            }
        });



    }
}