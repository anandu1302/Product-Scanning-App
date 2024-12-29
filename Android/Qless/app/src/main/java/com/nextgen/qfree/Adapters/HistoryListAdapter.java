package com.nextgen.qfree.Adapters;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.nextgen.qfree.GlobalPreference;
import com.nextgen.qfree.ModelClass.HistoryModelClass;
import com.nextgen.qfree.ModelClass.OffersModelClass;
import com.nextgen.qfree.R;

import java.util.ArrayList;

public class HistoryListAdapter extends RecyclerView.Adapter<HistoryListAdapter.MyViewHolder>{

    ArrayList<HistoryModelClass> list;
    Context context;
    String ip,uid;

    public HistoryListAdapter(ArrayList<HistoryModelClass> list, Context context) {
        this.list = list;
        this.context = context;

        GlobalPreference globalPreference = new GlobalPreference(context);
        ip = globalPreference.getIp();
        uid = globalPreference.getID();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.raw_history,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        HistoryModelClass historyList = list.get(position);

        holder.productNameTV.setText(historyList.getProductName());
        holder.priceTV.setText("Price: "+historyList.getPrice());
        holder.dateTV.setText("Date: "+historyList.getDate());
        holder.quantityTV.setText("Quantity: "+historyList.getQuantity());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView productNameTV;
        TextView priceTV;
        TextView quantityTV;
        TextView dateTV;

        public MyViewHolder(@NonNull View itemview) {
            super(itemview);

            productNameTV = itemview.findViewById(R.id.hProductNameTextView);
            priceTV = itemview.findViewById(R.id.hPriceTextView);
            quantityTV = itemview.findViewById(R.id.hQuantityTextView);
            dateTV = itemview.findViewById(R.id.hDatePriceTextView);

        }
    }
}
