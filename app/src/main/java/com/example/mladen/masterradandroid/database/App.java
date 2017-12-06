package com.example.mladen.masterradandroid.database;


import android.app.Application;

import com.example.mladen.masterradandroid.dagger.DaggerMyComponent;
import com.example.mladen.masterradandroid.dagger.MyComponent;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class App extends Application{
    private MyComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        component = createComponent();

        Realm.init(this);

        RealmConfiguration config = new RealmConfiguration
                .Builder()
                .deleteRealmIfMigrationNeeded()
                .build();

        Realm.setDefaultConfiguration(config);
    }

    private MyComponent createComponent() {
        return DaggerMyComponent.create();
    }

    public MyComponent getComponent() {
        return component;
    }

}
