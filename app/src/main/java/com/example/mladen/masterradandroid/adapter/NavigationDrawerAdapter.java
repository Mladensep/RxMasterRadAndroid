package com.example.mladen.masterradandroid.adapter;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mladen.masterradandroid.R;
import com.example.mladen.masterradandroid.activity.AboutActivity;
import com.example.mladen.masterradandroid.activity.LoginActivity;
import com.example.mladen.masterradandroid.activity.SearchActivity;
import com.example.mladen.masterradandroid.maps.MapsActivity;
import com.example.mladen.masterradandroid.model.NavigationDrawerItemModel;
import com.example.mladen.masterradandroid.model.SearchModel;
import com.facebook.login.LoginManager;

import java.util.Collections;
import java.util.List;

public class NavigationDrawerAdapter extends RecyclerView.Adapter<NavigationDrawerAdapter.MyViewHolder> {

    private List<NavigationDrawerItemModel> mDataList = Collections.emptyList();
    private LayoutInflater inflater;
    private Context context;
    private String mailfb = "";
    private String tokenfb = "";

    public NavigationDrawerAdapter(Context context, List<NavigationDrawerItemModel> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.mDataList = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.nav_drawer_list_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        SharedPreferences sharedPref = context.getSharedPreferences("facebook", Context.MODE_PRIVATE);
        mailfb = sharedPref.getString("emailData", "");
        tokenfb = sharedPref.getString("tokenData", "");

        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        NavigationDrawerItemModel current = mDataList.get(position);

        holder.imgIcon.setImageResource(current.getImageId());
        holder.title.setText(current.getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, holder.title.getText().toString(), Toast.LENGTH_SHORT).show();

//                if(position == 0) {
//                    SearchModel searchModel = new SearchModel();
//                    searchModel.setMesto("");
//                    searchModel.setNaziv("");
//
//                    final Intent intent = new Intent(context, MapsActivity.class);
//                    Bundle bundle = new Bundle();
//                    bundle.putParcelable("dataa", searchModel);
//                    intent.putExtras(bundle);
//                    context.startActivity(intent);
//                }
                if(position == 0) {
                    final Intent intent = new Intent(context, SearchActivity.class);
                    context.startActivity(intent);
                } else if(position == 1) {
                    final Intent intent = new Intent(context, AboutActivity.class);
                    context.startActivity(intent);
                } else if(position == 5) {
                    //android.os.Process.killProcess(android.os.Process.myPid());

                    SharedPreferences sharedPref = context.getSharedPreferences("facebook", Context.MODE_PRIVATE);
                    String mailfb = sharedPref.getString("emailData", "");
                    String tokenfb = sharedPref.getString("tokenData", "");

                    if(mailfb.equals("")) {
                        Intent intent = new Intent(context, LoginActivity.class);
                        context.startActivity(intent);
                    } else {

                        LoginManager.getInstance().logOut();

                        //SharedPreferences sharedPref2 = activity.getSharedPreferences("facebook", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("emailData", "");
                        editor.putString("tokenData", "");

                        editor.commit();

                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView imgIcon;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            imgIcon = (ImageView) itemView.findViewById(R.id.imgIcon);
        }
    }
}