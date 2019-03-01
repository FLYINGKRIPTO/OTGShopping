package com.example.otgshoppingMy;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.ArrayList;
import java.util.HashMap;

public class ShopActivity extends AppCompatActivity {

    ImageView addCart , payBill,add,subtract,delete ;
    ListView itemList ;
    ArrayList<items> list ;
    ArrayList<items> listTwo ;
    String uniqueKey;
    int totalBill = 0 ;
    private static final String TAG = "ShopActivity";
    ArrayList<Products> mProducts;
    ArrayList<Orders> mOrders;
    ArrayList<Products> custProducts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        itemList = (ListView)findViewById(R.id.item_list) ;
        list = new ArrayList<items>() ;
        listTwo = new ArrayList<items>();
        final ItemListAdapter itemListAdapter = new ItemListAdapter(this,list) ;
        itemList.setAdapter(itemListAdapter);






      //  list.add(new list("Parle G","$5.00","1")) ;
        addCart = findViewById(R.id.add_cart) ;
        addCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(ShopActivity.this,BarcodeScanner.class),0);
            }
        });
        mProducts = new ArrayList<>();
        custProducts = new ArrayList<>();
        mOrders = new ArrayList<>();


        payBill = findViewById(R.id.pay_bill) ;
        payBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog_final_pay = new Dialog(ShopActivity.this);
                dialog_final_pay.setContentView(R.layout.final_pay_dialog);
                Button finalCancelButton = dialog_final_pay.findViewById(R.id.final_cancel_btn) ;
                Button finalPayButton = dialog_final_pay.findViewById(R.id.final_pay_btn);
                finalCancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog_final_pay.dismiss();
                    }
                });
                dialog_final_pay.show();

                for(items it  : list){
                    Log.d(TAG, "onClick: Item Num ");
                    Log.d(TAG, "onClick: Name "+ it.getItemName());
                    Log.d(TAG, "onClick: price "+ it.getItemPrice());
                    Log.d(TAG, "onClick: quantity "+ it.getItemQuantity());
                }
                finalPayButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

                        HashMap<String , Object> hashMap = new HashMap<>();
                        hashMap.put("list",list);
                        hashMap.put("buyer", FirebaseAuth.getInstance().getCurrentUser().getUid());
                        reference.child("Orders").push().setValue(hashMap, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                               uniqueKey = databaseReference.getKey();
                                Log.d(TAG, "onComplete: Unique key "+ uniqueKey);

                                generateQRCode(uniqueKey);
                            }
                        });
                    }
                });
            }
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 0){
            if(resultCode == CommonStatusCodes.SUCCESS){
                if(data !=null){

                    Barcode barcode = data.getParcelableExtra("barcode");
                    Toast.makeText(ShopActivity.this,"Barcode Value is:"+barcode.displayValue, Toast.LENGTH_LONG).show();
                    Long barcodeValue = Long.parseLong(barcode.displayValue);
                    checkIfBarcodeIsInDatabase(barcodeValue);

                }else{
                    Toast.makeText(ShopActivity.this,"No Barcode", Toast.LENGTH_LONG).show();
                }

            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void checkIfBarcodeIsInDatabase(final Long barcodeValue) {
        Log.d(TAG, "checkIfBarcodeIsInDatabase:  bar code value "+ barcodeValue );
        final DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Products");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int flag=0;
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Products products = snapshot.getValue(Products.class);
                    mProducts.add(products);


                        if(barcodeValue.equals(products.getBarcode())){
                            Log.d(TAG, "onDataChange: equal " +barcodeValue);
                            Toast.makeText(ShopActivity.this,"Valid Barcode",Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "onDataChange: valid barcode ");
                            final ItemListAdapter itemListAdapter = new ItemListAdapter(getApplicationContext(),list) ;
                            itemList.setAdapter(itemListAdapter);
                            list.add(new items(products.getItem(),products.getPrice(),1L)) ;
                            flag = 1;
                            for(items it  : list){
                                Log.d(TAG, "Customer List onClick: Item Num ");
                                Log.d(TAG, "Customer List onClick: Name "+ it.getItemName());
                                Log.d(TAG, "Customer List onClick: price "+ it.getItemPrice());
                                Log.d(TAG, "Customer List onClick: quantity "+ it.getItemQuantity());
                            }
                        }




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
    private void generateQRCode(String key) {
        final Dialog QRCodeDialog  = new Dialog(ShopActivity.this);
        QRCodeDialog.setContentView(R.layout.qr_code_dialog);
        ImageView qrImage = QRCodeDialog.findViewById(R.id.imageQR);
        QRCodeDialog.show();
        Log.d(TAG, "generateQRCode: unik key "+ key );
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        String key_two = key.trim();
        Log.d(TAG, "generateQRCode: unik key two "+ key_two);

        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(key_two, BarcodeFormat.QR_CODE,200,200);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix );
            qrImage.setImageBitmap(bitmap);
        //    readFullInfo(key_two);
         //   readInfo(key_two);


        } catch (WriterException e) {
            e.printStackTrace();
        }


    }

    private void readInfo(final String key_two) {
        final DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Orders");
        reference.addValueEventListener(new ValueEventListener() {
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


                          //  Log.d(TAG, "Customer List onClick: quantity "+ it.getItemQuantity());
                        }


                        for(items it  : listTwo){
                            Log.d(TAG, "Customer List onClick: Item Num ");
                            Log.d(TAG, "Customer List onClick: Name "+ it.getItemName());
                            Log.d(TAG, "Customer List onClick: price "+ it.getItemPrice());
                            Log.d(TAG, "Customer List onClick: quantity "+ it.getItemQuantity());
                        }




                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void readFullInfo(String key){
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
                    for(items it  : myItems){
                        Log.d(TAG, "MyItems List onClick: Item Num ");
                        Log.d(TAG, "MyItems List onClick: Name "+ it.getItemName());
                        Log.d(TAG, "MyItems List onClick: price "+ it.getItemPrice());
                        Log.d(TAG, "MyItems List onClick: quantity "+ it.getItemQuantity());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
