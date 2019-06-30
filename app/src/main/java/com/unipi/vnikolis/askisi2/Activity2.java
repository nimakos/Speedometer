package com.unipi.vnikolis.askisi2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.github.anastr.speedviewlib.DeluxeSpeedView;

public class Activity2 extends MainActivity implements LocationListener {

    DeluxeSpeedView deluxeSpeedView;
    Button gpsOnOff;



    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);

        deluxeSpeedView = findViewById(R.id.deluxeSpeedView);
        deluxeSpeedView.setWithTremble(false);
        deluxeSpeedView.setWithEffects(false);

        gpsOnOff = findViewById(R.id.gpsButton);
        gpsOnOff.setText("GPS ON");

    }


    @SuppressLint("SetTextI18n")
    public void gps(View view){
        counter++;
        if (counter % 2 != 0) {
            gpsOnOff.setText("GPS OF");
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 543);
            else
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }
        else{
            gpsOnOff.setText("GPS ON");
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                ActivityCompat.requestPermissions(this,new String [] {Manifest.permission.ACCESS_FINE_LOCATION}, 543);
            else
                locationManager.removeUpdates(this);
        }
    }


    @SuppressLint("SetTextI18n")
    @Override
    public synchronized void onLocationChanged(final Location location) {
        final float MPS_to_KPH = (float) 3.6;

            deluxeSpeedView.speedTo(location.getSpeed() * MPS_to_KPH);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
