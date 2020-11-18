/*Ada Pluguez - Morning Star Scanning and Tracking V0.2 11/17/20
0.1 Basic Code39 Scanning Working.
0.2 Database Creation Occurs
0.3 Scanning and database insertion complete (gets user input for section and row, does not scan repeat barcodes)
0.4 Transfer to batch management screen via scanning complete
0.5 Update batches via checklist by adding GPS data to batches
0.6 Build a batch detail screen
0.7 Build a container detail screen accessed from the batch detail screen by click on a container in the batch list
0.8 Build a main search button that displays results by one or more search parameters
0.9 Organize and test all existing Functionality
1.0 Minimum functionality state DEC 2nd goal
References:
https://www.journaldev.com/18198/qr-code-barcode-scanner-android
https://www.journaldev.com/9438/android-sqlite-database-example-tutorial#opening-the-android-sqlite-database-file
https://www.journaldev.com/13325/android-location-api-tracking-gps
*/
package com.morningstar.barcodevisionapi;


import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Stream;

public class ScannedBarcodeActivity extends AppCompatActivity {
    //Variables
    SurfaceView surfaceView;
    TextView txtBarcodeValue;
    private DBManager dbManager;
    private SimpleCursorAdapter adapter;
    String [] batch = new String[255];
    //ArrayList<String> batch = new ArrayList<String>();
    int barcode_count = 0;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    int batchID = 0;
    Button btnAction;
    Button btnAction2;
    String intentData = "";
    boolean firstBatch = true;
    private EditText subjectEditText;
    private EditText descEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_barcode);

        //subjectEditText = (EditText) findViewById(R.id.subject_edittext);
        //descEditText = (EditText) findViewById(R.id.description_edittext);

        dbManager = new DBManager(this);
        dbManager.open();

        Cursor container_cursor = dbManager.fetch_containers();
        Cursor batch_cursor = dbManager.fetch_batches();
        dbManager = new DBManager(this);
        dbManager.open();


        initViews();
    }

    private void initViews() {
        txtBarcodeValue = findViewById(R.id.txtBarcodeValue);
        surfaceView = findViewById(R.id.surfaceView);
        btnAction = findViewById(R.id.btnAction);
        btnAction2 = findViewById(R.id.btnAction2);

        //Batch Complete Button
        btnAction.setOnClickListener(v -> {
            barcode_count = 0;
            //TODO Create SQL Batch and container data for database
            //See add record in addCountryActivity
            //Reset Batch to Empty
            //batch = new ArrayList<String>();
            batch = new String[255];
            //Reset current batch display
            txtBarcodeValue.post(() -> {
                intentData = "";
                txtBarcodeValue.setText(intentData);
                barcode_count++;
            });
            /*Old code
            if (intentData.length() > 0) {
                if (isEmail)
                    startActivity(new Intent(ScannedBarcodeActivity.this, EmailActivity.class).putExtra("email_address", intentData));
                else {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(intentData)));

                }
            }
            */

        });

        //Scanning Complete Button
        btnAction2.setOnClickListener(v -> {
            //TODO Place batch in SQL
            //Reset batch
            //batch = new ArrayList<String>();
            batch = new String[255];
            barcode_count = 0;
            //TODO Send user to batch management activity screen
        });

        //Scanning Complete Button
        btnAction2.setOnClickListener(v -> {
            //Place batch in SQL
            //Reset batch
            //batch = new ArrayList<String>();
            batch = new String[255];
            barcode_count = 0;
            //Send user to batch management activity screen
        });
    }

    private void initialiseDetectorsAndSources() {

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
                //Toast.makeText(getApplicationContext(), "To prevent memory leaks barcode scanner has been stopped", Toast.LENGTH_SHORT).show();
            }
            //Recieves barcodes displays them, adds each to the same batch, until batch is complete and index is incremented.
            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
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

                        //TODO grab date/time of scan
                        Date currentTime = Calendar.getInstance().getTime();
                        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
                        String strDate = dateFormat.format(currentTime);
                        //TODO get row input
                        //TODO get section input

                        //TODO Create container on each Scan
                        dbManager.insert_container(batchID,barcodes.valueAt(0).displayValue, strDate, 0, 0);

                        //TODO add to current batch
                        //TODO Notify user of a repeated barcode and do not add to batch
                        //TODO Create new Batch when user clicks Batch Complete
                        //TODO Scan complete send to batch management screen
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
}
