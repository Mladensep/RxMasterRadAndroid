package com.example.mladen.masterradandroid.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mladen.masterradandroid.R;
import com.example.mladen.masterradandroid.adapter.DataAdapter;
import com.example.mladen.masterradandroid.database.RealmHelper;
import com.example.mladen.masterradandroid.model.SchoolModel;
import com.example.mladen.masterradandroid.model.SchoolRealmModel;
import com.example.mladen.masterradandroid.model.SearchModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;


public class SearchResultFragment extends Fragment{

    private CompositeDisposable disposable;
    private RecyclerView recyclerView;
    private DataAdapter dataAdapter;
    private SchoolRealmModel sendQuery;
    private RealmHelper realmHelper;
    private SearchModel sc;
    private ArrayList<SchoolModel> schoolModel;
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search_result, container, false);

        disposable = new CompositeDisposable();

        sc = new SearchModel();

        Bundle bundle = getActivity().getIntent().getExtras();
        sc = bundle.getParcelable("dataa");

        sendQuery = new SchoolRealmModel();

        realmHelper = new RealmHelper(getActivity());
        initRecyclerView();

        String naziv = sc.getNaziv();
        String mesto = sc.getMesto();
        int samoOsnovna = sc.getSamoOsnovne();
        int samoSrednje = sc.getSamoSrednje();

        if(samoOsnovna == 1) {
            sendQuery.setNaziv(naziv);
            sendQuery.setMesto(mesto);
            sendQuery.setVrsta("Основна школа");
            //    nadji osnovne
        } else if(samoSrednje == 1) {
            sendQuery.setNaziv(naziv);
            sendQuery.setMesto(mesto);
            sendQuery.setVrsta("Средња школа");
            //    nadji srednje
        } else {
            sendQuery.setNaziv(naziv);
            sendQuery.setMesto(mesto);
        }

        disposable.add(realmHelper.filterRx(sendQuery)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(new DisposableObserver<List<SchoolRealmModel>>() {
                @Override
                public void onNext(List<SchoolRealmModel> result) {
                    schoolModel = new ArrayList<>();

                    for (SchoolRealmModel scModel : result) {
                        SchoolModel model = new SchoolModel();

                        model.setNaziv(scModel.getNaziv());
                        model.setId(scModel.getId());
                        model.setMesto(scModel.getMesto());
                        model.setWww(scModel.getWww());
                        model.setVrsta(scModel.getVrsta());
                        model.setPbroj(scModel.getPbroj());
                        model.setAdresa(scModel.getAdresa());
                        model.setSuprava(scModel.getSuprava());
                        model.setFax(scModel.getFax());
                        model.setGps(scModel.getGps());
                        model.setOdeljenja(scModel.getOdeljenja());
                        model.setNaziv(scModel.getNaziv());
                        model.setOkrug(scModel.getOkrug());
                        model.setOpstina(scModel.getOpstina());
                        model.setTel(scModel.getTel());

                        schoolModel.add(model);
                    }

                    dataAdapter = new DataAdapter(schoolModel);
                    recyclerView.setAdapter(dataAdapter);
                }

                @Override
                public void onError(Throwable e) {
                    Toast.makeText(getActivity(), "Грешка " , Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onComplete() {

                }
            }));

        return view;
    }

    @Override
    public void onDestroy() {
        if (!disposable.isDisposed()) {
            disposable.dispose();
        }
        super.onDestroy();
    }

    private void initRecyclerView() {
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
    }
}
