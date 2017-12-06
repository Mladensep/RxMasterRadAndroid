package com.example.mladen.masterradandroid.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.mladen.masterradandroid.R;
import com.example.mladen.masterradandroid.adapter.TextTabsAdapter;
import com.example.mladen.masterradandroid.fragments.NavigationDrawerFragment;
import com.example.mladen.masterradandroid.fragments.SearchResultFragment;
import com.example.mladen.masterradandroid.maps.MapsActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchResultTabActivity extends AppCompatActivity {

    private List<Fragment> fragmentList = new ArrayList<>();
    private List<String> stringList = new ArrayList<>();
    private TextTabsAdapter adapter;

    @BindView(R.id.toolbarId) Toolbar toolbar;
    @BindView(R.id.viewPager) ViewPager viewPager;
    @BindView(R.id.tabs) TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_search_result_tab);

        ButterKnife.bind(this);

        setUpDrawer();

        prepareData();

        adapter = new TextTabsAdapter(getSupportFragmentManager(),fragmentList, stringList);
        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);
    }

    private void setUpDrawer() {
        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.nav_drwr_fragment);
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerFragment.setUpDrawer(R.id.nav_drwr_fragment, drawerLayout, toolbar);
    }

    private void prepareData() {
        addData(new SearchResultFragment(), "Листа школа");
        addData(new MapsActivity(), "Приказ на мапи");
    }

    private void addData(Fragment fragment, String string) {
        fragmentList.add(fragment);
        stringList.add(string);
    }
}
