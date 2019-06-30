package com.unipi.vnikolis.askisi2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements LocationListener {

    Database myDb;
    LocationManager locationManager;
    TextView showSpeed,showDistance;
    Button gpsOnOff,getDistance,saveData, goToDeluxe;
    public int counter;
    String selectedTown,latitude, longitude;
    EditText name, setLatitude, SetLongitude;
    Spinner mySpinner;



    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDb = new Database(this);
        locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);

        //τραβάει τα δεδομένα από το dropdown menu
        mySpinner = findViewById(R.id.spinner2);
        ArrayAdapter<String> myAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.towns));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(myAdapter);


        showSpeed = findViewById(R.id.showSpeed);
        gpsOnOff = findViewById(R.id.gpsButton);
        gpsOnOff.setText("GPS ON");
        getDistance = findViewById(R.id.getDist);
        showDistance = findViewById(R.id.showDistance);
        name = findViewById(R.id.name);
        setLatitude = findViewById(R.id.lati);
        SetLongitude  = findViewById(R.id.lontit);
        saveData = findViewById(R.id.button2);
        goToDeluxe = findViewById(R.id.deluxe);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        addData();
        takeData();
        goToDeluxeSpeed();
    }

    @SuppressLint("SetTextI18n")
    public void gps(View view){
        counter++;
        if (counter % 2 != 0) {
            gpsOnOff.setText("GPS OFF");
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

    //προσθεσε τα δεδομενα στη βαση
    public void addData()
    {
        saveData.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                boolean isInserted = myDb.insert(name.getText().toString(), setLatitude.getText().toString(), SetLongitude.getText().toString());
                if(isInserted){
                    Toast.makeText(MainActivity.this, "Inserted", Toast.LENGTH_LONG).show();

                }
                else{
                    Toast.makeText(MainActivity.this, "Not Inserted", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    //on click παρε τις συντεταγμενες
    public void takeData(){
        getDistance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectedTown = mySpinner.getSelectedItem().toString(); //με το κλικ πάρε την επιλεγμένη πόλυ απο το dropdown menu και τσεκαρε στη βάση για τις συντεταγμένες
                Cursor res = myDb.getData(selectedTown);
                if(res.getCount() == 0){
                    Toast.makeText(MainActivity.this, "No Results Found", Toast.LENGTH_LONG).show();
                }

                while(res.moveToNext()){
                  latitude =  res.getString(2);
                  longitude =  res.getString(3);
                }
            }
        });

    }

    //go to 2 form
    public void goToDeluxeSpeed(){
        goToDeluxe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Activity2.class);
                startActivity(intent);
            }
        });
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onLocationChanged(final Location location) {

        final float MPS_to_KPH = (float) 3.6;
        if (location != null){
            showSpeed.setText(location.getSpeed() * MPS_to_KPH + " Km/h");

            //παίρνουμε τις συντεταγμένες που ειναι αποθηκευμενες στη βαση και τις συγκρίνουμε on real time με τις υπάρχουσες του GPS (εδω κάνουμε τον έλεγχο γιατι σκάει η εφαρμογη)
            if(latitude != null && longitude != null)
            {
                Location newlocation = new Location("newlocation");
                newlocation.setLatitude(Double.parseDouble(latitude));
                newlocation.setLongitude(Double.parseDouble(longitude));
                float distance = location.distanceTo(newlocation) / 1000;
                showDistance.setText(String.valueOf(distance) + "Km");
            }

        }
        else{
            showSpeed.setText("0.0 Km/h");
        }
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
