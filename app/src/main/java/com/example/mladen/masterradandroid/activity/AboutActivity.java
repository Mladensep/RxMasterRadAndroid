package com.example.mladen.masterradandroid.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.mladen.masterradandroid.R;
import com.jakewharton.rxbinding2.view.RxView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.functions.Consumer;

public class AboutActivity extends AppCompatActivity {
    @BindView(R.id.back_icon) ImageView button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        getSupportActionBar().hide();

        ButterKnife.bind(this);

        RxView.clicks(button)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        finish();
                    }
                });
    }
}
