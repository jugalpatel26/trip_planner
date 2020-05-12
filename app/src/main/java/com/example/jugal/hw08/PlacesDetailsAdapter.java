package com.example.jugal.hw08;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class PlacesDetailsAdapter extends ArrayAdapter<PlacesClass> {


    public PlacesDetailsAdapter(@NonNull Context context, int resource,@NonNull List<PlacesClass> objects) {
        super(context, resource, objects);
    }


    @NonNull
    @Override
    public View getView(int position,@Nullable View convertView,@NonNull ViewGroup parent) {
        PlacesClass p = getItem(position);
        if(convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listviewplaces, parent, false);
        }
        TextView place = (TextView) convertView.findViewById(R.id.textView_placename);
        place.setText(p.getName());
        return convertView;
    }
}
