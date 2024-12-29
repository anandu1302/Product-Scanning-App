package com.nextgen.qfree;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;
import com.nextgen.qfree.Adapters.OffersListAdapter;
import com.nextgen.qfree.ModelClass.OffersModelClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static String TAG ="HomeActivity";

    Fragment fragment = null;
    ArrayList<OffersModelClass> list;

    TextView nav_usernameTV;
    TextView nav_emailTV;

    private GlobalPreference globalPreference;
    private String ip,name,email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        globalPreference = new GlobalPreference(this);
        ip = globalPreference.getIp();
        name = globalPreference.getName();
        email = globalPreference.getEmail();

      //  Toast.makeText(this, ""+ip, Toast.LENGTH_SHORT).show();

        //for displaying the default screen
        displaySelectedScreen(R.id.nav_offers);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View hView =  navigationView.getHeaderView(0);
        nav_usernameTV =hView.findViewById(R.id.txtuser);
        nav_emailTV = hView.findViewById(R.id.txtemail);


        nav_usernameTV.setText(name);
        nav_emailTV.setText(email);

    }


    private void displaySelectedScreen(int itemId) {

        switch (itemId) {
            case R.id.nav_profile:
                Intent i=new Intent(HomeActivity.this,ProfileActivity.class);
                startActivity(i);
                break;

            case R.id.nav_account:
                Intent accIntent = new Intent(HomeActivity.this,AccountDetailsActivity.class);
                startActivity(accIntent);
                break;

            case R.id.nav_purchase:
                fragment = new PurchaseFragment();
                break;

            case R.id.nav_rewards:
                Intent rewardIntent = new Intent(HomeActivity.this,PointsActivity.class);
                startActivity(rewardIntent);
                break;

            case R.id.nav_cart:
                fragment = new CartFragment();
                break;
            case R.id.nav_history:
                fragment = new HistoryFragment();
                break;
            case R.id.nav_payment:
                fragment = new PaymentFragment();
                break;
            case R.id.nav_offers:
                fragment = new OfferFragment();
                break;

            case R.id.nav_logout:
                 logout();
                break;

        }

        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        displaySelectedScreen(id);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void getOffers() {

        list = new ArrayList<>();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://"+ ip +"/Qless/api/getOffers.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d(TAG, "onResponse: "+response);

                if (response.equals("failed")){
                    Toast.makeText(HomeActivity.this, ""+response, Toast.LENGTH_SHORT).show();
                }
                else{
                    try{
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i=0; i< jsonArray.length();i++){
                            JSONObject object = jsonArray.getJSONObject(i);
                            String id = object.getString("id");
                            String productName = object.getString("product_name");
                            String price = object.getString("price");
                            String offerPrice = object.getString("offer_price");

                            list.add(new OffersModelClass(id,productName,price,offerPrice));

                        }

                        /*OffersListAdapter adapter = new OffersListAdapter(list,HomeActivity.this);
                        challengeRV.setAdapter(adapter);*/

                    } catch(JSONException e){
                        e.printStackTrace();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               Toast.makeText(HomeActivity.this,""+error,Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(HomeActivity.this);
        requestQueue.add(stringRequest);
    }

    private void logout() {

        new AlertDialog.Builder(HomeActivity.this)
                .setMessage("Are you sure you want to Logout?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(HomeActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();

    }

}