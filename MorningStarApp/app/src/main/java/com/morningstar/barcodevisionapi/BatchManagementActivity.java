package com.morningstar.barcodevisionapi;
/*
* Description: Allows user to assign gps coordinates to one or more batches via a checkbox list.
* Clicking edit when a single batch is selected allows the user to see all containers in that batch and the details of those containers.
* Edit mode details see the EditBatchActivity description.
* Clicking new sends the user back to the realtime scanning screen.
* Batches are listed by batch id, GPS?(maybe)
*
* */

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.widget.AdapterView;
import android.widget.Toast;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.CancellationTokenSource;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.Objects;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY;
import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;


public class BatchManagementActivity extends AppCompatActivity {
    ArrayList<DataModel> dataModels = new ArrayList<>();
    ArrayList<Batch> batches = new ArrayList<>();
    ListView listView;
    private CustomAdapter adapter;
    //Variables
    private DBManager dbManager;
    private SimpleCursorAdapter adapter2;
    private static final int REQUEST_CHECK_SETTINGS = 1;
    private static final int REQUEST_FINE_LOCATION = 2;
    private FusedLocationProviderClient mFusedLocationClient;

    //Widgets
    Button btnAddGPS;
    Button btnNewBatch;
    Button btnEditBatch;
    Button btnQuit2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_batch_management);
        listView = findViewById(R.id.listView);
        //Connect to or create DB
        dbManager = new DBManager(this);
        dbManager.open();
        //DB cursors for access?
        Cursor container_cursor = dbManager.fetch_containers();
        Cursor batch_cursor = dbManager.fetch_batches();

        initViews();
        dataModels = new ArrayList<>();
        dataModels.add(new DataModel("Batch#:", false));
        dataModels.add(new DataModel("Batch#", false));
        dataModels.add(new DataModel("Batch#", false));
        dataModels.add(new DataModel("Batch#", false));
        dataModels.add(new DataModel("Batch#", false));
        dataModels.add(new DataModel("Batch#", false));
        dataModels.add(new DataModel("Batch#", false));
        dataModels.add(new DataModel("Batch#", false));
        dataModels.add(new DataModel("Batch#", false));
        dataModels.add(new DataModel("Batch#", false));
        dataModels.add(new DataModel("Batch#", false));
        dataModels.add(new DataModel("Batch#", false));
        adapter = new CustomAdapter(dataModels, getApplicationContext());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            DataModel dataModel = dataModels.get(position);
            dataModel.checked = !dataModel.checked;
            adapter.notifyDataSetChanged();
        });

    }

    //Init view of screen widgets
    private void initViews() {
        btnAddGPS = findViewById(R.id.btnAddGPS);
        btnNewBatch = findViewById(R.id.btnNewBatch);
        btnEditBatch = findViewById(R.id.btnEditBatch);
        btnQuit2 = findViewById(R.id.btnQuit2);
        DataWrapper dw = (DataWrapper) getIntent().getSerializableExtra("batches");
        if (dw != null) {
            batches = dw.getBatches();
        }
        //Button Functionality
        btnAddGPS.setOnClickListener(v -> {
            //TODO Grab current GPS
           //startActivity(new Intent(BatchManagementActivity.this, BarcodeLocationActivity.class));
            // TODO Update checked batches with GPS data via SQLite
            getLocation();

        });
        //Sends user back to scanning activity
        btnNewBatch.setOnClickListener(v -> startActivity(new Intent(BatchManagementActivity.this, ScannedBarcodeActivity.class)));

        btnQuit2.setOnClickListener(v ->
                       quitManage()
        );//this.finish() startActivity(new Intent(BatchManagementActivity.this, ScannedBarcodeActivity.class)));
        btnEditBatch.setOnClickListener(v -> {

            //TODO 1.Only allows a single batch to be selected, if more than one is selected warn user with Toast(small text at bottom of screen) to only select one.
            //Toast Example:
            //Toast.makeText(getApplicationContext(), "Barcode Already in Batch", Toast.LENGTH_SHORT).show();
            //TODO 2.When one batch is selected load a list of the containers in the batch and their details.
            //TODO 3.A button to delete a container
            //TODO 4.A button to add a container that sends user to scanning screen to add new containers to that batch.
            //This will likely require cloning the scanning activity and adding code to add the batch in question, making a addToExistingBatch.activity.
            //This is because the original scanning activity is built to start on a new batch.

        });
    }

    public void quitManage(){
        this.finish();
        startActivity(new Intent(BatchManagementActivity.this, MainActivity.class));
    }
    public void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            CancellationTokenSource cts = new CancellationTokenSource();
            Task<Location> currentLocationResult = getFusedLocationProviderClient(this).getCurrentLocation(PRIORITY_HIGH_ACCURACY, cts.getToken());
            currentLocationResult.addOnSuccessListener(this, Location -> {
                Location currentLocation = currentLocationResult.getResult();
                double latitude = currentLocation.getLatitude();
                double longitude = currentLocation.getLongitude();
                //Update a batch with GPS
                Toast.makeText(getApplicationContext(), "Longitude: "+ currentLocation.getLongitude() + ", Latitude: "+currentLocation.getLatitude(), Toast.LENGTH_SHORT).show();
            });
            currentLocationResult.addOnFailureListener(this, e ->
                    Toast.makeText(getApplicationContext(), "Location failed: " + Objects.requireNonNull(currentLocationResult.getException()).toString(), Toast.LENGTH_SHORT).show()
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
                    resolvable.startResolutionForResult(BatchManagementActivity.this, REQUEST_CHECK_SETTINGS);
                } catch (IntentSender.SendIntentException sendEx) {
                    // Ignore the error.
                }
            }
        });


    }
}
