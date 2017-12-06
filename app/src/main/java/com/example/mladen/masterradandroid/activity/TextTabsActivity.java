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
import com.example.mladen.masterradandroid.fragments.RecensionsSchoolFragment;
import com.example.mladen.masterradandroid.fragments.CommentSchoolFragment;
import com.example.mladen.masterradandroid.fragments.DetailSchoolFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TextTabsActivity extends AppCompatActivity {

    private List<Fragment> fragmentList = new ArrayList<>();
    private List<String> titleList = new ArrayList<>();
    private TextTabsAdapter adapter;

    @BindView(R.id.viewPager) ViewPager viewPager;
    @BindView(R.id.tabs) TabLayout tabLayout;
    @BindView(R.id.toolBarTab) Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_tabs);
        getSupportActionBar().hide();


        ButterKnife.bind(this);

        setUpDrawer();

        prepareDataResource();

        adapter = new TextTabsAdapter(getSupportFragmentManager(), fragmentList, titleList);

        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);
    }

    private void setUpDrawer() {
        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.nav_drwr_fragment);
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerFragment.setUpDrawer(R.id.nav_drwr_fragment, drawerLayout, toolbar);
    }

    private void prepareDataResource() {
        addData(new DetailSchoolFragment(), "Детаљи");
        addData(new CommentSchoolFragment(), "Коментари");
        addData(new RecensionsSchoolFragment(), "Рецензије");
    }

    private void addData(Fragment fragment, String title) {
        fragmentList.add(fragment);
        titleList.add(title);
    }
}
