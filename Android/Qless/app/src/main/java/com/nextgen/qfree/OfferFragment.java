package com.nextgen.qfree;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nextgen.qfree.Adapters.OffersListAdapter;
import com.nextgen.qfree.ModelClass.OffersModelClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**

 */
public class OfferFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private static String TAG ="OfferFragment";

    View view;
    RecyclerView offersRV;
    ArrayList<OffersModelClass> list;
    TextView noOfferTV;
    private String ip;
    private GlobalPreference globalPreference;


    public OfferFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static OfferFragment newInstance(String param1, String param2) {
        OfferFragment fragment = new OfferFragment();

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
        view =  inflater.inflate(R.layout.fragment_offer, container, false);

        offersRV = view.findViewById(R.id.offersRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        offersRV.setLayoutManager(layoutManager);

        noOfferTV = view.findViewById(R.id.noOfferTextView);

        getOffers();

        return view;
    }

    private void getOffers() {
        list = new ArrayList<>();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://"+ ip +"/Qless/api/getOffers.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d(TAG, "onResponse: "+response);

                if (response.equals("failed")){
                   // Toast.makeText(getContext(), ""+response, Toast.LENGTH_SHORT).show();
                    noOfferTV.setVisibility(View.VISIBLE);
                    offersRV.setVisibility(View.GONE);
                }
                else{
                    noOfferTV.setVisibility(View.GONE);
                    offersRV.setVisibility(View.VISIBLE);
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

                        OffersListAdapter adapter = new OffersListAdapter(list,getContext());
                        offersRV.setAdapter(adapter);

                    } catch(JSONException e){
                        e.printStackTrace();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),""+error,Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }
}