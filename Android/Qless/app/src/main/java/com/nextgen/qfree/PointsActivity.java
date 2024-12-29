package com.nextgen.qfree;

import androidx.annotation.Nullable;
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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nextgen.qfree.Adapters.PointsAdapter;
import com.nextgen.qfree.ModelClass.PointsModelClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PointsActivity extends AppCompatActivity {

    private static String TAG ="PointsActivity";

    TextView pointsTV;
    private ImageView backIV;

    RecyclerView pointsRV;
    ArrayList<PointsModelClass> list;

    private GlobalPreference globalPreference;
    private String ip,uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_points);

        globalPreference = new GlobalPreference(this);
        ip = globalPreference.getIp();
        uid = globalPreference.getID();

        pointsTV = findViewById(R.id.pointsTextView);
        backIV = findViewById(R.id.pointsBackImageView);

        pointsRV = findViewById(R.id.pointsRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        pointsRV.setLayoutManager(layoutManager);

        getPoints();

        getPointHistory();

        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PointsActivity.this,HomeActivity.class);
                startActivity(intent);
            }
        });
    }



    private void getPoints() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://"+ ip +"/Qless/api/getPoints.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d(TAG,"onResponse :"+response);

                pointsTV.setText(response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PointsActivity.this, ""+error, Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("uid",uid);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(PointsActivity.this);
        requestQueue.add(stringRequest);
    }

    private void getPointHistory() {

        list = new ArrayList<>();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://"+ ip +"/Qless/api/getPointDetails.php?uid="+uid, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d(TAG, "onResponse: "+response);

                if (response.equals("failed")){
                    Toast.makeText(PointsActivity.this, "No Points Available", Toast.LENGTH_SHORT).show();
                }
                else{
                    try{
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i=0; i< jsonArray.length();i++){
                            JSONObject object = jsonArray.getJSONObject(i);
                            String id = object.getString("id");
                            String points = object.getString("points");
                            list.add(new PointsModelClass(id,points));

                        }

                        PointsAdapter adapter = new PointsAdapter(list,PointsActivity.this);
                        pointsRV.setAdapter(adapter);

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