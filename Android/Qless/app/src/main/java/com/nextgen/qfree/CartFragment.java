package com.nextgen.qfree;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nextgen.qfree.ModelClass.OffersModelClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CartFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    String pname = "", price = "", id;
    int total;
    View view;
    ListView cartList;
    ArrayList<HashMap<String, String>> prodctList;
    private GlobalPreference globalPreference;
    private String ip, uid;
    private SwipeRefreshLayout swipeRefreshLayout;
    FloatingActionButton cartFAB;

    public CartFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static CartFragment newInstance(String param1, String param2) {
        CartFragment fragment = new CartFragment();
        ;

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        globalPreference = new GlobalPreference(getContext());
        ip = globalPreference.getIp();
        uid = globalPreference.getID();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_cart, container, false);

        cartList = view.findViewById(R.id.listCartLIstView);
        prodctList = new ArrayList<HashMap<String, String>>();
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        cartFAB = view.findViewById(R.id.cartFAB);

        getCart(uid);

        cartFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = null;
                fragment = new PurchaseFragment();
                if (fragment != null) {
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.content_frame, fragment);
                    ft.commit();
                }
            }
        });
        return view;
    }

    ///////////////////////////////////getCart///////////////////////////////////////////////////////
    private void getCart(String uid) {
        String UrlData = "?uid=" + uid;

        Log.d("url", "" + UrlData);


        class CartDetails extends AsyncTask<String, Void, String> {


            ProgressDialog progressDialog;


            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(getActivity(), "Please wait", null, true, true);


            }

            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressDialog.dismiss();
                //  Toast.makeText(getActivity(), s, Toast.LENGTH_LONG).show();
                if (s != null && s.equalsIgnoreCase("failed")) {
                    Toast.makeText(getActivity(), "No product In cart", Toast.LENGTH_LONG).show();
                } else {
                    showlist(s);

                   // Toast.makeText(getContext(), "s" + s, Toast.LENGTH_SHORT).show();
//
                }

            }

            @Override
            protected String doInBackground(String... params) {

                String s = params[0];
                Log.d("result", params[0]);
                BufferedReader bf = null;

                try {
                    URL url = new URL("http://" + ip + "/Qless/api/getCart.php" + s);

                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    bf = new BufferedReader(new InputStreamReader(connection.getInputStream()));


                    String result = bf.readLine();
                    return result;


                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }


            }

        }


        CartDetails cd = new CartDetails();
        cd.execute(UrlData);
    }


    public void showlist(String res) {

        Log.d("res", res);
        try {
            JSONObject jsonObject = new JSONObject(res);
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            for (int i=0; i< jsonArray.length();i++){
                JSONObject object = jsonArray.getJSONObject(i);
                String id = object.getString("id");
                String productName = object.getString("pname");
                String price = object.getString("price");
                String quantity = object.getString("quantity");

                HashMap<String, String> hist= new HashMap<String, String>();

                hist.put("pname", productName);
                hist.put("price", price);
                hist.put("quantity",quantity);

                hist.put("id", id);

                prodctList.add(hist);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final ListAdapter adapter = new SimpleAdapter(getActivity(), prodctList, R.layout.raw_list_cart, new String[]{"pname","quantity", "price"}, new int[]{R.id.titem,R.id.tiquantity, R.id.tiprice});


        cartList.setAdapter(adapter);

        cartList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                id = prodctList.get(i).get("id");


                LayoutInflater li = LayoutInflater.from(getActivity());
                View promptsView1 = li.inflate(R.layout.raw_prompt_delete, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView1);

                final Button deleteBT = (Button) promptsView1.findViewById(R.id.deleteCartButton);


                final AlertDialog alertDialog = alertDialogBuilder.create();
                deleteBT.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        // TODO Auto-generated method stub

                        prodctList.remove(id);

                        //Toast.makeText(getContext(), "id.."+id, Toast.LENGTH_SHORT).show();

                        deleteCartItem(id);
                        alertDialog.cancel();


                    }
                });


                alertDialog.show();

            }
        });

    }

    ////////////////////////////////////////////// Delete Cart Item /////////////////////////////////////////////////////

    private void deleteCartItem(String id) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://" + ip + "/Qless/api/deleteCartItem.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("success")) {
                    Toast.makeText(getContext(), "Item Deleted", Toast.LENGTH_SHORT).show();

                    refreshFragment();

                } else {
                    Toast.makeText(getContext(), "" + response, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("pid",id);
                params.put("uid",uid);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void refreshFragment() {

        prodctList.clear();
        getCart(uid);

    }
}