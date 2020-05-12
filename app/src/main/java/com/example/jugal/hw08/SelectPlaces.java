package com.example.jugal.hw08;

import android.content.Context;
import android.os.AsyncTask;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SelectPlaces extends AppCompatActivity {
        ArrayList<PlacesClass> places = new ArrayList<>();
        DatabaseReference trip;
        ListView listView;
        String tripid;
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_places);
        setTitle("Select Places");
        tripid =getIntent().getExtras().getString("id");
        trip = databaseReference.child("trips").child(tripid);
        listView = (ListView) findViewById(R.id.listview_places);
        double lat=getIntent().getExtras().getDouble("lat");
        double lng=getIntent().getExtras().getDouble("lng");
        Log.d("lat",""+lat);
        Log.d("lng",""+lng);
        new GetDataAsync().execute("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+lat+","+lng+"&radius=1609.34&type="+getIntent().getExtras().getString("type")+"&key=AIzaSyDJAz-1VmJOcVoC8ZwfZbYsmjPBGJvAkFA");

    }

    private class GetDataAsync extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection connection = null;
            String result = null;
            try {
                URL url = new URL(params[0]);
                Log.d("check",""+url);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    result = IOUtils.toString(connection.getInputStream(), "UTF-8");
                    JSONObject root = new JSONObject(result);
                    JSONArray results = root.getJSONArray("results");

                    for(int i=0;i<results.length();i++){
                        JSONObject resulroot = results.getJSONObject(i);
                        PlacesClass p = new PlacesClass();
                        p.setName(resulroot.getString("name"));
                        p.setTripId(tripid);
                        p.setLat(resulroot.getJSONObject("geometry").getJSONObject("location").getDouble("lat"));
                        p.setLng(resulroot.getJSONObject("geometry").getJSONObject("location").getDouble("lng"));
                        p.setPlaceid(resulroot.getString("place_id"));
                        places.add(p);

                    }

                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                Log.d("demo", result);
                if(places.size()==0){
                    Toast.makeText(SelectPlaces.this, "Not Found", Toast.LENGTH_LONG).show();
                }
                placeslistviewadapter placeslistviewadapter = new placeslistviewadapter(SelectPlaces.this,R.layout.listviewplaces,places);
                listView.setAdapter(placeslistviewadapter);

            } else {
                Log.d("demo", "null result");
            }
        }
    }
    public class placeslistviewadapter extends ArrayAdapter<PlacesClass> {
        Context context;
        int count;
        DatabaseReference placecount;
        public placeslistviewadapter(@NonNull Context context, int resource, @NonNull List<PlacesClass> objects) {
            super(context, resource, objects);
            this.context =context;




        }


        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            final PlacesClass p = getItem(position);
            ViewHolder viewHolder;
            placecount = trip.child("placecount");
            placecount.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String a = dataSnapshot.getValue().toString();
                    count = Integer.parseInt(a);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            if(convertView == null){
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.listviewplaces, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.textViewName = (TextView) convertView.findViewById(R.id.textView_placename);
                viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
                convertView.setTag(viewHolder);
            } else{
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.textViewName.setText(p.getName());
            viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean checked = ((CheckBox) v).isChecked();
                    if (checked) {
                        count++;
                        if(count<=15){
                            placecount.setValue(count);
                            p.setId(databaseReference.child("places").push().getKey());
                            databaseReference.child("places").child(p.getId()).setValue(p);
                            Toast.makeText(context, "Checked and saved", Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(context, "Selected more then 15 places", Toast.LENGTH_LONG).show();
                        }


                    } else {
                        Toast.makeText(context, "Un-Checked and saved", Toast.LENGTH_LONG).show();
                        databaseReference.child("places").child(p.getId()).removeValue();
                        count--;
                        placecount.setValue(count);

                    }
                }
            });
            return convertView;
        }
        private class ViewHolder{
            TextView textViewName;
            CheckBox checkBox;


        }
    }

}
