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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
    FirebaseAuth Auth;
    FirebaseDatabase database;
    ProgressDialog pd;
    EditText Name,Email,Phone,Password;
    TextView LOGIN,Gender;
    Button Signup;
    Spinner genders;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Name = findViewById(R.id.Name);
        Email = findViewById(R.id.Email);
        Phone = findViewById(R.id.Phone);
        LOGIN = findViewById(R.id.LOGIN);
        Gender = findViewById(R.id.Gender);
        Signup = findViewById(R.id.Signup);
        genders = findViewById(R.id.genders);
        Password = findViewById(R.id.Spassword);
        pd = new ProgressDialog(Signup.this);
        pd.setMessage("Signing You Up");
        Auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        final String[] Gender = new String[1];

        final List<String> list = new ArrayList<String>();
        list.add("Male");
        list.add("Female");
        list.add("Others");
        ArrayAdapter<String> adp1 = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, list);
        adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genders.setAdapter(adp1);

        genders.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
                Gender[0] = genders.getSelectedItem().toString();
//                Toast.makeText(getBaseContext(), list.get(position), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });
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
                                        user user = new user(Phone.getText().toString(),Gender[0]);
                                        database.getReference().child("Users").child(id).setValue(Name.getText().toString());
                                        database.getReference().child("Users").child(id).child(Name.getText().toString()).setValue(user);
                                        Intent intend = new Intent(Signup.this,Login.class);
                                        intend.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intend);
                                        Name.setText("");
                                        Email.setText("");
                                        Phone.setText("");
                                        Password.setText("");
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
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
}