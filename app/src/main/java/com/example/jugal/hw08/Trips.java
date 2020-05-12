package com.example.jugal.hw08;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Trips extends AppCompatActivity {

    DatabaseReference databaseReference;
    ArrayList<TripsClass> userTrips = new ArrayList<>();
    ArrayList<TripsClass> otherTrips = new ArrayList<>();
    TripsClass t;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trips);
        setTitle("Trips");

        listView = (ListView) findViewById(R.id.listview_trips);
        databaseReference =FirebaseDatabase.getInstance().getReference().child("trips");

        //Fetch data of all trips from database
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userTrips.clear(); //list of users trips
                otherTrips.clear();//list of otherusers trip
                for(DataSnapshot tasksnapshot:dataSnapshot.getChildren())
                {
                    t = tasksnapshot.getValue(TripsClass.class);
                    if(t.getUser().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
                        userTrips.add(t);
                    }
                    else {
                        otherTrips.add(t);
                    }

                }
                TripListAdapter tripListAdapter = new TripListAdapter(Trips.this,R.layout.listviewtrips,userTrips);
                listView.setAdapter(tripListAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Trips.this, "Database Error.",
                        Toast.LENGTH_SHORT).show();
            }
        });


    }
        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.christmas_menu, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {

            switch (item.getItemId()) {
                case R.id.add_person_menu_item:
                    Log.d("check", "onOptionsItemSelected: add person");
                    Intent intent = new Intent(this, AddTrips.class);
                    startActivity(intent);
                    return true;
                case R.id.logout_menu_item:
                    FirebaseAuth.getInstance().signOut();
                    Intent i = new Intent(Trips.this, MainActivity.class);
                    startActivity(i);
                    Log.d("check", "onOptionsItemSelected: logout");
                    return true;
                case R.id.select:
                    Log.d("check", "Otherusers");
                    TripListAdapter tripListAdapter = new TripListAdapter(Trips.this,R.layout.listviewtrips,otherTrips);
                    listView.setAdapter(tripListAdapter);

                default:
                    return super.onOptionsItemSelected(item);
            }
        }


}
