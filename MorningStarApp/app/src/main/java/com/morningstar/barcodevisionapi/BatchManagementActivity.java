package com.morningstar.barcodevisionapi;
/*Ada Pluguez - Morning Star Scanning and Tracking V0.2 11/17/20*/
import android.content.Intent;
import android.database.Cursor;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.barcode.BarcodeDetector;

public class BatchManagementActivity extends AppCompatActivity{
    //Variables
    private DBManager dbManager;
    private SimpleCursorAdapter adapter;
    String row = "";
    String section = "";
    private EditText rowEditText;
    private EditText sectionEditText;
    Button btnAddGPS;
    Button btnNewBatch;
    Button btnEditBatch;
    CheckBox checkBox;
    CheckBox checkBox2;
    CheckBox checkBox3;
    CheckBox checkBox4;
    CheckBox checkBox5;
    CheckBox checkBox6;
    CheckBox checkBox7;
    CheckBox checkBox8;
    CheckBox checkBox9;
    CheckBox checkBox10;
    CheckBox checkBox11;
    CheckBox checkBox12;
    CheckBox checkBox13;
    CheckBox checkBox14;
    CheckBox checkBox15;
    CheckBox checkBox16;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_batch_management);

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
        btnAddGPS = findViewById(R.id.btnAddGPS);
        btnNewBatch = findViewById(R.id.btnNewBatch);
        btnEditBatch = findViewById(R.id.btnEditBatch);
        checkBox = findViewById(R.id.checkBox);
        checkBox2 = findViewById(R.id.checkBox2);
        checkBox3 = findViewById(R.id.checkBox3);
        checkBox4 = findViewById(R.id.checkBox4);
        checkBox5 = findViewById(R.id.checkBox5);
        checkBox6 = findViewById(R.id.checkBox6);
        checkBox7 = findViewById(R.id.checkBox7);
        checkBox8 = findViewById(R.id.checkBox8);
        checkBox9 = findViewById(R.id.checkBox9);
        checkBox10 = findViewById(R.id.checkBox10);
        checkBox11 = findViewById(R.id.checkBox11);
        checkBox12 = findViewById(R.id.checkBox12);
        checkBox13 = findViewById(R.id.checkBox13);
        checkBox14 = findViewById(R.id.checkBox14);
        checkBox15 = findViewById(R.id.checkBox15);
        checkBox16 = findViewById(R.id.checkBox16);

        btnAddGPS.setOnClickListener(v -> {

        });
        btnNewBatch.setOnClickListener(v -> startActivity(new Intent(BatchManagementActivity.this, ScannedBarcodeActivity.class)));
        btnEditBatch.setOnClickListener(v -> {

        });

    }

}