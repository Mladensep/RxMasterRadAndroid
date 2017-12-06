package com.example.mladen.masterradandroid.model;


import android.content.Context;
import com.example.mladen.masterradandroid.R;
import java.util.ArrayList;
import java.util.List;


public class NavigationDrawerItemModel {
    private String title;
    private int imageId;
    public static Context context;
    private String mailfb;
    private String  tokenfb;

    public NavigationDrawerItemModel() {}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public List<NavigationDrawerItemModel> getData(String mailfb, String tokenfb) {
        this.mailfb = mailfb;
        this.tokenfb = tokenfb;

        List<NavigationDrawerItemModel> dataList = new ArrayList<>();

        int[] imageIds = getImages();
        String[] titles = getTitles();


        for (int i = 0; i < titles.length; i++) {
            NavigationDrawerItemModel navItem = new NavigationDrawerItemModel();
            navItem.setTitle(titles[i]);
            navItem.setImageId(imageIds[i]);
            dataList.add(navItem);
        }
        return dataList;
    }

    private static int[] getImages() {

        return new int[]{
                R.mipmap.search_icon,
                R.mipmap.icon, 0, 0,
                0, R.mipmap.logout};
    }

    private String[] getTitles() {
        String loginLogout;

        if(tokenfb.equals("") || tokenfb == null){
            loginLogout = "Пријава";
        } else {
            loginLogout = "Одјава";
        }

        return new String[] {
                "Претрага", "О апликацији", "", "", "", loginLogout
        };
    }
}
