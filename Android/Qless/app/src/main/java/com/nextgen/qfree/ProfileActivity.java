package com.nextgen.qfree;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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

public class ProfileActivity extends AppCompatActivity {

    TextView nameTV;
    TextView emailTV;
    TextView usernameTV;
    TextView phoneTV;
    TextView accTV;
    TextView addressTV;
    ImageView backIV;
    ImageView editProfileIV;

    private GlobalPreference globalPreference;
    private String id;
    private String ip;
    String userdata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        globalPreference= new GlobalPreference(this);
        id= globalPreference.getID();
        ip=globalPreference.getIp();


        nameTV=findViewById(R.id.nameTextView);
        emailTV=findViewById(R.id.emailTextView);
        usernameTV=findViewById(R.id.usernameTextView);
        phoneTV=findViewById(R.id.phoneTextView);
        accTV=findViewById(R.id.accountTextView);
        addressTV=findViewById(R.id.addressTextView);
        backIV = findViewById(R.id.profileBackIV);
        editProfileIV = findViewById(R.id.editProfileImageView);

        getUserProfile();

        editProfileIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this,EditProfileActivity.class);
                intent.putExtra("userdata",userdata);
                startActivity(intent);
            }
        });

        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this,HomeActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getUserProfile() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://" + ip +"/Qless/api/profile.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                userdata = response;


                if (!response.equals("")) ;
                {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        JSONObject data = jsonArray.getJSONObject(0);

                        String name = data.getString("name");
                        String email = data.getString("email");
                        String acc = data.getString("accno");
                        String address = data.getString("address");
                        String username = data.getString("username");
                        String phone = data.getString("phone");


                        nameTV.setText(name);
                        emailTV.setText(email);
                        usernameTV.setText(username);
                        addressTV.setText(address);
                        accTV.setText(acc);
                        phoneTV.setText(phone);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ProfileActivity.this, "" + error, Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            @Nullable

            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("uid", id);
                return params;


            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(ProfileActivity.this);
        requestQueue.add(stringRequest);
    }
}