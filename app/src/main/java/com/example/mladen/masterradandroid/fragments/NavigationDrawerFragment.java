package com.example.mladen.masterradandroid.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mladen.masterradandroid.R;
import com.example.mladen.masterradandroid.adapter.NavigationDrawerAdapter;
import com.example.mladen.masterradandroid.model.NavigationDrawerItemModel;


public class NavigationDrawerFragment extends Fragment {
    private ActionBarDrawerToggle   mDrawerToggle;
    private DrawerLayout            mDrawerLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);

        setUpRecyclerView(view);

        return view;
    }

    private void setUpRecyclerView(View view) {

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.drawerList);


        SharedPreferences sharedPref = this.getContext().getSharedPreferences("facebook", Context.MODE_PRIVATE);

        String mailfb = sharedPref.getString("emailData", "");
        String tokenfb = sharedPref.getString("tokenData", "");

        NavigationDrawerItemModel itemModel = new NavigationDrawerItemModel();

        NavigationDrawerAdapter adapter = new NavigationDrawerAdapter(getActivity(), itemModel.getData(mailfb, tokenfb));
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    public void setUpDrawer(int fragmentId, DrawerLayout drawerLayout, Toolbar toolbar) {
        mDrawerLayout = drawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
            }
        };

        mDrawerLayout.addDrawerListener(mDrawerToggle);

        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
                mDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
            }
        });
    }
}
