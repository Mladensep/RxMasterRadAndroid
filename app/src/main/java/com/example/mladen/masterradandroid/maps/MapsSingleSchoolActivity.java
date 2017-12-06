package com.example.mladen.masterradandroid.maps;

import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.mladen.masterradandroid.R;
import com.example.mladen.masterradandroid.model.SchoolModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MapsSingleSchoolActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private SchoolModel sc;
    @BindView(R.id.toolbar_title) TextView toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_single_school);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Bundle bundle = this.getIntent().getExtras();
        sc = bundle.getParcelable("data");

        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setText(sc.getNaziv());
        }
    }

    @OnClick(R.id.back_icon)
    public void back() {
        finish();
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

        Double lat = Double.parseDouble(sc.getGps().split(",")[0]);
        Double lng = Double.parseDouble(sc.getGps().split(",")[1]);

        LatLng center = new LatLng(lat, lng);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(center, 15));

        LatLng location = new LatLng(lat, lng);
        //mMap.addMarker(new MarkerOptions().position(sydney).title(sc.getNaziv()));
        mMap.addMarker(new MarkerOptions().position(location));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(location));

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                Toast.makeText(MapsSingleSchoolActivity.this, sc.getNaziv(), Toast.LENGTH_SHORT).show();

                return false;
            }
        });
    }
}
