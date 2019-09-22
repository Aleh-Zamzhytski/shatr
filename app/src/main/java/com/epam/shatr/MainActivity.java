package com.epam.shatr;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;
import android.app.Dialog;
import android.widget.Button;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

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
    public static final Map<String, Coordinates> CITIES = new HashMap<>();
    static {
        CITIES.put("Минск", new Coordinates(53.897872, 27.554944));
        CITIES.put("Витебск", new Coordinates(55.185576, 30.203791));
        CITIES.put("Могилев", new Coordinates(53.895155, 30.317134));
        CITIES.put("Гомель", new Coordinates(52.429407, 30.986640));
        CITIES.put("Брест", new Coordinates(52.098500, 23.713208));
        CITIES.put("Гродно", new Coordinates(53.669334, 23.821969));
    }

    private String city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDialog = new Dialog(this);
        locatorService = new LocatorService(getApplicationContext());

        setCitySpinner();
    }

    protected void setCitySpinner() {
        Spinner spinner = findViewById(R.id.citySpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, new ArrayList<>(CITIES.keySet()));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        final TextView selection = (TextView) findViewById(R.id.selection);
        OnItemSelectedListener itemSelectedListener = new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                listVisit.clear();
                viewed.clear();
                city = (String) parent.getItemAtPosition(position);
                selection.setText(city);

                ShowVisit(CITIES.get(city));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
        spinner.setOnItemSelectedListener(itemSelectedListener);
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

    public void ShowPopup(final Visit place) {
        TextView txtclose;
        TextView question;
        Button btnNo;
        Button btnYes;
        myDialog.setContentView(R.layout.custompopup);
        txtclose = (TextView) myDialog.findViewById(R.id.txtclose);
        txtclose.setText("M");
        question = myDialog.findViewById(R.id.question);
        question.setText("Хотите ли Вы посетить " + place.getPlace().getName() + "?");
        btnNo = (Button) myDialog.findViewById(R.id.btnNo);
        btnYes = (Button) myDialog.findViewById(R.id.btnYes);
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
}
