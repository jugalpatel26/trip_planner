package com.example.jugal.hw08;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PlacesDetails extends AppCompatActivity {
        DatabaseReference places;
      ListView listView;
    ArrayList<PlacesClass> listplaces = new ArrayList<>();
        PlacesClass p;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_places);
        setTitle("List of Places");
        listView= (ListView) findViewById(R.id.listview_places);
        places= FirebaseDatabase.getInstance().getReference().child("places");
        places.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listplaces.clear();
                Log.d("check","insideplaces");
                for(DataSnapshot tasksnapshot:dataSnapshot.getChildren())
                {
                    String id = tasksnapshot.child("tripId").getValue(String.class);
                    Log.d("tripid",id);
                    Log.d("tripidintent",getIntent().getExtras().getString("id"));
                    if(id.equals(getIntent().getExtras().getString("id"))) {
                        p = tasksnapshot.getValue(PlacesClass.class);
                        listplaces.add(p);

                    }

                }
                PlacesDetailsAdapter placesDetailsAdapter = new PlacesDetailsAdapter(PlacesDetails.this,R.layout.listviewplaces,listplaces);
                listView.setAdapter(placesDetailsAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
