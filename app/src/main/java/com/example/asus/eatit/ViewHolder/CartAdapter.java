package com.example.asus.eatit.ViewHolder;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.asus.eatit.Cart;
import com.example.asus.eatit.Common.Common;
import com.example.asus.eatit.Database.Database;
import com.example.asus.eatit.Interface.ItemClickListener;
import com.example.asus.eatit.Model.Order;
import com.example.asus.eatit.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    , View.OnCreateContextMenuListener{

    public TextView txt_cart_name,txt_price;
    public ElegantNumberButton btn_quantity;

    private ItemClickListener itemClickListener;

    public void setTxt_cart_name(TextView txt_cart_name) {
        this.txt_cart_name = txt_cart_name;
    }

    public CartViewHolder(View itemView) {
        super(itemView);
        txt_cart_name = (TextView)itemView.findViewById(R.id.cart_item_name);
        txt_price = (TextView)itemView.findViewById(R.id.cart_item_price);
        btn_quantity = (ElegantNumberButton) itemView.findViewById(R.id.btn_quantity);

        itemView.setOnCreateContextMenuListener(this);
    }

    @Override
    public void onClick(View view) {

    }

    //Menampilkan pilihan Delete
    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        contextMenu.setHeaderTitle("Select Action");
        contextMenu.add(0,0,getAdapterPosition(), Common.DELETE);
    }
}

//Membuat Array List untuk list makanan yang dimasukkan ke Cart
public class CartAdapter extends RecyclerView.Adapter<CartViewHolder> {

    private List<Order> listData = new ArrayList<>();
    private Cart cart;

    public CartAdapter(List<Order> listData, Cart cart) {
        this.listData = listData;
        this.cart = cart;
    }

    //Convert XML ke Java Object
    @Override
    public CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(cart);
        View itemView = inflater.inflate(R.layout.cart_layout,parent,false);

        return new CartViewHolder(itemView);
    }

    //Untuk Update Cart (Elegant Number)
    @Override
    public void onBindViewHolder(CartViewHolder holder, final int position) {
        //TextDrawable drawable = TextDrawable.builder()
          //      .buildRound(""+listData.get(position).getQuantity(), Color.RED);
        //holder.img_cart_count.setImageDrawable(drawable);

        holder.btn_quantity.setNumber(listData.get(position).getQuantity());
        holder.btn_quantity.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                Order order = listData.get(position);
                order.setQuantity(String.valueOf(newValue));
                new Database(cart).updateCard(order);

                //Update txtTotal
                //Jumlahkan harga total
                int total = 0;
                List<Order> orders = new Database(cart).getCarts();
                for (Order item : orders)
                    total+=(Integer.parseInt(item.getPrice()))*(Integer.parseInt(item.getQuantity()));
                Locale locale = new Locale("en","US");
                NumberFormat fmt = NumberFormat.getCurrencyInstance();

                cart.txtTotalPrice.setText(fmt.format(total));
            }
        });

        Locale locale = new Locale("en","US");
        NumberFormat fmt = NumberFormat.getCurrencyInstance();
        int price = (Integer.parseInt(listData.get(position).getPrice()))*(Integer.parseInt(listData.get(position).getQuantity()));
        holder.txt_price.setText(fmt.format(price));

        holder.txt_cart_name.setText(listData.get(position).getProductName());
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }
}
