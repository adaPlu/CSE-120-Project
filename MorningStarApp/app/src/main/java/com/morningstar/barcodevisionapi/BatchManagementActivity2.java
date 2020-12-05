package com.morningstar.barcodevisionapi;
/*
* Description: Allows user to assign gps coordinates to one or more batches via a checkbox list.
* Clicking edit when a single batch is selected allows the user to see all containers in that batch and the details of those containers.
* Edit mode details see the EditBatchActivity description.
* Clicking new sends the user back to the realtime scanning screen.
* Batches are listed by batch id, GPS?(maybe)
*
* */

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.AdapterView;
import java.util.ArrayList;


public class BatchManagementActivity2 extends AppCompatActivity {
    ArrayList dataModels;
    ArrayList<Batch> batches = new ArrayList<>();
    ListView listView;
    private CustomAdapter adapter;
    //Variables
    private DBManager dbManager;
    private SimpleCursorAdapter adapter2;
    //Widgets
    Button btnAddGPS;
    Button btnNewBatch;
    Button btnEditBatch;
    Button btnQuit2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_batch_management2);
        listView = (ListView) findViewById(R.id.listView);
        //Connect to or create DB
        dbManager = new DBManager(this);
        dbManager.open();
        //DB cursors for access?
        Cursor container_cursor = dbManager.fetch_containers();
        Cursor batch_cursor = dbManager.fetch_batches();

        initViews();
        dataModels = new ArrayList();
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
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                DataModel dataModel = (DataModel) dataModels.get(position);
                dataModel.checked = !dataModel.checked;
                adapter.notifyDataSetChanged();
            }
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


        });
        //Sends user back to scanning activity
        btnNewBatch.setOnClickListener(v -> startActivity(new Intent(BatchManagementActivity2.this, ScannedBarcodeActivity.class)));

        btnQuit2.setOnClickListener(v -> startActivity(new Intent(BatchManagementActivity2.this, MainActivity.class)));
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
}
