package com.example.otgshopping;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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
        TextView itemPrice = listView.findViewById(R.id.item_price);
        TextView itemQuantity = listView.findViewById(R.id.quantity_item_tv);
        ImageView itemImage = listView.findViewById(R.id.add_item_iv);
        final ImageView delItem = listView.findViewById(R.id.del_item_iv);
        delItem.setTag(position);
        delItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove(getItem(position));
                Log.d("check", Integer.toString(position));
            }
        });
        itemName.setText(desc.get(position).getItemName());
        itemPrice.setText(String.valueOf(desc.get(position).getItemPrice()));
        itemQuantity.setText(String.valueOf(desc.get(position).getItemQuantity()));
        return listView;
    }
}
