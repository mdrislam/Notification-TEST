package com.mristudio.notification_fcm.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mristudio.notification_fcm.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText email,password;
    private TextView registration;
    private Button login;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        registration = findViewById(R.id.registration);
        progressBar = findViewById(R.id.progress_bar);

        registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegistrationActivity.class));
                finish();
            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(TextUtils.isEmpty(email.getText().toString())){
                    Toast.makeText(LoginActivity.this, "Please Enter Email Address", Toast.LENGTH_SHORT).show();
                }
                else if(!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()){
                    Toast.makeText(LoginActivity.this, "Please Enter valid Email Address", Toast.LENGTH_SHORT).show();
                }
                else  if(TextUtils.isEmpty(password.getText().toString())){
                    Toast.makeText(LoginActivity.this, "Please Enter Password", Toast.LENGTH_SHORT).show();
                }
                else if(password.getText().toString().length()<6){
                    Toast.makeText(LoginActivity.this, "Please Enter 6 or more than digit password", Toast.LENGTH_SHORT).show();
                }
                else {
                    login();
                }


            }
        });
    }

    private void login() {
        progressBar.setVisibility(View.VISIBLE);
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }

                        // Get new Instance ID token
                        final String token = task.getResult().getToken();


                        FirebaseAuth.getInstance().signInWithEmailAndPassword(email.getText().toString(),password.getText().toString())
                                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {

                                        Map<String,Object> map = new HashMap<>();
                                        map.put("token",token);

                                        FirebaseDatabase.getInstance().getReference()
                                                .child("User")
                                                .child(FirebaseAuth.getInstance().getUid())
                                                .updateChildren(map)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {



                                                        FirebaseDatabase.getInstance().getReference()
                                                                .child("User")
                                                                .child(FirebaseAuth.getInstance().getUid())
                                                                .child("topic")
                                                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                                       if(dataSnapshot.exists()){
                                                                           int i = 0;

                                                                           for(DataSnapshot snapshot : dataSnapshot.getChildren()){

                                                                               FirebaseMessaging.getInstance().subscribeToTopic(snapshot.child("title").getValue().toString());

                                                                               i++;
                                                                               if(i==dataSnapshot.getChildrenCount()){
                                                                                   progressBar.setVisibility(View.GONE);
                                                                                   Toast.makeText(LoginActivity.this, "Login Successfull.", Toast.LENGTH_SHORT).show();
                                                                                   startActivity(new Intent(LoginActivity.this,DashboaredActivity.class));
                                                                                   finish();
                                                                               }

                                                                           }
                                                                       }
                                                                       else {
                                                                           progressBar.setVisibility(View.GONE);
                                                                           Toast.makeText(LoginActivity.this, "Login Successfull.", Toast.LENGTH_SHORT).show();
                                                                           startActivity(new Intent(LoginActivity.this,DashboaredActivity.class));
                                                                           finish();
                                                                       }

                                                                    }

                                                                    @Override
                                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                    }
                                                                });






                                                    }
                                                });


                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(LoginActivity.this, ""+e.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                    }
                });



    }
}
