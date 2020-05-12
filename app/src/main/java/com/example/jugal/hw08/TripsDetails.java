package com.example.jugal.hw08;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TripsDetails extends AppCompatActivity {
    ArrayList<PlacesClass> listplaces = new ArrayList<>();
    PlacesClass p;
    DatabaseReference place;
    TripsClass tripdetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trips_details);
        setTitle("Trips Details");
        tripdetails = new TripsClass();
        tripdetails = (TripsClass) getIntent().getExtras().getSerializable("tripdetails");
        place= FirebaseDatabase.getInstance().getReference().child("places");
        place.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listplaces.clear();
                Log.d("check","insideplaces");
                for(DataSnapshot tasksnapshot:dataSnapshot.getChildren())
                {
                    String id = tasksnapshot.child("tripId").getValue(String.class);
                    Log.d("tripid",id);
                    if(id.equals(tripdetails.getId())) {
                        p = tasksnapshot.getValue(PlacesClass.class);
                        listplaces.add(p);

                    }

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        TextView name = (TextView) findViewById(R.id.textView_dtripname);
        TextView city = (TextView) findViewById(R.id.textView_ddcity);
        TextView date = (TextView) findViewById(R.id.textView_ddate);
        TextView user = (TextView) findViewById(R.id.textView_duser);
        name.setText(tripdetails.getTripname());
        city.setText(tripdetails.getCityname());
        date.setText(tripdetails.getDate());
        user.setText(tripdetails.getUser());
        findViewById(R.id.button_showplace).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TripsDetails.this,PlacesDetails.class);
                i.putExtra("id",tripdetails.getId());
                startActivity(i);
            }
        });
        findViewById(R.id.button_showplacegoogle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TripsDetails.this,PlacesDetailsMap.class);
                i.putExtra("id",tripdetails.getId());
                i.putExtra("lat",tripdetails.getLat());
                i.putExtra("lng",tripdetails.getLng());
                i.putExtra("place",listplaces);
                startActivity(i);
            }
        });

    }
}
