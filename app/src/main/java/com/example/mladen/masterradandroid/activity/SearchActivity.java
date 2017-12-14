package com.example.mladen.masterradandroid.activity;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mladen.masterradandroid.R;
import com.example.mladen.masterradandroid.database.App;
import com.example.mladen.masterradandroid.database.RealmHelper;
import com.example.mladen.masterradandroid.model.SchoolModel;
import com.example.mladen.masterradandroid.model.SchoolRealmModel;
import com.example.mladen.masterradandroid.model.SearchModel;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class SearchActivity extends AppCompatActivity {

    @BindView(R.id.edit_text_naziv) EditText editNaziv;
    @BindView(R.id.edit_text_mesto) EditText editMesto;
    @BindView(R.id.samo_osnovne_id) CheckBox samoOsnovne;
    @BindView(R.id.samo_srednje_id) CheckBox samoSrednje;
    @BindView(R.id.search_result) RecyclerView searchResult;
    @BindView(R.id.search_result2) RecyclerView searchResult2;
    @BindView(R.id.back_icon) ImageView back;
    @BindView(R.id.submit_button) Button submitButton;

    private int osnovne;
    private int srednje;
    private boolean isOsnovne;
    private boolean isSrednje;

    @Inject SearchModel searchModel;

    private Disposable searchSubscription;
    private static final String TAG = "TestSearchActivity";
    private static final int MIN_LENGTH = 2;

    private boolean isChosen;
    private boolean isChosen2;
    private RealmHelper realmHelper;
    private ArrayList<SchoolModel> schoolModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getSupportActionBar().hide();

        ((App) getApplicationContext()).getComponent().inject(this);

        ButterKnife.bind(this);

        realmHelper = new RealmHelper(this);

        getAllSchool();

        SearchResultAdapter adapter = new SearchResultAdapter();
        SearchResultAdapter2 adapter2 = new SearchResultAdapter2();

        searchResult.setLayoutManager(new LinearLayoutManager(this));
        searchResult.setAdapter(adapter);

        searchResult2.setLayoutManager(new LinearLayoutManager(this));
        searchResult2.setAdapter(adapter2);

        if (isChosen == false) {
            searchSubscription = RxTextView.afterTextChangeEvents(editNaziv)
                    // Convert the event to a String
                    .map(textChangeEvent -> textChangeEvent.editable().toString())
                    // Perform search on computation scheduler
                    .observeOn(Schedulers.computation())
                    // If we get multiple events within 200ms, just emit the last one
                    .debounce(200, TimeUnit.MILLISECONDS)
                    // "Convert" the query string to a search result
                    .switchMap(this::searchNames)
                    // Switch back to the main thread
                    .observeOn(AndroidSchedulers.mainThread())
                    // Set the result on our adapter
                    .subscribe(adapter::setSearchResult);


            //location
            searchSubscription = RxTextView.afterTextChangeEvents(editMesto)
                    // Convert the event to a String
                    .map(textChangeEvent -> textChangeEvent.editable().toString())
                    // Perform search on computation scheduler
                    .observeOn(Schedulers.computation())
                    // If we get multiple events within 200ms, just emit the last one
                    .debounce(200, TimeUnit.MILLISECONDS)
                    // "Convert" the query string to a search result
                    .switchMap(this::searchCity)
                    // Switch back to the main thread
                    .observeOn(AndroidSchedulers.mainThread())
                    // Set the result on our adapter
                    .subscribe(adapter2::setSearchResult);
        }

        SharedPreferences sharedPref = this.getSharedPreferences("facebook", Context.MODE_PRIVATE);
        String highScore = sharedPref.getString("emailData", "");
        String highScore2 = sharedPref.getString("tokenData", "");

        RxView.clicks(back)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        finish();
                    }
                });

        RxView.clicks(submitButton)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        osnovne = 0;
                        srednje = 0;
                        String naziv = editNaziv.getText().toString();
                        String mesto = editMesto.getText().toString();

                        isOsnovne = samoOsnovne.isChecked();
                        if (isOsnovne)
                            osnovne = 1;

                        isSrednje = samoSrednje.isChecked();
                        if (isSrednje)
                            srednje = 1;

                        searchModel.setNaziv(naziv);
                        searchModel.setMesto(mesto);
                        searchModel.setSamoOsnovne(osnovne);
                        searchModel.setSamoSrednje(srednje);

                        Intent intent = new Intent(SearchActivity.this, SearchResultTabActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("dataa", searchModel);
                        intent.putExtras(bundle);
                        SearchActivity.this.startActivity(intent);
                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!searchSubscription.isDisposed()) {
            searchSubscription.dispose();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private Observable<List<String>> searchNames(String query) throws UnsupportedEncodingException {
        Log.d(TAG, "searchNames: Search for " + query);
        Log.d(TAG, "Searching on " + Thread.currentThread().getName());
        if (query == null || query.length() < MIN_LENGTH) {
            return Observable.just(Collections.emptyList());
        }

        LinkedList<String> result = new LinkedList<>();
        try (InputStream inputStream = getResources()
                .openRawResource(R.raw.naziv_skole)) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.toLowerCase().contains(query.toLowerCase())) {
                    result.add(line);
                }
            }
        } catch (IOException e) {
            return Observable.error(e);
        }

        Collections.sort(result);
        Log.d(TAG, "searchNames: Found " + result.size() + " hits!");
        return Observable.just(result);
    }


    private class SearchResultAdapter extends RecyclerView.Adapter<SearchResultViewHolder> {
        private List<String> mResult;

        @Override
        public SearchResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View viewItem = View.inflate(SearchActivity.this,
                    android.R.layout.simple_list_item_1, null);
            return new SearchResultViewHolder(viewItem);
        }

        @Override
        public void onBindViewHolder(SearchResultViewHolder holder, int position) {
            holder.textView.setText(mResult.get(position));

            holder.rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String text = (String) holder.textView.getText();

                    editNaziv.setText(text, TextView.BufferType.EDITABLE);

                    isChosen = true;
                }
            });
            if(isChosen == true) {
                holder.textView.setText("");
            }
            isChosen = false;
        }

        @Override
        public int getItemCount() {
            return mResult != null ? mResult.size() : 0;
        }

        public void setSearchResult(List<String> result) {
            mResult = result;
            notifyDataSetChanged();
        }
    }

    private class SearchResultViewHolder extends RecyclerView.ViewHolder {
        public  TextView textView = null;
        protected View rootView;

        public SearchResultViewHolder(View itemView) {
            super(itemView);

            if (isChosen != true) {
                textView = (TextView) itemView.findViewById(android.R.id.text1);

                rootView = itemView;
            }
        }
    }



    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private Observable<List<String>> searchCity(String query) throws UnsupportedEncodingException {
        Log.d(TAG, "searchNames: Search for " + query);
        Log.d(TAG, "Searching on " + Thread.currentThread().getName());
        if (query == null || query.length() < MIN_LENGTH) {
            return Observable.just(Collections.emptyList());
        }

        LinkedList<String> result = new LinkedList<>();
        try (InputStream inputStream = getResources()
                .openRawResource(R.raw.mesto_skole)) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.toLowerCase().contains(query.toLowerCase())) {
                    result.add(line);
                }
            }
        } catch (IOException e) {
            return Observable.error(e);
        }

        Collections.sort(result);
        Log.d(TAG, "searchNames: Found " + result.size() + " hits!");
        return Observable.just(result);
    }

    private class SearchResultAdapter2 extends RecyclerView.Adapter<SearchResultViewHolder> {
        private List<String> mResult;

        @Override
        public SearchResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View viewItem = View.inflate(SearchActivity.this,
                    android.R.layout.simple_list_item_1, null);
            return new SearchResultViewHolder(viewItem);
        }

        @Override
        public void onBindViewHolder(SearchResultViewHolder holder, int position) {
            holder.textView.setText(mResult.get(position));

            holder.rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String text = (String) holder.textView.getText();

                    editMesto.setText(text, TextView.BufferType.EDITABLE);

                    isChosen2 = true;
                }
            });
            if(isChosen2 == true) {
                holder.textView.setText("");
            }
            isChosen2 = false;
        }

        @Override
        public int getItemCount() {
            return mResult != null ? mResult.size() : 0;
        }

        public void setSearchResult(List<String> result) {
            mResult = result;
            notifyDataSetChanged();
        }
    }


    private void getAllSchool() {
        List<SchoolRealmModel> result = realmHelper.findAll();
        schoolModel = new ArrayList<>();

        for (SchoolRealmModel sc : result) {
            SchoolModel model = new SchoolModel();

            model.setNaziv(sc.getNaziv());

            schoolModel.add(model);
        }
    }
}
