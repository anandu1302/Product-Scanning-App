package com.nextgen.qfree;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nextgen.qfree.Adapters.AccountAdapter;
import com.nextgen.qfree.ModelClass.AccountModelClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AccountDetailsActivity extends AppCompatActivity {

    private static String TAG ="AccountDetailsActivity";

    RecyclerView accountRV;
    ArrayList<AccountModelClass> list;

    private GlobalPreference globalPreference;
    private String ip,uid;

    private ImageView backIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_details);

        globalPreference = new GlobalPreference(this);
        ip = globalPreference.getIp();
        uid = globalPreference.getID();


        accountRV = findViewById(R.id.accountRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        accountRV.setLayoutManager(layoutManager);

        getAccountDetails();


        backIV = findViewById(R.id.accountBackImageView);

        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent bIntent = new Intent(AccountDetailsActivity.this,HomeActivity.class);
                startActivity(bIntent);
            }
        });

    }

    private void getAccountDetails() {

        list = new ArrayList<>();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://"+ ip +"/Qless/api/getAccounts.php?uid="+uid, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d(TAG, "onResponse: "+response);

                if (response.equals("failed")){
                    Toast.makeText(AccountDetailsActivity.this, "No Accounts Available", Toast.LENGTH_SHORT).show();
                }
                else{
                    try{
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i=0; i< jsonArray.length();i++){
                            JSONObject object = jsonArray.getJSONObject(i);
                            String id = object.getString("id");
                            String accname = object.getString("accname");
                            String accountno = object.getString("accountno");
                            String bank = object.getString("bank");
                            String balance = object.getString("amount");

                            list.add(new AccountModelClass(id,accname,accountno,bank,balance));

                        }

                        AccountAdapter adapter = new AccountAdapter(list,AccountDetailsActivity.this);
                        accountRV.setAdapter(adapter);

                    } catch(JSONException e){
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: "+error);
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
}