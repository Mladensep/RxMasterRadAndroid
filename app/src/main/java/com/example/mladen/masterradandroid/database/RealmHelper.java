package com.example.mladen.masterradandroid.database;


import android.app.Activity;
import android.util.Log;

import com.example.mladen.masterradandroid.model.SchoolModel;
import com.example.mladen.masterradandroid.model.SchoolRealmModel;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmResults;

public class RealmHelper {

    @Inject Realm realmDb;
    private static final String TAG = "myApp";

    public RealmHelper(Activity activity) {
        ((App) activity.getApplication()).getComponent().inject(this);
    }

    public Observable<Void> addSchoolsRx(List<SchoolModel> list) {

        try {
            realmDb.beginTransaction();
            realmDb.delete(SchoolRealmModel.class);

            for (SchoolModel schoolModel : list) {

                SchoolRealmModel realmModel = realmDb.createObject(SchoolRealmModel.class);

                realmModel.setId(schoolModel.getId());
                realmModel.setMesto(schoolModel.getMesto());
                realmModel.setWww(schoolModel.getWww());
                realmModel.setVrsta(schoolModel.getVrsta());
                realmModel.setPbroj(schoolModel.getPbroj());
                realmModel.setAdresa(schoolModel.getAdresa());
                realmModel.setSuprava(schoolModel.getSuprava());
                realmModel.setFax(schoolModel.getFax());
                realmModel.setGps(schoolModel.getGps());
                realmModel.setOdeljenja(schoolModel.getOdeljenja());
                realmModel.setNaziv(schoolModel.getNaziv());
                realmModel.setOkrug(schoolModel.getOkrug());
                realmModel.setOpstina(schoolModel.getOpstina());
                realmModel.setTel(schoolModel.getTel());
            }
            realmDb.commitTransaction();

            return Observable.empty();

        } catch (Exception e) {
            e.printStackTrace();
            return Observable.empty();
        }
    }


    public RealmResults<SchoolRealmModel> findAll() {
        RealmResults<SchoolRealmModel> result =  realmDb.where(SchoolRealmModel.class).findAll();

        return result;
    }

    public Observable<RealmResults<SchoolRealmModel>> findAllRx() {
        RealmResults<SchoolRealmModel> result =  realmDb.where(SchoolRealmModel.class).findAll();

        return Observable.just(result);
    }

    public Observable<RealmResults<SchoolRealmModel>> filterRx(SchoolRealmModel scModel) {

        RealmResults<SchoolRealmModel> result = realmDb.where(SchoolRealmModel.class)
                .beginsWith("naziv", scModel.getNaziv(), Case.INSENSITIVE)
                .beginsWith("mesto", scModel.getMesto())
                .contains("vrsta", scModel.getVrsta())
                .findAll();

        return Observable.just(result);
    }
}
