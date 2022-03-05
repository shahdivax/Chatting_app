package com.divax.chatfree;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Signup extends AppCompatActivity {
    Animation topanim;
    FirebaseAuth Auth;
    FirebaseDatabase database;
    ProgressDialog pd;
    TextInputEditText Name,Email,Phone,Password;
    TextView LOGIN,SignTEXT;
    Button Signup;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Name = findViewById(R.id.Name);
        Email = findViewById(R.id.Email);
        Phone = findViewById(R.id.Phone);
        LOGIN = findViewById(R.id.LOGIN);
        Signup = findViewById(R.id.Signup);
        Password = findViewById(R.id.Spassword);
        imageView = findViewById(R.id.SignI);
        SignTEXT = findViewById(R.id.SignTEXT);
        pd = new ProgressDialog(Signup.this);
        pd.setMessage("Signing You Up");
        Auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        topanim = AnimationUtils.loadAnimation(Signup.this,R.anim.top_anim);
        imageView.setAnimation(topanim);


        Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Name.getText().toString().equals("") || Email.getText().toString().equals("") || Phone.getText().toString().equals("") || Password.getText().toString().equals("")){
                    AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
                    view.startAnimation(buttonClick);
                    Animation shake = AnimationUtils.loadAnimation(Signup.this, R.anim.shake);
                    view.startAnimation(shake);
                    Toast.makeText(Signup.this, "Something Found Empty", Toast.LENGTH_SHORT).show();
                }else{
                    pd.show();
                    Auth.createUserWithEmailAndPassword(Email.getText().toString(),Password.getText().toString()).
                            addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (task.isSuccessful()) {
                                        String id = task.getResult().getUser().getUid();
                                        user user = new user(Phone.getText().toString());
                                        database.getReference().child("Users").child(id).setValue(Name.getText().toString());
                                        database.getReference().child("Users").child(id).child(Name.getText().toString()).setValue(user);
                                        Intent intend = new Intent(Signup.this,Login.class);
                                        intend.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intend);
                                        pd.dismiss();
                                    }else{
                                        AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
                                        view.startAnimation(buttonClick);
                                        Animation shake = AnimationUtils.loadAnimation(Signup.this, R.anim.shake);
                                        view.startAnimation(shake);

                                        Toast.makeText(Signup.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        pd.dismiss();
                                    }
                                }
                            });
                }}
        });
        LOGIN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Signup.this,Login.class);
                startActivity(intent);
                finish();
            }
        });
    }
}