package com.example.mladen.masterradandroid.dagger;


import com.example.mladen.masterradandroid.activity.SearchActivity;
import com.example.mladen.masterradandroid.fragments.AllSchoolsFragment;
import com.example.mladen.masterradandroid.database.RealmHelper;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component (modules = MyModule.class)
public interface MyComponent {

    void inject(AllSchoolsFragment allSchoolsFragment);

    void inject(RealmHelper realmHelper);

    void inject(SearchActivity searchActivity);
}
