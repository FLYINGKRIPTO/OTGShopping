package com.example.otgshoppingMy;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {

   public List<Cart> cartList ;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View itemView  = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.cart_list_row,viewGroup,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

      Cart cart = cartList.get(i);
      myViewHolder.productName.setText(cart.getItem());
      myViewHolder.productPrice.setText(String.valueOf(cart.getPrice()));
      myViewHolder.productQuantity.setText(String.valueOf(cart.getQuantity()));
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

   public TextView productName;
   public TextView productPrice;
   public TextView productQuantity;
       public MyViewHolder(@NonNull View itemView) {
           super(itemView);
           productName = itemView.findViewById(R.id.product_name);
           productPrice = itemView.findViewById(R.id.product_price);
           productQuantity = itemView.findViewById(R.id.product_quantity);
       }
   }
    public CartAdapter(List<Cart> cartList){
           this.cartList = cartList;
    }
}
