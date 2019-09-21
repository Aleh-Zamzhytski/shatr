package com.epam.shatr;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.epam.shatr.entity.Location;
import com.epam.shatr.service.LocatorService;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
