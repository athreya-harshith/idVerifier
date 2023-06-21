package com.example.idverifier;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class loginActivity extends AppCompatActivity
{

    TextInputEditText userMail,userPassword;
    Button loginBtn;
    ProgressBar progressBar;

    //firebase auth instance

    FirebaseAuth auth;
    // testing on this

    private DatabaseReference database ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userMail = findViewById(R.id.userMail);
        userPassword = findViewById(R.id.userPassword);
        progressBar = findViewById(R.id.progressBar);
        loginBtn = findViewById(R.id.loginBtn);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                String emailId = userMail.getText().toString();
                String password = userPassword.getText().toString();
                if(TextUtils.isEmpty(emailId) || TextUtils.isEmpty(password))
                {
                    Toast.makeText(loginActivity.this,"Credentials cannot be empty",Toast.LENGTH_SHORT).show();
                }
                else
                    loginUser(emailId,password);
            }
        });

    }

    private void loginUser(String emailId, String password)
    {
        auth.signInWithEmailAndPassword(emailId,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(loginActivity.this,"Successfully Logged In"+emailId,Toast.LENGTH_SHORT).show();
                // here itself i need to redirect users according to their roles
//                if(TextUtils.equals(emailId,"firstadmin@gmail.com"))
//                {
////                    HashMap<String,Object> map = new HashMap<>();
////                    map.put("Name","FirstAdmin");
////                    map.put("Uid",auth.getCurrentUser().getUid());
//                    user add = new user("FirstAdmin",auth.getCurrentUser().getUid(),"firstadmin@gmail.com","Administrator");
//                    FirebaseDatabase.getInstance().getReference().child("Users").child("Administrator").child(auth.getCurrentUser().getUid()).setValue(add);
//                }

                //checking if an administrator
                FirebaseDatabase.getInstance().getReference().child("Users").child("Administrator").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot)
                    {
                        if(snapshot.hasChild(auth.getCurrentUser().getUid()))
                        {
                            String uid= auth.getCurrentUser().getUid();
                            Bundle bundle = getUserDataBundle(snapshot,uid);
                            Intent intent = new Intent(loginActivity.this,admin.class);
                            intent.putExtras(bundle);
                            intent.putExtra("Role","Administrator");
                            startActivity(intent);
                            finish();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


                // checking if a Security
                FirebaseDatabase.getInstance().getReference().child("Users").child("Security").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.hasChild(auth.getCurrentUser().getUid()))
                        {
                            String uid= auth.getCurrentUser().getUid();
                            Bundle bundle = getUserDataBundle(snapshot,uid);
                            Intent intent = new Intent(loginActivity.this,security.class);
                            intent.putExtras(bundle);
                            intent.putExtra("Role","Security");
                            startActivity(intent);
                            finish();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                FirebaseDatabase.getInstance().getReference().child("Users").child("Student").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.hasChild(auth.getCurrentUser().getUid()))
                        {
                            String uid= auth.getCurrentUser().getUid();
                            Bundle bundle = getUserDataBundle(snapshot,uid);
                            Intent intent = new Intent(loginActivity.this,student.class);
                            intent.putExtras(bundle);
                            intent.putExtra("Role","Student");
                            startActivity(intent);
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(loginActivity.this,"Unable to find the user re check your credentials",Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    protected void onStart() {

        super.onStart();

        if(FirebaseAuth.getInstance().getCurrentUser() != null)
        {
            //checking if an administrator
            FirebaseDatabase.getInstance().getReference().child("Users").child("Administrator").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot)
                {
                    if(snapshot.hasChild(auth.getCurrentUser().getUid()))
                    {
                        String uid= auth.getCurrentUser().getUid();
                        Bundle bundle = getUserDataBundle(snapshot,uid);
                        Intent intent = new Intent(loginActivity.this,admin.class);
                        intent.putExtras(bundle);
                        intent.putExtra("Role","Administrator");
                        startActivity(intent);
                        finish();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


            // checking if a Security
            FirebaseDatabase.getInstance().getReference().child("Users").child("Security").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.hasChild(auth.getCurrentUser().getUid()))
                    {
                        String uid= auth.getCurrentUser().getUid();
                        Bundle bundle = getUserDataBundle(snapshot,uid);
                        Intent intent = new Intent(loginActivity.this,security.class);
                        intent.putExtras(bundle);
                        intent.putExtra("Role","Security");
                        startActivity(intent);
                        finish();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            FirebaseDatabase.getInstance().getReference().child("Users").child("Student").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.hasChild(auth.getCurrentUser().getUid()))
                    {
                        String uid= auth.getCurrentUser().getUid();
                        Bundle bundle = getUserDataBundle(snapshot,uid);
                        Intent intent = new Intent(loginActivity.this,student.class);
                        intent.putExtras(bundle);
                        intent.putExtra("Role","Student");
                        startActivity(intent);
                        finish();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    public Bundle getUserDataBundle(DataSnapshot snapshot,String uid)
    {
        Bundle bundle = new Bundle();
        bundle.putString("name",snapshot.child(uid).child("name").getValue(String.class));
        bundle.putString("email",snapshot.child(uid).child("email").getValue(String.class));
        bundle.putString("id",snapshot.child(uid).child("id").getValue(String.class));
        bundle.putString("profilePic",snapshot.child(uid).child("profilePic").getValue(String.class));
        bundle.putString("mobile",snapshot.child(uid).child("mobile").getValue(String.class));
        bundle.putString("uid",snapshot.child(uid).child("uid").getValue(String.class));
        bundle.putString("branch",snapshot.child(uid).child("branch").getValue(String.class));

        return bundle;
    }
}