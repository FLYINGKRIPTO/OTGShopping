package com.example.otgshoppingMy;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainScreen extends AppCompatActivity {
    Button MainBtn ;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    List<Products> mProducts;
    private static final String TAG = "MainScreen";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        MainBtn = findViewById(R.id.btn_main) ;
        MainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainScreen.this,ShopActivity.class));
          mProducts = new ArrayList<>();
                if (ContextCompat.checkSelfPermission(MainScreen.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainScreen.this, new String[]{Manifest.permission.CAMERA}, 101);
                }
       updateDatabase();
       readAllProducts();
            }
        });
    }


    private void updateDatabase() {
      DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        HashMap<String , Object> hashMap = new HashMap<>();


        reference.child("Products").push().setValue(hashMap);
    }
    private void readAllProducts() {

        Log.d(TAG, "readAllProducts: ");
       final DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Products");
       reference.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
              for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                  Products products = snapshot.getValue(Products.class);
                  mProducts.add(products);
                  Log.d(TAG, "onDataChange: Barcode "+ products.getBarcode());
                  Log.d(TAG, "onDataChange: Name "+ products.getItem());
                  Log.d(TAG, "onDataChange: Price "+ products.getPrice());
              }



           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       });

    }

}
