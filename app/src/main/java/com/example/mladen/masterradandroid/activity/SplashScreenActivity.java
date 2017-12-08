package com.example.mladen.masterradandroid.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.mladen.masterradandroid.R;
import com.example.mladen.masterradandroid.database.RealmHelper;
import com.example.mladen.masterradandroid.model.SchoolModel;
import com.example.mladen.masterradandroid.model.SchoolRealmModel;
import com.example.mladen.masterradandroid.retrofit.RestApi;
import com.example.mladen.masterradandroid.retrofit.RestClient;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;


public class SplashScreenActivity extends AppCompatActivity {

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private final int SPLASH_DISPLAY_LENGTH = 3;
    private RealmHelper realmHelper;
    private RestApi apiService;
    private ArrayList<SchoolModel> mAndroidArrayList;
    private CompositeDisposable disposable = new CompositeDisposable();

    private String mailfb;
    private String tokenfb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_splash_screen);

        realmHelper = new RealmHelper(this);

        disposable.add(realmHelper.findAllRx()
            //.subscribeOn(Schedulers.io()) Realm access from incorrect thread. Realm objects can only be accessed on the thread they were created.
            .subscribeWith(new DisposableObserver<List<SchoolRealmModel>>() {
                @Override
                public void onNext(List<SchoolRealmModel> schoolRealmModels) {
                    if(schoolRealmModels.size() == 0) {
                        apiService = RestClient.getClient();
                        compositeDisposable = new CompositeDisposable();

                        getSchoolData();
                    }
                }

                @Override
                public void onError(Throwable e) {
                    Toast.makeText(SplashScreenActivity.this, "Грешка " , Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onComplete() {

                }
            }));

        sharedPref()
                .subscribeOn(Schedulers.io())
                .subscribe();

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {

                if(tokenfb == "") {
                    Intent mainIntent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                    SplashScreenActivity.this.startActivity(mainIntent);
                    SplashScreenActivity.this.finish();
                } else {
                    Intent mainIntent = new Intent(SplashScreenActivity.this, HomeTabsActivity.class);
                    SplashScreenActivity.this.startActivity(mainIntent);
                    SplashScreenActivity.this.finish();
                }
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    public Observable<Void> sharedPref() {
        SharedPreferences sharedPref = this.getSharedPreferences("facebook", Context.MODE_PRIVATE);

        mailfb = sharedPref.getString("emailData", "");
        tokenfb = sharedPref.getString("tokenData", "");

        return Observable.empty();
    }

    private void getSchoolData() {
        compositeDisposable.add(apiService.getAllSchoolData()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse,this::handleError));
    }

    private void handleResponse(List<SchoolModel> androidList) {

        mAndroidArrayList = new ArrayList<>(androidList);

        realmHelper.addSchoolsRx(mAndroidArrayList);
        Toast.makeText(SplashScreenActivity.this, "Added to realm", Toast.LENGTH_SHORT).show();
    }

    private void handleError(Throwable error) {
        Toast.makeText(this, "Error "+error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        if (!compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
        if (!disposable.isDisposed()) {
            disposable.dispose();
        }
        super.onDestroy();
    }
}
