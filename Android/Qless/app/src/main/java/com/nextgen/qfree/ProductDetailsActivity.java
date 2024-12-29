package com.nextgen.qfree;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProductDetailsActivity extends AppCompatActivity {

    private final String TAG="ProductDetailsActivity";

    TextView pNameTV;
    TextView pPriceTV;
    TextView pQuantityTV;
    Button addCartBT;
    ImageView addIV;
    ImageView subIV;

    private GlobalPreference globalPreference;
    private String ip,uid;

    private String pName,pPrice,pid;

    private int quantity = 1;
    private int price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        globalPreference = new GlobalPreference(this);
        ip = globalPreference.getIp();
        uid = globalPreference.getID();

        Intent intent = getIntent();
        String response = intent.getStringExtra("response");
        int pos = intent.getIntExtra("pos",0);

        Log.d(TAG,"productDetails:"+pos);

        iniit();
        loadData(response,pos);

        addCartBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkQuantity();
            }
        });
    }

    private void checkQuantity() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://" + ip +"/Qless/api/checkQuantity.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("success")) {

                   addCart();

                } else {
                    Toast.makeText(ProductDetailsActivity.this, "Insufficient Item", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ProductDetailsActivity.this, ""+error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("pid",pid);
                params.put("quantity",String.valueOf(quantity));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }

    //Adding the Scanned product to cart

    private void addCart() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://" + ip +"/Qless/api/addCart.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("success")) {
                    Toast.makeText(ProductDetailsActivity.this, "Successfully Added to Cart", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(ProductDetailsActivity.this,HomeActivity.class);
                    startActivity(intent);

                } else {
                    Toast.makeText(ProductDetailsActivity.this, "Failed to Add Item", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ProductDetailsActivity.this, ""+error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("uid",uid);
                params.put("pid",pid);
                params.put("pName",pName);
                params.put("quantity",String.valueOf(quantity));
                params.put("price",String.valueOf(price));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    // Loading the data Fetched Using QR From Database

    private void loadData(String response, int pos) {
        try{
            JSONObject jsonObject = new JSONObject(response);
            JSONArray array = jsonObject.getJSONArray("data");

            JSONObject object = array.getJSONObject(pos);
            pid = object.getString("id");
            pName = object.getString("pname");
            pPrice = object.getString("price");

            pNameTV.setText(pName);
            pPriceTV.setText(pPrice);
            pQuantityTV.setText(String.valueOf(quantity));

            price = Integer.valueOf(pPrice);

            addIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (quantity < 20) {

                        quantity = quantity + 1;
                        pQuantityTV.setText(String.valueOf(quantity));

                        int amount = Integer.valueOf(pPrice);

                        price = quantity * amount;

                       // Toast.makeText(ProductDetailsActivity.this, ""+price, Toast.LENGTH_SHORT).show();

                        pPriceTV.setText(String.valueOf(price));

                    }else{
                        Toast.makeText(ProductDetailsActivity.this, "Maximum quantity reached", Toast.LENGTH_SHORT).show();
                    }

                }
            });

            subIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (quantity > 1) {

                        quantity = quantity - 1;
                        pQuantityTV.setText(String.valueOf(quantity));

                        int amount = Integer.valueOf(pPrice);

                        price = quantity * amount;

                        //Toast.makeText(ProductDetailsActivity.this, ""+price, Toast.LENGTH_SHORT).show();

                        pPriceTV.setText(String.valueOf(price));

                    }else{
                        Toast.makeText(ProductDetailsActivity.this, "Minimum quantity required", Toast.LENGTH_SHORT).show();
                    }


                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

   //initialising the values to the declared variables from xml file
    private void iniit() {
        pNameTV = findViewById(R.id.dProductNameTextView);
        pPriceTV = findViewById(R.id.dProductPriceTextView);
        pQuantityTV = findViewById(R.id.dProductQuantityTextView);
        addCartBT = findViewById(R.id.addCartButton);
        addIV = findViewById(R.id.addImageView);
        subIV = findViewById(R.id.subImageView);
    }
}