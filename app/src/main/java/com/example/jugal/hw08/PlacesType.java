package com.example.jugal.hw08;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class PlacesType extends AppCompatActivity {
    String[] types = {"museum","amusement parks","aquarium","car rental","airport","police station","city hall","parking"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places_type);
        setTitle("Type of Places");
        final ListView listView = (ListView) findViewById(R.id.listview_types);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,android.R.id.text1,types);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(PlacesType.this,SelectPlaces.class);
                i.putExtra("type",types[position]);
                i.putExtra("id",getIntent().getExtras().getString("id"));
                i.putExtra("lat",getIntent().getExtras().getDouble("lat"));
                i.putExtra("lng",getIntent().getExtras().getDouble("lng"));
                startActivity(i);
            }
        });
        findViewById(R.id.button_done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PlacesType.this,Trips.class);
                startActivity(i);
                finish();
            }
        });

    }
}
