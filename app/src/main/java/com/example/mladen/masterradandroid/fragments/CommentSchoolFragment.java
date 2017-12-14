package com.example.mladen.masterradandroid.fragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Toast;

import com.example.mladen.masterradandroid.R;
import com.example.mladen.masterradandroid.adapter.CommentDataAdapter;
import com.example.mladen.masterradandroid.model.CommentModel;
import com.example.mladen.masterradandroid.model.SchoolModel;
import com.example.mladen.masterradandroid.retrofit.RestApi;
import com.example.mladen.masterradandroid.retrofit.RestClient;
import com.jakewharton.rxbinding2.view.RxView;


import java.util.ArrayList;
import java.util.Calendar;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

import static com.example.mladen.masterradandroid.fragments.DialogComment.DATEPICKER_FRAGMENT;


public class CommentSchoolFragment extends Fragment {

    @BindView(R.id.button) FloatingActionButton button;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;

    private int id;
    private CommentModel commentModel;
    private RestApi apiService;
    private CompositeDisposable compositeDisposable;
    private ArrayList<CommentModel> list;
    private ConnectivityManager conMgr;

    private CommentDataAdapter dataAdapter;

    private String mailfb;
    private String namefb;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_comment_school, container, false);

        ButterKnife.bind(this, view);

        Bundle bundle = getActivity().getIntent().getExtras();
        SchoolModel sc = bundle.getParcelable("data");

        sharedPref()
                .subscribeOn(Schedulers.io())
                .subscribe();

        commentModel = new CommentModel();

        id = Integer.parseInt(sc.getId());

        conMgr = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        if ( conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED ) {

            apiService = RestClient.getClient();
            compositeDisposable = new CompositeDisposable();

            getCommentData();

        }
        else if ( conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.DISCONNECTED
                || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.DISCONNECTED) {

            Toast.makeText(getActivity(), "Нема интернет конекције. " , Toast.LENGTH_SHORT).show();
        }

        if(mailfb != "") {
            button.show();
        } else {
            button.hide();
        }

        RxView.clicks(button)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        DialogComment dialog = new DialogComment();

// optionally pass arguments to the dialog fragment
//        Bundle args = new Bundle();
//        args.putString("pickerStyle", "fancy");
//        dialog.setArguments(args);
// setup link back to use and display

                        dialog.setTargetFragment(CommentSchoolFragment.this, DATEPICKER_FRAGMENT);
                        dialog.show(getFragmentManager().beginTransaction(), "MyProgressDialog");
                    }
                });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case DATEPICKER_FRAGMENT:
                if (resultCode == Activity.RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    String titleCom = bundle.getString("title", "");
                    String contentCom = bundle.getString("content", "");

                    String time = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());

                    commentModel.setTitleComment(titleCom);
                    commentModel.setContentComment(contentCom);
                    commentModel.setFbName(namefb);
                    commentModel.setDateTime(time);
                    commentModel.setSchool_id(id);

                    compositeDisposable.add(apiService.postOrder(commentModel)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeWith(new DisposableObserver<String>() {

                                @Override
                                public void onNext(String value) {
                                }

                                @Override
                                public void onError(Throwable e) {
                                    Toast.makeText(getActivity(), "Није послато", Toast.LENGTH_SHORT).show();

                                    e.printStackTrace();
                                }

                                @Override
                                public void onComplete() {
                                    Toast.makeText(getActivity(), "Успешно послато", Toast.LENGTH_SHORT).show();

                                    getCommentData();
                                }
                            }));
                } else if (resultCode == Activity.RESULT_CANCELED) {

                }
                break;
        }
    }

//    @OnClick(R.id.button)
//    public void openDialog() {
//
//        DialogComment dialog = new DialogComment();
//
//// optionally pass arguments to the dialog fragment
////        Bundle args = new Bundle();
////        args.putString("pickerStyle", "fancy");
////        dialog.setArguments(args);
//// setup link back to use and display
//
//        dialog.setTargetFragment(this, DATEPICKER_FRAGMENT);
//        dialog.show(getFragmentManager().beginTransaction(), "MyProgressDialog");
//    }

    public Observable<Void> sharedPref() {
        SharedPreferences sharedPref = getActivity().getSharedPreferences("facebook", Context.MODE_PRIVATE);

        mailfb = sharedPref.getString("emailData", "");
        namefb = sharedPref.getString("nameData", "");

        return Observable.empty();
    }

    @Override
    public void onDestroy() {
        if (!compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
        super.onDestroy();
    }

    private void getCommentData() {
        compositeDisposable.add(apiService.getAllComment(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(new DisposableObserver<ArrayList<CommentModel>>() {
                @Override
                public void onNext(ArrayList<CommentModel> commentModels) {
                    initRecyclerView();
                    dataAdapter = new CommentDataAdapter(commentModels);
                    recyclerView.setAdapter(dataAdapter);
                }

                @Override
                public void onError(Throwable e) {
                    Toast.makeText(getActivity(), "Коментари нису добављени. ", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onComplete() {
                    Toast.makeText(getActivity(), "Добављени коментари. " , Toast.LENGTH_SHORT).show();
                }
            }));
    }

    private void initRecyclerView() {
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(manager);
    }
}
