package com.nextgen.qfree;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
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

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    GlobalPreference globalPreference;
    private String ip;

    EditText nameET;
    EditText emailET;
    EditText usernameET;
    EditText phoneET;
    EditText addressET;
    EditText accnoET;
    EditText passwordET;
    Button signupBT;
    TextView signinTV;

    private boolean val;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        globalPreference = new GlobalPreference(this);
        ip = globalPreference.getIp();

        nameET = findViewById(R.id.nameEditText);
        emailET = findViewById(R.id.emailEditText);
        usernameET = findViewById(R.id.usernameEditText);
        phoneET = findViewById(R.id.phoneEditText);
        addressET = findViewById(R.id.addressEditText);
        accnoET = findViewById(R.id.accnoEditText);
        passwordET = findViewById(R.id.passwordEditText);
        signupBT = findViewById(R.id.signupButton);
        signinTV = findViewById(R.id.signinTextView);

        signinTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        signupBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                check();
            }
        });

    }

    private void check() {
        if (nameET.getText().toString().equals("")) {
            nameET.setError("Please Enter name");
        }
        else if (emailET.getText().equals("")) {
            emailET.setError("Please Enter Email");
        }
        else if (usernameET.getText().toString().equals("")) {
            usernameET.setError("Please Enter Username");
        }else if (phoneET.getText().equals("") || phoneET.getText().length() > 10 || phoneET.getText().length() < 10) {
            phoneET.setError("Invalid Phone number ");
        }
        else if (addressET.getText().toString().equals("")) {
            addressET.setError("Please Enter Address");
        }
        else if (accnoET.getText().toString().equals("") || phoneET.getText().length() > 10 || phoneET.getText().length() < 5) {
            addressET.setError("Account Number should be minimum 5 numbers and Maximum 10");
        }
        else if (passwordET.getText().equals("") || passwordET.getText().length() < 5) {
            passwordET.setError("Password Empty or It Doesnot contain 5 letters");
        } else if (emailET.getText().length() > 0) {
            val = validateEmail(emailET);
            if (val == true) {
                rRegister();
            } else {
                Toast.makeText(RegisterActivity.this, "Please Check Your Email id", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void rRegister() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://"+ ip +"/Qless/api/register.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("success")) {
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(RegisterActivity.this, "" + response, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RegisterActivity.this, "" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name", nameET.getText().toString());
                params.put("email", emailET.getText().toString());
                params.put("password", passwordET.getText().toString());
                params.put("address", addressET.getText().toString());
                params.put("phone", phoneET.getText().toString());
                params.put("accno", accnoET.getText().toString());
                params.put("username", usernameET.getText().toString());
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(RegisterActivity.this);
        requestQueue.add(stringRequest);
    }

    private boolean validateEmail(EditText emailET) {
        String email = emailET.getText().toString();

        if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            // Toast.makeText(UserRegisterActivity.this,"Email Validated",Toast.LENGTH_SHORT).show();
            return true;
        } else {
            Toast.makeText(RegisterActivity.this, "Invalid email", Toast.LENGTH_SHORT).show();
            return false;

        }
    }
}