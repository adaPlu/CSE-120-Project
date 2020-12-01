/*Morning Star Scanning and Tracking V0.4 11/17/20
By Ada Pluguez adapluguez@gmail.com
Description: This activity uses the device camera to scan barcodes in realtime.
Once a barcode is scanned the user is asked to input the row and section, then the time and date are grabbed from the system clock.
Then a container is created for insertion into a batch and the sql database.
When batch complete is pressed the scanned barcode list is cleared and the batch is inserted into the database.
A new batch is created for continued scanning.
When scanning complete is pressed the user is sent to the batch management screen to assign gps coordinates.
Stages:
0.1 Basic Code39 Scanning Working, Realtime GUI Render and Photo Multi-Scan
0.2 Database Creation Occurs
0.3 gets user input for section/row, setup all main screen transitions via buttons, Transfer to batch management screen via scanning.
0.3.5 gets user input for section/row via popup dialog rather than activity.
0.4 Scanning and database insertion
0.4.5 Does not scan repeat barcodes
0.5 Update batches via checklist by adding GPS data to batches - Build a batch detail screen
0.7 Build a container detail screen accessed from the batch detail screen by click on a container in the batch list
0.8 Build a view database functionality
0.9 Build a main search functionality that displays results by one or more search parameters
1.0 Organize and test all existing Functionality - Minimum functionality state
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
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    Button btnExit;
    String intentData = "";
    String row = "0";
    String section = "0";

    int batchID = 0;
    int barcode_count = 0;
    //ArrayList<Container> containers = new ArrayList<Container>();
    ArrayList<Batch> batches = new ArrayList<>();
    ArrayList<String> barcodeString = new ArrayList<>();
    //String[] barcodeString = new String[255];
    Batch currentBatch = new Batch();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_barcode);

        //Get Section and row from activity with out crash on null
        /*
        if(getIntent()!=null && getIntent().getExtras()!=null){
            Bundle bundle = getIntent().getExtras();
            if(bundle.getString("ROW") != null){
                row = bundle.getString("ROW");
            }
            if(bundle.getString("SECTION")!= null){
                section = bundle.getString("SECTION");
            }
        }
        */
        //row = getIntent().getStringExtra("ROW");
        //section = getIntent().getStringExtra("SECTION");


        //For grabbing row and section input
        //rowEditText = (EditText) findViewById(R.id.subject_edittext);
        //sectionEditText = (EditText) findViewById(R.id.description_edittext);

        dbManager = new DBManager(this);
        dbManager.open();

        //Cursor container_cursor = dbManager.fetch_containers();
        //Cursor batch_cursor = dbManager.fetch_batches();

        initViews();
    }

    private void initViews() {
        txtBarcodeValue = findViewById(R.id.txtBarcodeValue);
        surfaceView = findViewById(R.id.surfaceView);
        btnBatchComplete = findViewById(R.id.btnBatchComplete);
        btnScanComplete = findViewById(R.id.btnScanningComplete);
        btnRow = findViewById(R.id.btnRow);
        btnExit = findViewById(R.id.btnExit);

        //Batch Complete Button
        btnBatchComplete.setOnClickListener(v -> {
            //Add current batch to arraylist for passing to batch management
            batches.add(currentBatch);
            //Reset current batch
            //currentBatch = new Batch();
            //Reset barcode count
            barcode_count = 0;
            //Increment batchID
            batchID++;
            //reset barcodes
            barcodeString = new ArrayList<>();
            row = "0";
            section = "0";
            //Reset current batch display
            txtBarcodeValue.post(() -> {
                intentData = "";
                txtBarcodeValue.setText(intentData);
            });


        });

        //Scanning Complete Button
        btnScanComplete.setOnClickListener(v -> {
            //Insert current batches and pass to management for GPS update?

            for(int i = 0; i < batches.size(); i++){
                int id = batches.get(i).getBatchID();
                int numOfContainers = batches.get(i).getNumOfContainers();
                dbManager.insert_batch(String.valueOf(id), numOfContainers);
            }

            Intent main = new Intent(ScannedBarcodeActivity.this, BatchManagementActivity2.class);
            main.putExtra("BATCHES", batches);
            startActivity(main);
        });
        btnRow.setOnClickListener(v -> {
            //set row and section activity? or dialog popup of somekind? this is a placer holder for a better dialog popup
            startActivity(new Intent(ScannedBarcodeActivity.this, AddSectionRowActivity.class));
        });
        btnExit.setOnClickListener(v -> {
            //dbManager.insert_batch(String.valueOf(batchID), barcode_count);
            startActivity(new Intent(ScannedBarcodeActivity.this, MainActivity.class));
        });
    }

    //Function conrtols line animation
    private void setAnimation() {
        final View line = findViewById(R.id.line);
        final Animation anim = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.move);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                line.startAnimation(anim);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        line.startAnimation(anim);
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
                //TODO get row/section input -Attempt at a dialog popup rather than an activity- Crashes?. Need better examples...
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

                    //TODO Notify user of a repeated barcode(use toast) and  do not add to batch
                    //Toast.makeText(getApplicationContext(), "Barcode Already Scanned to Current Batch.", Toast.LENGTH_SHORT).show();
                    //Display barcodes to the textview located above batch complete button on the left hand side of the screen
                    txtBarcodeValue.post(() -> {
                        //Add sound on scan?
                        //Grab last scanned barcode --Store code in array--needed?
                        barcodeString.add(barcodes.valueAt(0).displayValue);
                        //intentData = barcodes.valueAt(0).displayValue;

                        //Display Current barcodes scanned into the current batch
                        intentData = intentData + " " + barcodeString.get(barcode_count) + "\n";
                        txtBarcodeValue.setText(intentData);
                        barcode_count++;

                        //Grab date/time of scan then convert to string
                        Date currentTime = Calendar.getInstance().getTime();
                        //DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                        DateFormat dateFormat = new SimpleDateFormat("d MMM yyyy", Locale.US);
                        String strDate = dateFormat.format(currentTime);

                        //TODO get row/section input -- Change to a dialog popup rather than an activity- Would improve workflow.
                        if(getIntent()!=null && getIntent().getExtras()!=null){
                            Bundle bundle = getIntent().getExtras();
                            if(bundle.getString("ROW") != null){
                                row = bundle.getString("ROW");
                            }
                            if(bundle.getString("SECTION")!= null){
                                section = bundle.getString("SECTION");
                            }
                        }

                        //Create container on each Scan then insert into database as these do not have GPS only batchIDs
                        dbManager.insert_container(batchID,barcodes.valueAt(0).displayValue, strDate, Integer.parseInt(row),  Integer.parseInt(section));
                        //Container currentContainer = new Container(batchID,barcodes.valueAt(0).displayValue, strDate, Integer.parseInt(row),  Integer.parseInt(section));
                        currentBatch.setBatchID(batchID);
                        int temp = currentBatch.getNumOfContainers() + 1;
                        currentBatch.setNumOfContainers(temp);
                    });

                }

            }
        });
        setAnimation();

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

    public interface BarcodeReaderListener {
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
