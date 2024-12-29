package com.nextgen.qfree;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PurchaseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PurchaseFragment extends Fragment {

    private static final String TAG = "PurchaseFragment";

    // TODO: Rename parameter arguments, choose names that match
     View view;
     ImageView qrIV;

    private GlobalPreference globalPreference;
    private String ip;

    private IntentIntegrator qrScan;

    public PurchaseFragment() {

    }

    // TODO: Rename and change types and number of parameters
    public static PurchaseFragment newInstance(String param1, String param2) {
        PurchaseFragment fragment = new PurchaseFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        globalPreference = new GlobalPreference(getContext());
        ip = globalPreference.getIp();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_purchase, container, false);
        qrIV = view.findViewById(R.id.qrImageView);

        qrScan = new IntentIntegrator(getActivity());

        qrIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                qrScan.forSupportFragment(PurchaseFragment.this).initiateScan();
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (result != null){
            if (result.getContents() == null){
                Toast.makeText(getContext(),"Result Not Found",Toast.LENGTH_LONG).show();
            }
            else{
                //if qr contains data

                Log.d(TAG, "qrData: "+result.getContents());

               // Toast.makeText(getContext(),"product id"+result.getContents(),Toast.LENGTH_SHORT).show();

               loadProductDetails(result.getContents().trim());

            }
        } else{
            Toast.makeText(getContext(), "Result null", Toast.LENGTH_LONG).show();
            super.onActivityResult(requestCode, resultCode, data);
        }

    }

    private void loadProductDetails(String productId) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://" + ip + "/Qless/api/getProductDetails.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d(TAG, "onResponse: "+response);

               if (response.equals("failed")){

                   Toast.makeText(getContext(),"Invalid Try Again",Toast.LENGTH_SHORT).show();

               }else{

                   Intent intent = new Intent(getActivity(), ProductDetailsActivity.class);
                   intent.putExtra("response",response);
                   intent.putExtra("pos",0);
                   startActivity(intent);
               }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: " + error);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("productId",productId);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }
}