package com.example.mladen.masterradandroid.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mladen.masterradandroid.R;
import com.example.mladen.masterradandroid.maps.MapsSingleSchoolActivity;
import com.example.mladen.masterradandroid.model.SchoolModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailSchoolFragment extends Fragment{

    @BindView(R.id.naziv) TextView naziv;
    @BindView(R.id.mesto) TextView mesto;
    @BindView(R.id.opstina) TextView opstina;
    @BindView(R.id.adresa) TextView adresa;
    @BindView(R.id.pbroj) TextView pbroj;
    @BindView(R.id.okrug) TextView okrug;
    @BindView(R.id.suprava) TextView suprava;
    @BindView(R.id.www) TextView www;
    @BindView(R.id.tel) TextView tel;
    @BindView(R.id.fax) TextView fax;
    @BindView(R.id.vrsta) TextView vrsta;
    @BindView(R.id.odeljenja) TextView odeljenja;
    @BindView(R.id.gps) TextView gps;

    private SchoolModel sc;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_school, container, false);

        ButterKnife.bind(this, view);

        Bundle bundle = getActivity().getIntent().getExtras();
        sc = bundle.getParcelable("data");

        naziv.setText(sc.getNaziv());
        mesto.setText(sc.getMesto());
        opstina.setText(sc.getOpstina());
        adresa.setText(sc.getAdresa());
        pbroj.setText(sc.getPbroj());
        okrug.setText(sc.getOkrug());
        suprava.setText(sc.getSuprava());
        www.setText(sc.getWww());
        tel.setText(sc.getTel());
        fax.setText(sc.getFax());
        vrsta.setText(sc.getVrsta());
        odeljenja.setText(sc.getOdeljenja());

        gps.setText("Прикажи на мапи");

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @OnClick(R.id.gps)
    public void showSchool() {
        Intent intent =  new Intent(getActivity(), MapsSingleSchoolActivity.class);

        Bundle bundle = new Bundle();
        bundle.putParcelable("data", sc);
        intent.putExtras(bundle);

        getActivity().startActivity(intent);
    }
}
