package com.example.mladen.masterradandroid.utils;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.example.mladen.masterradandroid.R;

public class Utils {

    public static void replaceFragment(FragmentActivity fragmentActivity, Fragment fragment, String tag) {
        FragmentTransaction ft = fragmentActivity.getSupportFragmentManager().beginTransaction();
        ft.addToBackStack(tag);
        ft.replace(R.id.fragment_replace, fragment);
        ft.commit();
    }
}
