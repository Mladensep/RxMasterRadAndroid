package com.example.mladen.masterradandroid.adapter;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mladen.masterradandroid.R;
import com.example.mladen.masterradandroid.activity.TextTabsActivity;
import com.example.mladen.masterradandroid.model.SchoolModel;

import java.util.ArrayList;


public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {

    private ArrayList<SchoolModel> listSchool;
    private static final String TAG = "myApp";
    public DataAdapter(ArrayList<SchoolModel> listSchool) {
        this.listSchool = listSchool;
    }

    private Context context;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_all_school_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final SchoolModel schoolModel = listSchool.get(position);

        holder.naziv.setText(listSchool.get(position).naziv);
        holder.mesto.setText(listSchool.get(position).mesto);
        holder.vrsta.setText(listSchool.get(position).vrsta);

        holder.mRootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent;
                intent =  new Intent(context, TextTabsActivity.class);

                Bundle bundle = new Bundle();
                bundle.putParcelable("data", schoolModel);
                intent.putExtras(bundle);

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listSchool.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView naziv;
        private TextView mesto;
        private TextView vrsta;

        protected View mRootView;

        public ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();

            naziv = (TextView) itemView.findViewById(R.id.naziv_skole);
            mesto = (TextView) itemView.findViewById(R.id.mesto);
            vrsta = (TextView) itemView.findViewById(R.id.vrsta);
            mRootView = itemView;
        }
    }
}
