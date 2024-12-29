package com.nextgen.qfree;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;


public class PaymentFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    View view;
    ListView productLV;
    ArrayList<HashMap<String, String>> productList;
    TextView totalTV;
    Button billPaymentBT;

    int total;
    private GlobalPreference globalPreference;
    private String ip,uid;
    private OnFragmentInteractionListener mListener;


    public PaymentFragment() {

    }

    // TODO: Rename and change types and number of parameters
    public static PaymentFragment newInstance(String param1, String param2) {
        PaymentFragment fragment = new PaymentFragment();

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
        view =  inflater.inflate(R.layout.fragment_payment, container, false);
        productLV = view.findViewById(R.id.paymentListView);
        totalTV = view.findViewById(R.id.totalTextView);
        billPaymentBT = view.findViewById(R.id.paymentButton);
        productList = new ArrayList<HashMap<String,String>>();

        getCart(uid);

        billPaymentBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String totAmount = totalTV.getText().toString();
                Intent intent = new Intent(getActivity(),PaymentActivity.class);
                intent.putExtra("total",totAmount);
                startActivity(intent);
            }
        });
        return view;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    //////////////////////////////////////////getCart//////////////////////////////////////////////
    private void getCart(String uid) {

        String UrlData = "?uid=" + uid ;

        Log.d("url",""+UrlData);

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
                if(s!=null && s.equalsIgnoreCase("failed")){
                    Toast.makeText(getActivity(),"No product In cart", Toast.LENGTH_LONG).show();
                }
                else {
                    showList(s);
                }

            }


            @Override
            protected String doInBackground(String... params) {

                String s = params[0];
                Log.d("result",params[0]);
                BufferedReader bf = null;

                try {
                    URL url = new URL("http://"+ip+"/Qless/api/cartDetails.php" + s);

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

    private void showList(String res) {
        Log.d("res", res);
        JSONArray jarray = null;
        try {
            JSONObject jsonObject = new JSONObject(res);
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            for (int i=0; i< jsonArray.length();i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                String id = object.getString("id");
                String pname = object.getString("product_name");
                String price = object.getString("price");
                String quantity = object.getString("qty");

                total+=Integer.parseInt(price);

                HashMap<String, String> hist= new HashMap<String, String>();

                hist.put("pname", pname);
                hist.put("price", price);
                hist.put("quantity",quantity);
                hist.put("id", id);

                productList.add(hist);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ListAdapter adapter = new SimpleAdapter(getActivity(), productList, R.layout.raw_cart_payment, new String[]{"pname","quantity", "price"}, new int[]{R.id.cartItemTV,R.id.cartQuantityTV, R.id.cartPriceTV});


        productLV.setAdapter(adapter);
        totalTV.setText(""+total);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}