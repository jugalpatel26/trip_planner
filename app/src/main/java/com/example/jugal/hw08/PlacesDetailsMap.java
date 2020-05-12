package com.example.jugal.hw08;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class PlacesDetailsMap extends AppCompatActivity implements OnMapReadyCallback {
    ArrayList<PlacesClass> listplaces = new ArrayList<>();
    private GoogleMap mMap;
    Double citylat,citylng;
    LatLngBounds.Builder builder;
    CameraUpdate cu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places_details_map);
        setTitle("List of Places");
        citylat = getIntent().getExtras().getDouble("lat");
        citylng = getIntent().getExtras().getDouble("lng");
        listplaces = (ArrayList<PlacesClass>) getIntent().getExtras().getSerializable("place");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        builder = new LatLngBounds.Builder();
        mMap.addMarker(new MarkerOptions().position(new LatLng(citylat,citylng)).title("City Center"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(citylat,citylng)));
        Log.d("size",""+listplaces.size());
        for(int i=0;i<listplaces.size();i++)
        {
            Log.d("lat",""+listplaces.get(i).getLat());
            mMap.addMarker(new MarkerOptions().position(new LatLng(listplaces.get(i).getLat(),listplaces.get(i).getLng())).title(listplaces.get(i).getName()));
            builder.include(new LatLng(listplaces.get(i).getLat(),listplaces.get(i).getLng()));

        }
        builder.build();
        int padding = 200;
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        LatLngBounds bounds = builder.build();
        cu = CameraUpdateFactory.newLatLngBounds(bounds,width,height,padding);
        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {

                mMap.animateCamera(cu);

            }
        });


    }
}
