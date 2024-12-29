package com.nextgen.qfree;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

public class EditProfileActivity extends AppCompatActivity {

    EditText cNameET;
    EditText cEmailET;
    EditText cUsernameET;
    EditText cNumberET;
    EditText cAddressET;
    TextView submitTV;

    private GlobalPreference globalPreference;
    private String ip,uid;
    private Intent intent;
    String intentResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        globalPreference = new GlobalPreference(this);
        ip = globalPreference.getIp();
        uid = globalPreference.getID();

        intent = getIntent();
        intentResponse = intent.getStringExtra("userdata");

        initial();

        setData(intentResponse);

        submitTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateData();
            }
        });


    }



    private void setData(String response) {
        try{
            JSONObject obj = new JSONObject(response);
            JSONArray array = obj.getJSONArray("data");
            JSONObject data = array.getJSONObject(0);

            String cName = data.getString("name");
            String cEmail = data.getString("email");
            String cUserName = data.getString("username");
            String cNumber = data.getString("phone");
            String cAddress = data.getString("address");


            cNameET.setText(cName);
            cEmailET.setText(cEmail);
            cUsernameET.setText(cUserName);
            cNumberET.setText(cNumber);
            cAddressET.setText(cAddress);

        }catch (JSONException e){
            e.printStackTrace();
        }

    }

    private void updateData() {
        String URL = "http://"+ ip +"/Qless/api/editProfile.php";


        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(EditProfileActivity.this,""+response,Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(EditProfileActivity.this,ProfileActivity.class);
                startActivity(intent);
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(EditProfileActivity.this,""+error,Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();

                params.put("cName",cNameET.getText().toString());
                params.put("cEmail",cEmailET.getText().toString());
                params.put("cUsername",cUsernameET.getText().toString());
                params.put("cNumber",cNumberET.getText().toString());
                params.put("address",cAddressET.getText().toString());
                params.put("uid",uid);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void initial() {
        cNameET = findViewById(R.id.cNameEditText);
        cEmailET = findViewById(R.id.cEmailEditText);
        cUsernameET = findViewById(R.id.cUserNameEditText);
        cNumberET = findViewById(R.id.cNumberEditText);
        cAddressET = findViewById(R.id.cAddressEditText);
        submitTV = findViewById(R.id.cSubmitTextView);
    }
}