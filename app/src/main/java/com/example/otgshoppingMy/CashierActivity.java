package com.example.otgshoppingMy;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

public class CashierActivity extends AppCompatActivity {

    private Button scan;
    ArrayList<Orders> mOrders;
    private CartAdapter mCartAdapter;
    private  List<Cart> cartList = new ArrayList<>();
    RecyclerView recyclerView;
    TextView buyerName ;
    String buyerId;
    private static final String TAG = "CashierActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cashier);
        final Activity activity = this;
        scan = findViewById(R.id.scan);
        mOrders = new ArrayList<>();
        buyerName = findViewById(R.id.buyer_name);


       recyclerView = findViewById(R.id.recycler_view);
       mCartAdapter = new CartAdapter(cartList);

       RecyclerView.LayoutManager mLayoutManager =new LinearLayoutManager(getApplicationContext());
       recyclerView.setLayoutManager(mLayoutManager);
       recyclerView.setItemAnimator(new DefaultItemAnimator());
       recyclerView.setAdapter(mCartAdapter);


        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(activity);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setPrompt("Scan");
                integrator.setBeepEnabled(true);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(intentResult != null){
            if(intentResult.getContents() == null){
                Toast.makeText(this,"You Cancelled the scan",Toast.LENGTH_SHORT).show();
            }
            else {
                readFullInfo(intentResult.getContents());
            }
        }


        super.onActivityResult(requestCode, resultCode, data);
    }

    private void readFullInfo(String key) {
        Log.d(TAG, "readFullInfo: "+ key );
        final DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Orders");
        reference.child(key).child("list").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<items> myItems = new ArrayList<>();
                myItems.clear();
                for(DataSnapshot d : dataSnapshot.getChildren()){
                    items item = d.getValue(items.class);
                    Log.d(TAG, "read Full Info onDataChange: Entered here "+ item);
                    myItems.add(item);
                    Log.d(TAG, "read Full info onDataChange: "+ myItems);


                }
                for(items it  : myItems){
                    Log.d(TAG, "MyItems List onClick: Item Num ");
                    Log.d(TAG, "MyItems List onClick: Name "+ it.getItemName());
                    Log.d(TAG, "MyItems List onClick: price "+ it.getItemPrice());
                    Log.d(TAG, "MyItems List onClick: quantity "+ it.getItemQuantity());
                    Cart cart = new Cart(it.getItemName(),it.getItemPrice(),it.getItemQuantity());
                    cartList.add(cart);
                    mCartAdapter.notifyDataSetChanged();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        final String key_two = key.trim();
        final DatabaseReference referenceName = FirebaseDatabase.getInstance().getReference("Orders");
        referenceName.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    if(snapshot.getKey().equals(key_two)){
                        Log.d(TAG, "onDataChange: snapshot key "+ snapshot.getKey());
                        Log.d(TAG, "onDataChange: my key " + key_two);
                        Orders orders = snapshot.getValue(Orders.class);
                        mOrders.add(orders);
                        for(Orders o : mOrders){
                            Log.d(TAG, "Customer List onClick: Item Num ");
                            Log.d(TAG, "Customer List onClick: Buyer Name "+ o.getBuyer());
                            Log.d(TAG, "Customer List onClick: list  "+ o.getList());
                            buyerId = o.getBuyer();



                            //  Log.d(TAG, "Customer List onClick: quantity "+ it.getItemQuantity());
                        }

                    }
                }
                getBuyerName(buyerId);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void getBuyerName(String buyerId) {
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Users> users = new ArrayList<>();
                users.clear();
                for(DataSnapshot d : dataSnapshot.getChildren()){
                    Users users1 = d.getValue(Users.class);
                    users.add(users1);

                    for(Users u : users){
                        u.getUsername();
                        buyerName.setText(u.getUsername());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
