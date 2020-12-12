package com.morningstar.barcodevisionapi;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/*Ada Pluguez - Morning Star Scanning and Tracking V0.2 11/17/20
 * Description: This activity allows  to scan new containers into a existing batch.
 * This will be a clone of ScanBarcodeActivity, except it will load the selected batch id and any scanned items will be added to that existing batch.
 * Unlike ScanBarcodeActivity which is written to start a new batch.

 */
public class AddToBatchActivity extends AppCompatActivity {
    //TODO clone ScanBarcodeActivity- this must be finished first
    //TODO Modify code to add to an existing batch rather than start a new one
//Variables
    private DBManager dbManager;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    SurfaceView surfaceView;
    TextView txtBarcodeValue;
    Button btnBatchComplete;
    Button btnScanComplete;
    Button btnRow;
    Button btnSec;
    Button btnClearScan;
    Button btnClearBatch;
    Button btnExit;
    String intentData = "";
    String row = "0";
    String section = "0";
    int batchID = 1;
    int barcode_count = 0;
    ArrayList<Container> containers = new ArrayList<>();
    ArrayList<Batch> batches = new ArrayList<>();
    ArrayList<String> barcodeS = new ArrayList<>();
    Batch currentBatch = new Batch();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_barcode);


        dbManager = new DBManager(this);
        dbManager.open();

        rowAlert();
        sectionAlert();
        initViews();
    }
    private void rowAlert(){
        AlertDialog.Builder alert = new AlertDialog.Builder(AddToBatchActivity.this);

        alert.setTitle("Set Row");
        alert.setMessage("Row:");

        // Set an EditText view to get user input
        final EditText input = new EditText(AddToBatchActivity.this);
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                row = input.getText().toString();

            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();

    }
    private void sectionAlert(){
        AlertDialog.Builder alert = new AlertDialog.Builder(AddToBatchActivity.this);

        alert.setTitle("Set Section");
        alert.setMessage("Section:");

        // Set an EditText view to get user input
        final EditText input = new EditText(AddToBatchActivity.this);
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                section = input.getText().toString();

            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();

    }
    private void initViews() {
        txtBarcodeValue = findViewById(R.id.txtBarcodeValue);
        surfaceView = findViewById(R.id.surfaceView);
        btnBatchComplete = findViewById(R.id.btnBatchComplete);
        btnScanComplete = findViewById(R.id.btnScanningComplete);
        btnRow = findViewById(R.id.btnRow);
        btnSec = findViewById(R.id.btnSec);
        btnClearBatch = findViewById(R.id.btnClearBatch);
       // btnClearScan = findViewById(R.id.btnClearScan);
        btnExit = findViewById(R.id.btnExit);

        //Batch Complete Button
        btnBatchComplete.setOnClickListener(new View.OnClickListener() {    //start
            @Override
            public void onClick(View v) {
                for (int i = 0; i < containers.size(); i++) {
                    int id = containers.get(i).getBatchID();
                    int r = containers.get(i).getRow();
                    int s = containers.get(i).getSection();
                    String date = containers.get(i).getDate();
                    String barcode = containers.get(i).getBarcode();
                    dbManager.insert_container(batchID, barcode, date, r, s);
                }
                Intent main = new Intent(AddToBatchActivity.this, EditBatchActivity.class);
                DataWrapper a = new DataWrapper(batches);
                Bundle args = new Bundle();
                args.putSerializable("batches", a);
                main.putExtra("BUNDLE",args);
                startActivity(main);

                //Reset Container Array
                containers = new ArrayList<>();
                //Add current batch to arraylist for passing to batch management
                batches.add(currentBatch);
                //Reset current batch
                currentBatch = new Batch();
                //Reset barcode count
                barcode_count = 0;
                //Increment batchID
                batchID++;
                //reset barcodes
                barcodeS = new ArrayList<>();
                row = "0";
                section = "0";
                // Reset current batch display
                txtBarcodeValue.post(() -> {
                    intentData = batchID + ":\n";
                    txtBarcodeValue.setText(intentData);
                });
            }

        }); //end

        //Scanning Complete Button
        // btnScanComplete.setOnClickListener(v -> {
//            //Insert current batches and pass to management for GPS update
//            for(int i = 0; i < batches.size(); i++){
//                int id = batches.get(i).getBatchID();
//                int numOfContainers = batches.get(i).getNumOfContainers();
//                dbManager.insert_batch(String.valueOf(id), numOfContainers);
//            }
//            //Insert any remaining containers
//            for(int i = 0; i < containers.size(); i++){
//                int id = containers.get(i).getBatchID();
//                int r = containers.get(i).getRow();
//                int s = containers.get(i).getSection();
//                String date = containers.get(i).getDate();
//                String barcode = containers.get(i).getBarcode();
//                dbManager.insert_container(batchID,barcode, date, r,  s);
//            }
//
//            Intent main = new Intent(ScannedBarcodeActivity.this, BatchManagementActivity2.class);
//            DataWrapper a = new DataWrapper(batches);
//            main.putExtra("batches", a);
//            startActivity(main);
//        });

        btnRow.setOnClickListener(v -> {
            rowAlert();
        });
        btnSec.setOnClickListener(v -> {
            sectionAlert();
        });
        btnClearBatch.setOnClickListener(v -> {
            //Reset Container Array
            containers = new ArrayList<>();
            //Reset current batch
            currentBatch = new Batch();
            //Reset barcode count
            barcode_count = 0;
            //reset barcodes
            barcodeS = new ArrayList<>();
            row = "0";
            section = "0";
            //Reset current batch display
            txtBarcodeValue.post(() -> {
                intentData = batchID + ":\n";
                txtBarcodeValue.setText(intentData);
            });
        });
        btnClearScan.setOnClickListener(v -> {
            removeLastScan();
        });
        btnExit.setOnClickListener(v -> {
            startActivity(new Intent(AddToBatchActivity.this, MainActivity.class));
        });

    }

    private void removeLastScan(){
        //TODO Clear last scan and remove from textview
        String remove = containers.get(barcode_count).getBarcode();
        containers.remove(barcode_count);
        intentData = intentData.replace(remove, "");
        txtBarcodeValue.post(() -> {
            txtBarcodeValue.setText(intentData);
        });

    }


    //Function controls line animation
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

    //Create barcode detectors and camaera sources
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
                    if (ActivityCompat.checkSelfPermission(AddToBatchActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(AddToBatchActivity.this, new
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

                if (barcodes.size() != 0) {
                    //TODO add beep on detection
                    //Notify user of a repeated barcode(using toast) and  do not add to batch
                    txtBarcodeValue.post(() -> {
                        //check for duplicates
                        for(int i = 0; i < barcodeS.size(); i++){
                            if(barcodeS.get(i).contains(barcodes.valueAt(0).displayValue)){
                                Toast.makeText(getApplicationContext(), "Barcode Already Scanned to Current Batch.", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                        //Grab last scanned barcode
                        barcodeS.add(barcodes.valueAt(0).displayValue);
                        intentData = batchID + ":\n";

                        //Display Current barcodes scanned into the current batch
                        intentData = intentData + " " + barcodeS.get(barcode_count) + "\n";
                        //txtBarcodeValue.setText(intentData);

                        barcode_count++;

                        //Grab date/time of scan then convert to string
                        Date currentTime = Calendar.getInstance().getTime();
                        DateFormat dateFormat = new SimpleDateFormat("d MMM yyyy", Locale.US);
                        String strDate = dateFormat.format(currentTime);

                        //Create container on each Scan then insert into database as these do not have GPS only batchIDs
                        //dbManager.insert_container(batchID,barcodes.valueAt(0).displayValue, strDate, Integer.parseInt(row),  Integer.parseInt(section));

                        Container currentContainer = new Container(batchID,barcodes.valueAt(0).displayValue, strDate, Integer.parseInt(row),  Integer.parseInt(section));
                        containers.add(currentContainer);
                        currentBatch.setBatchID(batchID);
                        int temp = currentBatch.getNumOfContainers() + 1;
                        currentBatch.setNumOfContainers(temp);


                        txtBarcodeValue.setText(intentData);
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

}
