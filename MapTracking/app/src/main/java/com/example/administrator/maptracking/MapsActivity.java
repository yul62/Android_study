package com.example.administrator.maptracking;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private PolylineOptions polylineOptions;
    private ArrayList<LatLng> arrayPoints;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        arrayPoints = new ArrayList<LatLng>();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        String str = "";
        SQLiteDatabase db;
        db = SQLiteDatabase.openDatabase("/data/data/com.example.administrator.maptracking/myloggerDB",
                null, SQLiteDatabase.CREATE_IF_NECESSARY);

        Cursor cursor = db.rawQuery("select * from logger", null);

        while (cursor.moveToNext()) {
            LatLng loaction = new LatLng(cursor.getDouble(1), cursor.getDouble(2));
            mMap.addMarker(new MarkerOptions().position(loaction).title(cursor.getInt(0) + ". " + cursor.getString(3)));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(loaction));

            polylineOptions = new PolylineOptions();
            polylineOptions.color(Color.RED);
            polylineOptions.width(5);
            arrayPoints.add(loaction);
            polylineOptions.addAll(arrayPoints);
            mMap.addPolyline(polylineOptions);
        }
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
        mMap.animateCamera(zoom);
    }
}