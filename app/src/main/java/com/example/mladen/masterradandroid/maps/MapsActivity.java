package com.example.mladen.masterradandroid.maps;


import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;


public class MapsActivity extends Fragment implements OnMapReadyCallback {

    private ClusterManager<MyItem> mClusterManager;
    private GoogleMap mMap;
    private SchoolRealmModel sendQuery;
    private RealmHelper realmHelper;
    private SearchModel sc;
    private ArrayList<SchoolModel> schoolModel;
    private CompositeDisposable disposable;


    private List<String> nameList = new ArrayList<>();
    private List<String> positionAs = new ArrayList<>();

    private String naziv = "";
    private String mesto = "";
    private int samoOsnovna = 0;
    private int samoSrednje = 0;
    private Bundle bundle;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_maps, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        sc = new SearchModel();
        disposable = new CompositeDisposable();

        bundle = getActivity().getIntent().getExtras();

        if(bundle != null) {
            sc = bundle.getParcelable("dataa");

            if (sc != null) {
                bundle = null;

                naziv = sc.getNaziv();
                mesto = sc.getMesto();
                samoOsnovna = sc.getSamoOsnovne();
                samoSrednje = sc.getSamoSrednje();
            }
        }

        sendQuery = new SchoolRealmModel();

        sendQuery = new SchoolRealmModel();
        realmHelper = new RealmHelper(getActivity());

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
                    }

                    @Override
                    public void onError(Throwable e) {

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

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng center = new LatLng(44.10, 20.90);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(center, 7));

        mClusterManager = new ClusterManager<MyItem>(getActivity(), mMap);

        mMap.setOnCameraChangeListener(mClusterManager);


        for (SchoolModel s: schoolModel) {
            try{
                Double lat = Double.parseDouble(s.getGps().split(",")[0]);
                Double lng = Double.parseDouble(s.getGps().split(",")[1]);

                String name = s.getNaziv();
                nameList.add(name);

                String poz = s.getId();
                positionAs.add(poz);

                MyItem offsetItem = new MyItem(lat, lng);
                mClusterManager.addItem(offsetItem);

            }catch (Exception e){

            }
        }

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker arg0) {

                LatLng poz = arg0.getPosition();

                String clickPosition = String.valueOf(poz);

                for (SchoolModel sm : schoolModel) {

                    String a = "lat/lng: ("+ sm.getGps() +")";

                    if (clickPosition.equals(a)) {
                        Toast.makeText(getActivity(), sm.getNaziv(), Toast.LENGTH_SHORT).show();
                    }
                }
                return true;
            }
        });
    }

    public class MyItem implements ClusterItem {
        private final LatLng mPosition;


        public MyItem(double lat, double lng) {
            mPosition = new LatLng(lat, lng);
        }

        @Override
        public LatLng getPosition() {
            return mPosition;
        }
    }
}