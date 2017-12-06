package com.example.mladen.masterradandroid.dagger;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.mladen.masterradandroid.model.SearchModel;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;
import io.realm.annotations.PrimaryKey;



@Module
public class MyModule {

    @Provides
    @Singleton
    public Realm provideMyDatabeseInstance() {
        return Realm.getDefaultInstance();
    }

    @Provides
    @Singleton
    public SearchModel provideSearchModel() {
        return new SearchModel();
    }
}
