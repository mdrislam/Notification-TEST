package com.mristudio.notification_fcm.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.myhexaville.smartimagepicker.ImagePicker;
import com.myhexaville.smartimagepicker.OnImagePickedListener;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegistrationActivity extends AppCompatActivity {

    private EditText name,email,password;
    private TextView login;
    private ImageView back;
    private Button registration;
    private CircleImageView imageView;
    private ProgressBar progressBar;

    final int PERMISSION_ALL = 100;

    private ImagePicker imagePicker;

    private Uri uri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        registration = findViewById(R.id.register);
        imageView = findViewById(R.id.image);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        name = findViewById(R.id.name);
        login = findViewById(R.id.login);
        back = findViewById(R.id.back);

        progressBar = findViewById(R.id.progress_bar);


        final String[] PERMISSIONS = {
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.CAMERA
        };

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!hasPermissions(RegistrationActivity.this, PERMISSIONS)) {
                    ActivityCompat.requestPermissions(RegistrationActivity.this, PERMISSIONS, PERMISSION_ALL);
                }
                else {
                    imagePicker.choosePicture(true /*show camera intents*/);
                }
            }
        });

        imagePicker = new ImagePicker(this, /* activity non null*/
                null, /* fragment nullable*/
                new OnImagePickedListener() {
                    @Override
                    public void onImagePicked(Uri imageUri) {
                        UCrop.of(imageUri, getTempUri())
                                .withAspectRatio(1, 1)
                                .start(RegistrationActivity.this);
                    }
                });


        registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(email.getText().toString())){
                    Toast.makeText(RegistrationActivity.this, "Please Enter Email Address", Toast.LENGTH_SHORT).show();
                }
                else if(!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()){
                    Toast.makeText(RegistrationActivity.this, "Please Enter valid Email Address", Toast.LENGTH_SHORT).show();
                }
                else  if(TextUtils.isEmpty(password.getText().toString())){
                    Toast.makeText(RegistrationActivity.this, "Please Enter Password", Toast.LENGTH_SHORT).show();
                }
                else if(password.getText().toString().length()<6){
                    Toast.makeText(RegistrationActivity.this, "Please Enter 6 or more than digit password", Toast.LENGTH_SHORT).show();
                }
                else  if(TextUtils.isEmpty(name.getText().toString())){
                    Toast.makeText(RegistrationActivity.this, "Please Enter Name", Toast.LENGTH_SHORT).show();
                }
                else if(uri == null){
                    Toast.makeText(RegistrationActivity.this, "Select Image", Toast.LENGTH_SHORT).show();
                }
                else {
                    register();
                }
            }
        });

    }

    private void register() {


        progressBar.setVisibility(View.VISIBLE);
        FirebaseAuth.getInstance().
                createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString())
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        upload();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegistrationActivity.this, "Failed.", Toast.LENGTH_SHORT).show();
                    }
                });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegistrationActivity.this,LoginActivity.class));
                finish();
            }
        });
    }

    void upload(){
        final StorageReference riversRef = FirebaseStorage.getInstance().getReference().child("Temp/"+System.currentTimeMillis()+".png");
        riversRef.putFile(uri).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                progressBar.setVisibility(View.GONE);
                Log.i("dscgjdshv", "onFailure: "+exception.getMessage());
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        insertUserInfo(uri);
                    }
                });

            }
        });
    }

    private void insertUserInfo(final Uri uri) {

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("sdfkjbvdf", "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        HashMap<String,Object> map = new HashMap<>();
                        map.put("email",email.getText().toString());
                        map.put("name",name.getText().toString());
                        map.put("password",password.getText().toString());
                        map.put("image",uri.toString());
                        map.put("token",token);

                        FirebaseDatabase.getInstance().getReference()
                                .child("User")
                                .child(FirebaseAuth.getInstance().getUid())
                                .setValue(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(RegistrationActivity.this, "Registration successful.", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(RegistrationActivity.this,DashboaredActivity.class));
                                        finish();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressBar.setVisibility(View.GONE);
                                        Log.i("dscgjdshv", "onFailure: "+e.getMessage());
                                    }
                                });

                    }
                });


    }


    private Uri getTempUri(){
        String dri = Environment.getExternalStorageDirectory()+ File.separator+"Temp";
        File dirFile = new File(dri);
        dirFile.mkdir();

        String file = dri+File.separator+"temp.png";
        File tempFile = new File(file);
        try {
            tempFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Uri.fromFile(tempFile);
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissionsList[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_ALL:{
                if (grantResults.length > 0) {
                    boolean b = true;
                    for (String per : permissionsList) {
                        if(grantResults[0] == PackageManager.PERMISSION_DENIED){
                           b=false;

                        }

                    }
                    if(b){
                        imagePicker.choosePicture(true /*show camera intents*/);
                    }

                }
                return;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imagePicker.handleActivityResult(resultCode,requestCode, data);

        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            uri = UCrop.getOutput(data);
            imageView.setImageURI(null);
            imageView.setImageURI(uri);

        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
            Log.i("dsjknjsdkn", "onActivityResult: "+cropError.getMessage());
        }
    }

}
