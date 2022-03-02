package com.divax.chatfree;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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

public class Login extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseDatabase database;
    ProgressDialog pd;

    EditText ID,password;
    TextView SIGNup;
    Button Login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ID = findViewById(R.id.ID);
        password = findViewById(R.id.LPassword);
        Login = findViewById(R.id.Login);
        SIGNup = findViewById(R.id.SIGNup);
        pd = new ProgressDialog(Login.this);
        pd.setMessage("Logging You In");
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
            Intent i = new Intent(Login.this, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        } else {
        }

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ID.getText().toString().equals("") || password.getText().toString().equals("")){
                    AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
                    view.startAnimation(buttonClick);
                    Animation shake = AnimationUtils.loadAnimation(Login.this, R.anim.shake);
                    view.startAnimation(shake);
                    Toast.makeText(Login.this, "Something Found Empty", Toast.LENGTH_SHORT).show();
                }else{
                    pd.show();

                    auth.signInWithEmailAndPassword(ID.getText().toString(),password.getText().toString()).
                            addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if(task.isSuccessful()){

                                        Intent i = new Intent(Login.this, MainActivity.class);
                                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(i);
                                        ID.setText("");
                                        password.setText("");
                                        Toast.makeText(Login.this, "Login successful", Toast.LENGTH_SHORT).show();
                                        pd.dismiss();

                                    }else{
                                        AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
                                        view.startAnimation(buttonClick);
                                        Animation shake = AnimationUtils.loadAnimation(Login.this, R.anim.shake);
                                        view.startAnimation(shake);
                                        Toast.makeText(Login.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        pd.dismiss();
                                    }
                                }
                            });

                }}

        });
        SIGNup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this,Signup.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
}