/*Ada Pluguez adapluguez@gmail.com- Morning Star Scanning and Tracking V0.2 11/17/20
Description: This activity uses the device camera to scan barcodes in realtime.
Once a barcode is scanned the user is asked to input the row and section, then the time and date are grabbed from the system clock.
Then a container is created for insertion into a batch and the sql database.
When batch complete is pressed the scanned barcode list is cleared and the batch is inserted into the database.
A new batch is created for continued scanning.
When scanning complete is pressed the user is sent to the batch management screen to assign gps coordinates.
Stages:
0.1 Basic Code39 Scanning Working, Realtime GUI Render and Photo Multi-Scan
0.2 Database Creation Occurs
0.3 gets user input for section, Setup all main screen transitions via buttons.
0.4 Scanning and database insertion and does not scan repeat barcodes
0.5 Transfer to batch management screen via scanning
0.6 Update batches via checklist by adding GPS data to batches - Build a batch detail screen
0.7 Build a container detail screen accessed from the batch detail screen by click on a container in the batch list
0.8 Build a view database functionality - Build a main search functionality that displays results by one or more search parameters
0.9 Organize and test all existing Functionality
1.0 Minimum functionality state DEC 2nd goal
References:
https://www.journaldev.com/18198/qr-code-barcode-scanner-android
https://www.journaldev.com/9438/android-sqlite-database-example-tutorial#opening-the-android-sqlite-database-file
https://www.journaldev.com/13325/android-location-api-tracking-gps
*/
package com.morningstar.barcodevisionapi;


import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;

import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Button;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.EditText;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;
import java.util.Date;
import java.util.Locale;

public class ScannedBarcodeActivity extends AppCompatActivity {// implements View.OnClickListener  {
    //Variables
    private EditText rowEditText;
    private EditText sectionEditText;
    private DBManager dbManager;
    private SimpleCursorAdapter adapter;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    SurfaceView surfaceView;
    TextView txtBarcodeValue;
    Button btnBatchComplete;
    Button btnScanComplete;
    Button btnRow;
    String intentData = "";
    String row = "0";
    String section = "0";
    int batchID = 0;
    int barcode_count = 0;
    String [] batch = new String[255];
    //ArrayList<String> batch = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_barcode);

        //For grabbing row and section input
        rowEditText = (EditText) findViewById(R.id.subject_edittext);
        sectionEditText = (EditText) findViewById(R.id.description_edittext);

        dbManager = new DBManager(this);
        dbManager.open();

        Cursor container_cursor = dbManager.fetch_containers();
        Cursor batch_cursor = dbManager.fetch_batches();



        initViews();
    }

    private void initViews() {
        txtBarcodeValue = findViewById(R.id.txtBarcodeValue);
        surfaceView = findViewById(R.id.surfaceView);
        btnBatchComplete = findViewById(R.id.btnBatchComplete);
        btnScanComplete = findViewById(R.id.btnScanningComplete);
        btnRow = findViewById(R.id.btnRow);

        //Batch Complete Button
        btnBatchComplete.setOnClickListener(v -> {
            //TODO Create SQL Batch  and insert
            dbManager.insert_batch(String.valueOf(batchID), barcode_count);
            //Reset barcode count
            barcode_count = 0;
            //Increment batchID
            batchID++;


            batch = new String[255];
            //Reset current batch display
            txtBarcodeValue.post(() -> {
                intentData = "";
                txtBarcodeValue.setText(intentData);
                barcode_count++;
            });


        });

        //Scanning Complete Button
        btnScanComplete.setOnClickListener(v -> {
            //TODO Place batch in SQL
            dbManager.insert_batch(String.valueOf(batchID), barcode_count);
            //Reset batch
            batch = new String[255];
            barcode_count = 0;
            batchID = 0;
            //Send user to batch management activity screen
            startActivity(new Intent(ScannedBarcodeActivity.this, BatchManagementActivity.class));
        });
        btnRow.setOnClickListener(v -> {
            //set row and section activity? or dialog popup of somekind?
            startActivity(new Intent(ScannedBarcodeActivity.this, AddSectionRowActivity.class));
        });

    }

    private void initialiseDetectorsAndSources() {
        //Toasts are small circular box text at the bottom of device screen
        //Toast.makeText(getApplicationContext(), "Barcode scanner started", Toast.LENGTH_SHORT).show();

        //Creates Barcode detector
        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        //Manages the camera in conjunction with an underlying detector.
        //Here SurfaceView is the underlying detector.
        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true) //you should add this feature
                .build();
        //Is used to display camera preview images as it renders the GUI rapidly.
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            //When, in the first instance, the surface is created, this method is called.
            @Override
            public void surfaceCreated(@androidx.annotation.NonNull SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(ScannedBarcodeActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(ScannedBarcodeActivity.this, new
                                String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

            //This method is called when the size or the format of the surface changes.
            @Override
            public void surfaceChanged(@androidx.annotation.NonNull SurfaceHolder holder, int format, int width, int height) {
            }
            //This is called when the surface is destroyed.
            @Override
            public void surfaceDestroyed(@androidx.annotation.NonNull SurfaceHolder holder) {
                cameraSource.stop();
            }
        });


        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
                //Toast Example
                //Toast.makeText(getApplicationContext(), "To prevent memory leaks barcode scanner has been stopped", Toast.LENGTH_SHORT).show();
            }

            //Recieves barcodes displays them, adds each to the same batch, until batch is complete and index is incremented.
            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                /*
                AlertDialog.Builder alert = new AlertDialog.Builder(ScannedBarcodeActivity.this);

                alert.setTitle("Title");
                alert.setMessage("Message");

                // Set an EditText view to get user input
                final EditText input = new EditText(ScannedBarcodeActivity.this);
                alert.setView(input);

                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String value = input.getText().toString();
                        // Do something with value!
                    }
                });
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                    }
                });

                alert.show();
                */
                if (barcodes.size() != 0) {



                    //Display barcodes to the textview located above batch complete button on the left hand side of the screen
                    txtBarcodeValue.post(() -> {

                        //Grab last scanned barcode --Store code in array--needed?
                        batch[barcode_count] = barcodes.valueAt(0).displayValue;
                        //intentData = barcodes.valueAt(0).displayValue;

                        //Display Current barcodes scanned into the current batch
                        intentData = intentData + " " + batch[barcode_count] + "\n";
                        txtBarcodeValue.setText(intentData);
                        barcode_count++;

                        //Grab date/time of scan then convert to string
                        Date currentTime = Calendar.getInstance().getTime();
                        //DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                        DateFormat dateFormat = new SimpleDateFormat("d MMM yyyy", Locale.US);
                        String strDate = dateFormat.format(currentTime);

                        //TODO get row/section input
                        row = getIntent().getStringExtra("ROW");
                        section = getIntent().getStringExtra("SECTION");


                        //TODO Notify user of a repeated barcode(use toast) and do not add to batch



                        //Create container on each Scan with relevant data
                        dbManager.insert_container(batchID,barcodes.valueAt(0).displayValue, strDate, Integer.parseInt(row),  Integer.parseInt(section));

                    });

                }

            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        cameraSource.release();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initialiseDetectorsAndSources();
    }
/*
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnAction2){
            startActivity(new Intent(ScannedBarcodeActivity.this, BatchManagementActivity.class));
        }
    }

 */
}
