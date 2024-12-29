package com.nextgen.qfree.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nextgen.qfree.GlobalPreference;
import com.nextgen.qfree.ModelClass.PointsModelClass;
import com.nextgen.qfree.R;

import java.util.ArrayList;

public class PointsAdapter extends RecyclerView.Adapter<PointsAdapter.MyViewHolder> {

    ArrayList<PointsModelClass> list;
    Context context;
    String ip;

    public PointsAdapter(ArrayList<PointsModelClass> list, Context context) {
        this.list = list;
        this.context = context;

        GlobalPreference globalPreference = new GlobalPreference(context);
        ip = globalPreference.getIp();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.raw_points,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        PointsModelClass pointsList = list.get(position);
        holder.pointsTV.setText("+ "+pointsList.getPoints()+" pts");

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView pointsTV;
        TextView descTV;

        public MyViewHolder(@NonNull View itemview) {
            super(itemview);

            pointsTV = itemview.findViewById(R.id.addPointTextView);

        }
    }
}
