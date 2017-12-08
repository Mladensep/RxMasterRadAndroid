package com.example.mladen.masterradandroid.fragments;


import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mladen.masterradandroid.R;
import com.example.mladen.masterradandroid.adapter.DataAdapter;
import com.example.mladen.masterradandroid.database.RealmHelper;
import com.example.mladen.masterradandroid.model.SchoolModel;
import com.example.mladen.masterradandroid.model.SchoolRealmModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class AllSchoolsFragment extends Fragment {
    private DataAdapter dataAdapter;
    private RealmHelper realmHelper;
    private ArrayList<SchoolModel> schoolModel;

    @BindView(R.id.recycler_view) RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_all_schools, container, false);

        ButterKnife.bind(this, view);

        realmHelper = new RealmHelper(getActivity());

            showAllSchool()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe();

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    private void initRecyclerView() {
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(manager);
    }

    public Observable<Boolean> showAllSchool() {

        List<SchoolRealmModel> result = realmHelper.findAll();
        schoolModel = new ArrayList<>();

        for(SchoolRealmModel sc : result) {
            SchoolModel model = new SchoolModel();

            model.setNaziv(sc.getNaziv());
            model.setId(sc.getId());
            model.setMesto(sc.getMesto());
            model.setWww(sc.getWww());
            model.setVrsta(sc.getVrsta());
            model.setPbroj(sc.getPbroj());
            model.setAdresa(sc.getAdresa());
            model.setSuprava(sc.getSuprava());
            model.setFax(sc.getFax());
            model.setGps(sc.getGps());
            model.setOdeljenja(sc.getOdeljenja());
            model.setNaziv(sc.getNaziv());
            model.setOkrug(sc.getOkrug());
            model.setOpstina(sc.getOpstina());
            model.setTel(sc.getTel());

            schoolModel.add(model);
        }

        return Observable.fromCallable(this::startRecyclerView);
    }

    private boolean startRecyclerView() {
        initRecyclerView();
        dataAdapter = new DataAdapter(schoolModel);
        recyclerView.setAdapter(dataAdapter);

        return true;
    }
}