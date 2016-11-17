package com.example.administrator.lifelogger;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

public class LifeMap extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private PolylineOptions polylineOptions;
    private ArrayList<LatLng> arrayPoints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_life_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Intent intent = getIntent();
        String category = intent.getStringExtra("category");

        String str = "";
        SQLiteDatabase db;
        db = SQLiteDatabase.openDatabase("/data/data/com.example.administrator.lifelogger/myloggerDB",
                null, SQLiteDatabase.CREATE_IF_NECESSARY);

        Cursor cursor = db.rawQuery("select * from logger", null);
        while (cursor.moveToNext()) {
            if(cursor.getString(5).equals(category)==true) {
                LatLng location = new LatLng(cursor.getDouble(1), cursor.getDouble(2));
                mMap.addMarker(new MarkerOptions().position(location).title(cursor.getInt(0) + ". " + cursor.getString(6)));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(location));

            }
        }
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
        mMap.animateCamera(zoom);
    }
}
