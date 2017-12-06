package com.example.mladen.masterradandroid.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mladen.masterradandroid.R;
import com.example.mladen.masterradandroid.adapter.CommentAdapter;
import com.example.mladen.masterradandroid.model.CommentModel;
import com.example.mladen.masterradandroid.model.SchoolModel;
import com.example.mladen.masterradandroid.retrofit.RestApi;
import com.example.mladen.masterradandroid.retrofit.RestClient;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;


public class CommentSchoolFragment extends Fragment {

    @BindView(R.id.editText) EditText editText;
    @BindView(R.id.list) ListView listView;
    @BindView(R.id.submitButton) Button submitButton;

    private String getComment;
    private int id;
    private CommentModel commentModel;
    private RestApi apiService;
    private CompositeDisposable compositeDisposable;
    private ArrayList<CommentModel> list;
    private CommentAdapter adapter;
    private ConnectivityManager conMgr;

    private String mailfb;
    private String namefb;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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
            submitButton.setBackgroundColor(getResources().getColor(R.color.tab_background));
            submitButton.setTextColor(getResources().getColor(R.color.white));
        } else {
            submitButton.setBackgroundColor(getResources().getColor(R.color.disable_button));
            submitButton.setTextColor(getResources().getColor(R.color.light_gray2));
        }

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

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
            .subscribeWith(new DisposableObserver<List<CommentModel>>() {
                @Override
                public void onNext(List<CommentModel> commentModels) {
                    int xa = commentModels.size()*137;
                    listView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, xa));

                    list = new ArrayList<>(commentModels);
                    adapter = new CommentAdapter(getActivity(), list);
                    listView.setAdapter(adapter);
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

    @OnClick(R.id.submitButton)
    public void sendComment() {
        if(mailfb != "") {
            if (conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                    || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {

                getComment = editText.getText().toString();
                String finalComment = namefb + ": " +getComment;

                commentModel.setCom(finalComment);
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

            } else if (conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.DISCONNECTED
                    || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.DISCONNECTED) {

                Toast.makeText(getActivity(), "Нема интернет конекције. ", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getActivity(), "Морате бити пријављени", Toast.LENGTH_SHORT).show();
        }
    }
}
