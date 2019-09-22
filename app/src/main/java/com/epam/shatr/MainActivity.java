package com.epam.shatr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;
import android.app.Dialog;
import android.widget.Button;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import com.epam.shatr.adapters.ListViewAdapter;
import com.epam.shatr.entity.Coordinates;
import com.epam.shatr.entity.Place;
import com.epam.shatr.entity.Visit;
import com.epam.shatr.service.LocatorService;

import org.joda.time.LocalDateTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    Dialog myDialog;
    private LocatorService locatorService;
    List<Visit> listVisit = new ArrayList<>();
    List<Place> viewed = new ArrayList<>();
    private ListViewAdapter listViewAdapter;
    private String city;
    private String cityTo;
    public static final Map<String, Coordinates> CITIES = new HashMap<>();

    static {
        CITIES.put("", new Coordinates(0,0));
        CITIES.put("Минск", new Coordinates(53.897872, 27.554944));
        CITIES.put("Витебск", new Coordinates(55.185576, 30.203791));
        CITIES.put("Могилев", new Coordinates(53.895155, 30.317134));
        CITIES.put("Гомель", new Coordinates(52.429407, 30.986640));
        CITIES.put("Брест", new Coordinates(52.098500, 23.713208));
        CITIES.put("Гродно", new Coordinates(53.669334, 23.821969));
    }

    private ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        listViewAdapter = new ListViewAdapter(MainActivity.this, listVisit);

        listView = findViewById(R.id.listView);
        listView.setAdapter(listViewAdapter);



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String coords = listVisit.get(position).getPlace().getCoords().getStringRepresentation();

                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?daddr=" + coords));
                startActivity(intent);
            }
        });


        myDialog = new Dialog(this);
        locatorService = new LocatorService(getApplicationContext());

        setCitySpinner();
    }

    protected void setCitySpinner() {
        final Spinner spinner = findViewById(R.id.citySpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, new ArrayList<>(CITIES.keySet()));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);
        final Spinner spinner2 = findViewById(R.id.spinner);

        OnItemSelectedListener itemSelectedListener = new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                listVisit.clear();
                viewed.clear();
                city = (String) parent.getItemAtPosition(position);
                if(city.equals("")){
                    return;
                }

                ArrayList<String> arList = new ArrayList<>();
                for(String cityInList : CITIES.keySet()){
                    if(cityInList.equals("") || !cityInList.equals(city)){
                        arList.add(cityInList);
                    }
                }
                ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, arList);
                spinner2.setVisibility(Spinner.VISIBLE);
                spinner2.setAdapter(adapter2);

                ShowVisit(CITIES.get(city));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };

        OnItemSelectedListener itemSelectedListener2 = new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cityTo = (String) parent.getItemAtPosition(position);

                if(cityTo.equals("")){
                    return;
                }

                listVisit.clear();
                viewed.clear();
                ShowVisit(CITIES.get(city), CITIES.get(cityTo));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };

        spinner.setOnItemSelectedListener(itemSelectedListener);
        spinner2.setOnItemSelectedListener(itemSelectedListener2);
    }

    public void ShowVisit(Coordinates coordinates){
        LocalDateTime visitTime;
        if (listVisit.isEmpty()) {
            visitTime = LocalDateTime.now();
        } else {
            visitTime = listVisit.get(listVisit.size() - 1).getEndTime();
        }

        List<Visit> visits = locatorService.getPlacesByLocationAndTime(coordinates, visitTime, viewed);
        if (!visits.isEmpty()) {
            ShowPopup(visits.get(0));
        }
    }
    public void ShowVisit(Coordinates coorsFrom, Coordinates coordsTo){
        LocalDateTime visitTime;
        if (listVisit.isEmpty()) {
            visitTime = LocalDateTime.now();
        } else {
            visitTime = listVisit.get(listVisit.size() - 1).getEndTime();
        }

        List<Visit> visits = locatorService.getPlacesByPath(coorsFrom, coordsTo, visitTime, viewed);
        if (!visits.isEmpty()) {
            ShowPopup(visits.get(0));
        }
    }

    public void ShowPopup(final Visit place) {
        myDialog.setContentView(R.layout.custompopup);
        TextView txtclose = (TextView) myDialog.findViewById(R.id.txtclose);
        txtclose.setText("X");
        TextView question = myDialog.findViewById(R.id.question);
        question.setText("Хотите ли Вы посетить " + place.getPlace().getName() + "?");
        TextView desc = myDialog.findViewById(R.id.description);
        desc.setText(place.getPlace().getDescription());
        Button btnNo = (Button) myDialog.findViewById(R.id.btnNo);
        Button btnYes = (Button) myDialog.findViewById(R.id.btnYes);
        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View sender) {
                myDialog.dismiss();
                onEnterButtonNo(sender, place);
            }
        });
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View sender) {
                myDialog.dismiss();
                onEnterButtonYes(sender, place);
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

    private void onEnterButtonYes(View sender, Visit place) {
        listVisit.add(place);
        viewed.add(place.getPlace());
        ListViewUpdate();
        ShowVisit(place.getPlace().getCoords());
    }

    private void onEnterButtonNo(View sender, Visit place) {
        viewed.add(place.getPlace());
        if (listVisit.isEmpty()) {
            ShowVisit(CITIES.get(city));
        } else {
            ShowVisit(listVisit.get(listVisit.size() - 1).getPlace().getCoords());
        }
    }
    private void ListViewUpdate(){
        listViewAdapter.notifyDataSetChanged();
    }
}
