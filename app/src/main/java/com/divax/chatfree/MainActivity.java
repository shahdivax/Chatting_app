package com.divax.chatfree;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {
    FusedLocationProviderClient fusedLocationProviderClient;
    FirebaseDatabase Database;
    FirebaseAuth Auth;
    DatabaseReference databaseReference;
    ListView lv;
    TextView messages, LocationNew;
    ArrayList<String> arrayList = new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;
    LocationRequest locationRequest;
    ImageView SendM, LocationSymbol, List_views;
    Spinner SignOut;
    String Locationold;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Initialize Different Views

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        messages = findViewById(R.id.message);
        LocationNew = findViewById(R.id.Locations);
        SendM = findViewById(R.id.SendM);
        LocationSymbol = findViewById(R.id.LocationSymbol);
        SignOut = findViewById(R.id.SignOut);
        Database = FirebaseDatabase.getInstance();
        Auth = FirebaseAuth.getInstance();
        String id = Auth.getCurrentUser().getUid();
        lv = findViewById(R.id.listview);
        arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, arrayList);
        lv.setAdapter(arrayAdapter);
        LocationNew.setText("Global");
        Locationold = "Global";
        initializeListView();


        //Spinner for SignOut

        final String[] Gender = new String[1];
        final List<String> list = new ArrayList<String>();
        list.add("Menu");
        list.add("Global Chat");
        list.add("SignOut");
        ArrayAdapter<String> adp1 = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, list);
        adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SignOut.setAdapter(adp1);
        SignOut.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
                Gender[0] = SignOut.getSelectedItem().toString();
                if (!list.get(position).equals("")) {
                    Gender[0] = list.get(position);
                    if (Gender[0].equals("SignOut")) {
                        FirebaseAuth.getInstance().signOut();
                        Intent i = new Intent(MainActivity.this, Login.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                    } else if (Gender[0].equals("Global Chat") && !LocationNew.getText().toString().equals("Global")) {
                        Intent i = new Intent(MainActivity.this, MainActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        Toast.makeText(getBaseContext(), "Global Chat", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getBaseContext(), "Global Chat", Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });


        //ListView Item Clicker

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                builder.setCancelable(false);
                builder.setTitle("Caution");
                builder.setMessage("What u want to do ?\nOnce done can not be UNDONE.!");

                builder.setNegativeButton("Report", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(MainActivity.this, "User Reported!", Toast.LENGTH_SHORT).show();
                    }
                });

                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Database.getReference().child(LocationNew.getText().toString()).child(id).removeValue();
                        Toast.makeText(MainActivity.this, "Message Deleted", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.show();
            }
        });


        //Location Icon Click
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);
        LocationSymbol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getlocation();
            }
        });


        //Send Icon Click

        SendM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Locationold.equals(LocationNew.getText().toString())) {
                    Database.getReference().child(Locationold).child(id).removeValue();
                    Locationold = LocationNew.getText().toString();
                    arrayAdapter.clear();
                    Database.getReference().child(LocationNew.getText().toString()).child(id).removeValue();
                    arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, arrayList);
                    lv.setAdapter(arrayAdapter);
                    initializeListView();
                }

                if (messages.getText().toString().equals("")) {
                    Toast.makeText(MainActivity.this, "Can't Send Empty Message", Toast.LENGTH_SHORT).show();
                } else {
                    Database.getReference().child(LocationNew.getText().toString()).child(id).setValue(LocationNew.getText().toString() + ">  " + messages.getText().toString());
                    messages.setText("");
                }
            }
        });


    }

    private void getlocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if (location != null) {

                    try {
                        Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                        List<Address> addresses = geocoder.getFromLocation(
                                location.getLatitude(), location.getLongitude(), 1
                        );
                        LocationNew.setText(addresses.get(0).getLocality());
                        Toast.makeText(MainActivity.this, "Location Set to : " + LocationNew.getText().toString(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
         }



    // initializeListView Method

    private void initializeListView() {
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.listview, arrayList);
        databaseReference = FirebaseDatabase.getInstance().getReference().child(LocationNew.getText().toString());
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                arrayList.add(snapshot.getValue(String.class));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                arrayList.add(snapshot.getValue(String.class));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                arrayList.remove(snapshot.getValue(String.class));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                arrayList.remove(snapshot.getValue(String.class));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        lv.setAdapter(adapter);
    }

}