package com.example.jugal.hw08;

import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddTrips extends AppCompatActivity {
    TripsClass trip = new TripsClass();
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trips);
        setTitle("Add Trips");

        databaseReference = FirebaseDatabase.getInstance().getReference();
        final EditText tripname = (EditText) findViewById(R.id.edittext_tripname);
        final Button addplaces = (Button) findViewById(R.id.button_selectplaces);
        final TextView addplacestextview = (TextView) findViewById(R.id.textView_tplaces);

        addplaces.setVisibility(View.INVISIBLE);
        addplacestextview.setVisibility(View.INVISIBLE);

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);



        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES)
                .build();
        autocompleteFragment.setFilter(typeFilter);
        final EditText date= (EditText) findViewById(R.id.editText_date);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                trip.setCityid(place.getId());
                trip.setCityname(place.getName().toString());
                LatLng latLng = place.getLatLng();
                trip.setLat(latLng.latitude);
                trip.setLng(latLng.longitude);

            }

            @Override
            public void onError(Status status) {
                Log.d("status",""+status);
            }
        });

        findViewById(R.id.button_addtrip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trip.setTripname(tripname.getText().toString());
                trip.setDate(date.getText().toString());
                trip.setId(databaseReference.child("trips").push().getKey());
                trip.setUser(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                databaseReference.child("trips").child(trip.getId()).setValue(trip);
                addplaces.setVisibility(View.VISIBLE);
                String temp = addplacestextview.getText().toString();
                addplacestextview.setText(temp+" "+trip.getCityname()+":");
                addplacestextview.setVisibility(View.VISIBLE);
                Toast.makeText(AddTrips.this, "Please select places to visit", Toast.LENGTH_LONG).show();
                }
        });

        addplaces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AddTrips.this,PlacesType.class);
                i.putExtra("id",trip.getId());
                i.putExtra("lat",trip.getLat());
                i.putExtra("lng",trip.getLng());
                startActivity(i);
            }
        });

    }

}
