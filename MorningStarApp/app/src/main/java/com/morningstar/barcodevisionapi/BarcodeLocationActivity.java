package com.morningstar.barcodevisionapi;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.CancellationTokenSource;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY;
import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

public class BarcodeLocationActivity extends AppCompatActivity {
    private static final int REQUEST_CHECK_SETTINGS = 1;
    private static final int REQUEST_FINE_LOCATION = 2;
    private FusedLocationProviderClient mFusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        displayLocationSettingsRequest(getApplicationContext());
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if(requestCode == REQUEST_FINE_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // permission granted, do stuff
                getLocation();
            }
            else{
                Toast.makeText(getApplicationContext(), "Permission was not granted", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            super.onRequestPermissionsResult(REQUEST_FINE_LOCATION, permissions, grantResults);
        }
    }
    //Changed to grab lat and long and send to activity. --Ada
    public void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            CancellationTokenSource cts = new CancellationTokenSource();
            Task<Location> currentLocationResult = getFusedLocationProviderClient(this).getCurrentLocation(PRIORITY_HIGH_ACCURACY, cts.getToken());
            currentLocationResult.addOnSuccessListener(this, Location -> {
                Location currentLocation = currentLocationResult.getResult();
                double latitude = currentLocation.getLatitude();
                double longitude = currentLocation.getLongitude();
                Intent main = new Intent(BarcodeLocationActivity.this, ScannedBarcodeActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                main.putExtra("LAT", latitude);
                main.putExtra("LONG", longitude);
                startActivity(main);
                //Toast.makeText(getApplicationContext(), "Longitude: "+ currentLocation.getLongitude() + ", Latitude: "+currentLocation.getLatitude(), Toast.LENGTH_LONG).show();
            });
            currentLocationResult.addOnFailureListener(this, e ->
                    Toast.makeText(getApplicationContext(), "Location failed: " + currentLocationResult.getException().toString(), Toast.LENGTH_LONG).show()
            );
        }
        else {
            requestPermissions(new String[]{ACCESS_FINE_LOCATION}, REQUEST_FINE_LOCATION);
        }
    }

    void displayLocationSettingsRequest(Context context) {
        LocationRequest mHighAccuracyLocationRequest = new LocationRequest();
        mHighAccuracyLocationRequest.setPriority(PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mHighAccuracyLocationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();
        SettingsClient settingsClient = LocationServices.getSettingsClient(this);


        Task<LocationSettingsResponse> result = settingsClient.checkLocationSettings(builder.build());
        result.addOnSuccessListener(this, locationSettingsResponse ->
                getLocation()
        );
        result.addOnFailureListener(this, e -> {
            if ( e instanceof ResolvableApiException) {
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.

                Toast.makeText(getApplicationContext(), "Everything Not Turned On", Toast.LENGTH_LONG).show();
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    ResolvableApiException resolvable = (ResolvableApiException) e;
                    resolvable.startResolutionForResult(BarcodeLocationActivity.this,
                            REQUEST_CHECK_SETTINGS);
                } catch (IntentSender.SendIntentException sendEx) {
                    // Ignore the error.
                }
            }
        });


    }
}