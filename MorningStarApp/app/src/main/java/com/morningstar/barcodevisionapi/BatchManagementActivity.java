package com.morningstar.barcodevisionapi;
/*
* Description: Allows user to assign gps coordinates to one or more batches via a checkbox list.
* Clicking edit when a single batch is selected allows the user to see all containers in that batch and the details of those containers.
* Edit mode details see the EditBatchActivity description.
* Clicking new sends the user back to the realtime scanning screen.
* Batches are listed by batch id, GPS?(maybe)
* */
import android.content.Intent;
import android.database.Cursor;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;


public class BatchManagementActivity extends AppCompatActivity{
    //Variables
    private DBManager dbManager;
    private SimpleCursorAdapter adapter;
    //Widgets
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

    //Same as main
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_batch_management);
        //Object retrival getIntent().getSerializableExtra("BATCHES");
        //Connect to or create DB
        dbManager = new DBManager(this);
        dbManager.open();
        //DB cursors for access?
        Cursor container_cursor = dbManager.fetch_containers();
        Cursor batch_cursor = dbManager.fetch_batches();

        initViews();
    }
    //Init view of screen widgets
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

        //Button Functionality
        btnAddGPS.setOnClickListener(v -> {
            //TODO Grab current GPS
            startActivity(new Intent(BatchManagementActivity.this, BarcodeLocationActivity.class));
            // TODO Update checked batches with GPS data via SQLite
            for(int i = 0; i < 16; i++){
                onCheckboxClicked(this.findViewById(R.id.checkBox));

            }


        });
        //Sends user back to scanning activity
        btnNewBatch.setOnClickListener(v -> startActivity(new Intent(BatchManagementActivity.this, ScannedBarcodeActivity.class)));

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

    //CheckBoxs
    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        /*
        switch(view.getId()) {
            case R.id.checkBox:
                if (checked)

            else
                break;
            case R.id.checkBox2:
                if (checked)

            else

                break;
        }
        */

    }

}
