package com.nextgen.qfree.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nextgen.qfree.GlobalPreference;
import com.nextgen.qfree.LoginActivity;
import com.nextgen.qfree.ModelClass.OffersModelClass;
import com.nextgen.qfree.OfferActivity;
import com.nextgen.qfree.ProductDetailsActivity;
import com.nextgen.qfree.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class OffersListAdapter extends RecyclerView.Adapter<OffersListAdapter.MyViewHolder> {

    ArrayList<OffersModelClass> oList;
    Context context;
    String ip,uid;

    public OffersListAdapter(ArrayList<OffersModelClass> list, Context context) {
        this.oList = list;
        this.context = context;

        GlobalPreference globalPreference = new GlobalPreference(context);
        ip = globalPreference.getIp();
        uid = globalPreference.getID();
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.raw_offer_list,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        OffersModelClass offerList = oList.get(position);

        holder.productNameTV.setText(offerList.getProductName());
        holder.priceTV.setText("Price:"+offerList.getPrice());
        holder.priceTV.setPaintFlags(holder.priceTV.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.offerPriceTV.setText("Offer Price:"+offerList.getOfferPrice());

        holder.offerCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creating a popup menu

                PopupMenu popupMenu = new PopupMenu(context,holder.optionsTV);

                popupMenu.inflate(R.menu.options_menu);

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.cart_menu1:

                                addCart(offerList.getId(),offerList.getProductName(),offerList.getOfferPrice());


                        }
                        return false;
                    }
                });
                popupMenu.show();
               // Toast.makeText(context, "Added to cart", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return oList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView productNameTV;
        TextView priceTV;
        TextView offerPriceTV;
        TextView optionsTV;
        CardView offerCV;

        public MyViewHolder(@NonNull View itemview) {
            super(itemview);

            productNameTV = itemview.findViewById(R.id.productNameTextView);
            priceTV = itemview.findViewById(R.id.priceTextView);
            offerPriceTV = itemview.findViewById(R.id.offerPriceTextView);
            optionsTV = itemview.findViewById(R.id.optionsTextView);
            offerCV = itemview.findViewById(R.id.offersCardView);

        }
    }


    ////////////////////////////////////////////// Add to Cart /////////////////////////////////////////////////////

    private void addCart(String id, String productName, String offerPrice) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://"+ ip +"/Qless/api/addCart.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(context, "Successfully Added to Cart", Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, ""+error, Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            @Nullable
            protected Map<String,String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("uid",uid);
                params.put("pid",id);
                params.put("pName",productName);
                params.put("quantity","1");
                params.put("price",offerPrice);
                return  params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        requestQueue.add(stringRequest);
    }
}
