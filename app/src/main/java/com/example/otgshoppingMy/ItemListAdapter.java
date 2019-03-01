package com.example.otgshoppingMy;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ItemListAdapter extends ArrayAdapter<items> {

    public final Context context;
    private final ArrayList<items> desc;

    public ItemListAdapter(Context context, ArrayList<items> desc) {
        super(context, R.layout.list_layout, desc);
        this.context = context;
        this.desc = desc;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View listView = layoutInflater.inflate(R.layout.list_layout, parent, false);
        final TextView itemName = listView.findViewById(R.id.item_name);
        final TextView itemPrice = listView.findViewById(R.id.item_price);
        final TextView itemQuantity = listView.findViewById(R.id.quantity_item_tv);
        ImageView add = listView.findViewById(R.id.add_item_iv);
        ImageView subtract = listView.findViewById(R.id.rem_item_iv);
        final ImageView delItem = listView.findViewById(R.id.del_item_iv);
        delItem.setTag(position);
        delItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove(getItem(position));
                Log.d("check", Integer.toString(position));
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int item = Integer.parseInt(String.valueOf(itemQuantity.getText()));
                item++;
                itemQuantity.setText(item+"");

            }
        });
        subtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int item = Integer.parseInt(String.valueOf(itemQuantity.getText()));
                if(item>=2){
                    item--;
                    itemQuantity.setText(item+"");

                }
                else {
                    Toast.makeText(getContext(),"Cant have 0 list",Toast.LENGTH_SHORT).show();
                }


            }
        });
        itemName.setText(desc.get(position).getItemName());
        itemPrice.setText(" ₹  : "+String.valueOf(desc.get(position).getItemPrice()));
        itemQuantity.setText(String.valueOf(desc.get(position).getItemQuantity()));
        return listView;
    }
}
