package com.example.jugal.hw08;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

public class TripListAdapter extends ArrayAdapter<TripsClass> {
    public TripListAdapter(@NonNull Context context, int resource,@NonNull List<TripsClass> objects) {
        super(context, resource, objects);

    }

    @NonNull
    @Override
    public View getView(int position,@Nullable View convertView,@NonNull final ViewGroup parent) {
        final TripsClass t = getItem(position);
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listviewtrips, parent, false);
        }
        TextView title = (TextView) convertView.findViewById(R.id.textView_triptitle);
        title.setText(t.getTripname());
        ImageButton details = (ImageButton) convertView.findViewById(R.id.imageButton_details);
        details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(parent.getContext(),TripsDetails.class);
                i.putExtra("tripdetails",t);
                parent.getContext().startActivity(i);
            }
        });
        return convertView;

    }
}
