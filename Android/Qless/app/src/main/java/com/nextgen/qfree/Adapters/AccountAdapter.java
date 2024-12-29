package com.nextgen.qfree.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nextgen.qfree.GlobalPreference;
import com.nextgen.qfree.ModelClass.AccountModelClass;
import com.nextgen.qfree.R;

import java.util.ArrayList;

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.MyViewHolder>{

    ArrayList<AccountModelClass> list;
    Context context;
    String ip;

    public AccountAdapter(ArrayList<AccountModelClass> list, Context context) {
        this.list = list;
        this.context = context;

        GlobalPreference globalPreference = new GlobalPreference(context);
        ip = globalPreference.getIp();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.raw_account,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        AccountModelClass accountList = list.get(position);
        holder.bankNameTV.setText(accountList.getBank());
        holder.accNameTV.setText(accountList.getAccname());
        holder.accNumberTV.setText(accountList.getAccountno());
        holder.balanceTV.setText("₹ "+accountList.getBalance());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView bankNameTV;
        TextView accNameTV;
        TextView accNumberTV;
        TextView balanceTV;

        public MyViewHolder(@NonNull View itemview) {
            super(itemview);

            bankNameTV = itemview.findViewById(R.id.aBankNameTextView);
            accNameTV = itemview.findViewById(R.id.aAccountNameTextView);
            accNumberTV = itemview.findViewById(R.id.aAccountNumberTextView);
            balanceTV = itemview.findViewById(R.id.aBalanceTextView);

        }
    }
}
