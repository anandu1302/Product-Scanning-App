package com.nextgen.qfree;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class LoginActivity extends AppCompatActivity {

    GlobalPreference globalPreference;
    private String ip;

    EditText usernameET;
    EditText passwordET;
    Button signinBT;
    TextView signupTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        globalPreference = new GlobalPreference(this);
        ip = globalPreference.getIp();

        usernameET = findViewById(R.id.userNameEditText);
        passwordET = findViewById(R.id.passwordEditText);
        signinBT = findViewById(R.id.signinButton);
        signupTV = findViewById(R.id.signupTextView);


        signinBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (usernameET.getText().toString().equals("") || passwordET.getText().toString().equals("")){
                    Toast.makeText(LoginActivity.this, "Please Fill the Fields", Toast.LENGTH_SHORT).show();
                }else{
                    login();
                }


            }
        });

        signupTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void login() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://"+ ip +"/Qless/api/login.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("failed")) {
                    Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        String id = jsonObject.getString("id");
                        String name = jsonObject.getString("name");
                        String email = jsonObject.getString("email");
                        globalPreference.saveID(id);
                        globalPreference.saveName(name);
                        globalPreference.saveEmail(email);

                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(intent);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this, ""+error, Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String,String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("username",usernameET.getText().toString());
                params.put("password",passwordET.getText().toString());
                return  params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
        requestQueue.add(stringRequest);
    }
}