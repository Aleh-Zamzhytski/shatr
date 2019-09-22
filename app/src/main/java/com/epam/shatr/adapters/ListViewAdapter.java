package com.epam.shatr.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.epam.shatr.R;
import com.epam.shatr.entity.Visit;
import android.content.Context;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;


public class ListViewAdapter extends BaseAdapter {
    private final Context context;
    private final List<Visit> values;
    private final LayoutInflater inflater;

    public ListViewAdapter(Context context, List<Visit> values) {
        //super(context, );
        this.context = context;
        this.values = values;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public Visit getItem(int index){
        return values.get(index);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    @Override
    public int getCount(){
        return values.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rowView =  inflater.inflate(R.layout.rowlayout,  null);

        TextView time = (TextView) rowView.findViewById(R.id.time);
        TextView name = (TextView) rowView.findViewById(R.id.name);
        TextView desc = (TextView) rowView.findViewById(R.id.desc);

        Visit visitObj = values.get(position);

        DateTimeFormatter format = DateTimeFormat.forPattern("dd.MM.yyyy HH:mm");

        time.setText(visitObj.getStartTime().toString(format) + " - " + visitObj.getEndTime().toString(format));
        name.setText(visitObj.getPlace().getName());
        desc.setText(visitObj.getPlace().getDescription());

        return rowView;
    }
}