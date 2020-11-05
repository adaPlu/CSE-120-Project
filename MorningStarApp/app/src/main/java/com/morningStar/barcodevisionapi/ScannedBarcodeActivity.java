//Ada Pluguez - Morning Star Scanning and Tracking 0.1 11/3/20
//Reference: https://www.journaldev.com/18198/qr-code-barcode-scanner-android
package com.morningstar.barcodevisionapi;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Stream;

public class ScannedBarcodeActivity extends AppCompatActivity {

    //Variables
    SurfaceView surfaceView;
    TextView txtBarcodeValue;
    String [] batch = new String[255];
    //ArrayList<String> batch = new ArrayList<String>();
    int barcode_count = 0;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    Button btnAction;
    Button btnAction2;
    String intentData = "";
    boolean firstBatch = true;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_barcode);
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
        btnAction2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Place batch in SQL
                //Reset batch
                //batch = new ArrayList<String>();
                batch = new String[255];
                barcode_count = 0;
                //Send user to batch management activity screen
            }
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

                    //Display barcodes to textview located above batch complete button


                    txtBarcodeValue.post(() -> {
                        //TODO:if scanned barcode not in array or array list? Display code and add to batch
                       /*
                        boolean contains = false;
                        if(batch.contains(barcodes.valueAt(0).displayValue))
                            contains = true;

                        for (int i = 0; i < batch.size(); i++) {
                            if(batch.get(i).equals(barcodes.valueAt(0).displayValue))
                                contains = true;
                        }

                        if(!contains){
                            //Store code in array
                            batch.add(barcodes.valueAt(0).displayValue);
                            //intentData = barcodes.valueAt(0).displayValue;
                            //Display Current batch
                            intentData = intentData + " " + batch.get(barcode_count) + "\n";
                            txtBarcodeValue.setText(intentData);
                            barcode_count++;


                        }
                        */
                        //Store code in array
                        batch[barcode_count] = barcodes.valueAt(0).displayValue;
                        //intentData = barcodes.valueAt(0).displayValue;
                        //Display Current batch
                        intentData = intentData + " " + batch[barcode_count] + "\n";
                        txtBarcodeValue.setText(intentData);
                        barcode_count++;


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
