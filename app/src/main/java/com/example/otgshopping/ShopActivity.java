package com.example.otgshopping;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ShopActivity extends AppCompatActivity {

    ImageView addCart , payBill ;
    ListView itemList ;
    ArrayList<items> list ;
    int totalBill = 0 ;
    private static final String TAG = "ShopActivity";
    List<Products> mProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        itemList = (ListView)findViewById(R.id.item_list) ;
        list = new ArrayList<items>() ;
        final ItemListAdapter itemListAdapter = new ItemListAdapter(this,list) ;
        itemList.setAdapter(itemListAdapter);
      //  list.add(new items("Parle G","$5.00","1")) ;
        addCart = findViewById(R.id.add_cart) ;
        addCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(ShopActivity.this,BarcodeScanner.class),0);
            }
        });
        mProducts = new ArrayList<>();

        payBill = findViewById(R.id.pay_bill) ;
        payBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog_final_pay = new Dialog(ShopActivity.this);
                dialog_final_pay.setContentView(R.layout.final_pay_dialog);
                Button finalCancelButton = dialog_final_pay.findViewById(R.id.final_cancel_btn) ;
                finalCancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog_final_pay.dismiss();
                    }
                });
                dialog_final_pay.show();

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
                        }





                 /*
                    Log.d(TAG, "onDataChange: Barcode "+ products.getBarcode());
                    Log.d(TAG, "onDataChange: Name "+ products.getItem());
                    Log.d(TAG, "onDataChange: Price "+ products.getPrice());*/
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
