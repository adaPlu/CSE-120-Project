package com.morningstar.barcodevisionapi;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

/*Ada Pluguez - Morning Star Scanning and Tracking V0.2 11/17/20
 * Description: This activity allows the user to delete from a batch via a check list of containers showing all their details.
 * There is also a  add button to scan container to that batch using the AddToBatchActivity.

 *
 */
public class EditBatchActivity extends AppCompatActivity {
    //TODO SHow batch ID at the top- GPS?
    //TODO build checklist of containers showing their details barcode, datetime,row/section
    //TODO add functional delete button that allows deletion of one or more selected containers
    //TODO add button linked to AddToBatchActivity
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
    Button btnExit;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_batch);

        dbManager = new DBManager(this);
        dbManager.open();
        //DB cursors for access?
        Cursor container_cursor = dbManager.fetch_containers();
        Cursor batch_cursor = dbManager.fetch_batches();

        //initViews();
        dataModels = new ArrayList();
        dataModels.add(new DataModel("Batch#:", false));
        dataModels.add(new DataModel("Batch#", false));
        dataModels.add(new DataModel("Batch#", false));
        dataModels.add(new DataModel("Batch#", false));
        dataModels.add(new DataModel("Batch#", false));

//        adapter = new CustomAdapter(dataModels, getApplicationContext());
//        listView.setAdapter(adapter);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView parent, View view, int position, long id) {
//                DataModel dataModel = (DataModel) dataModels.get(position);
//                dataModel.checked = !dataModel.checked;
//                adapter.notifyDataSetChanged();
//            }
//        });


    }
//    private void initViews() {
//        btnAddGPS = findViewById(R.id.btnAddGPS);
//        btnNewBatch = findViewById(R.id.btnNewBatch);
//        btnEditBatch = findViewById(R.id.btnEditBatch);
//        btnExit = findViewById(R.id.btnExit);
//        DataWrapper dw = (DataWrapper) getIntent().getSerializableExtra("batches");
//        if (dw != null) {
//            batches = dw.getBatches();
//        }
//        //Button Functionality
//        btnAddGPS.setOnClickListener(v -> {
//
//
//
//        });
//
//       // btnNewBatch.setOnClickListener(v -> startActivity(new Intent(EditBatchActivity.this, ScannedBarcodeActivity.class)));
//
//        btnExit.setOnClickListener(v -> startActivity(new Intent(EditBatchActivity.this, MainActivity.class)));
//        btnEditBatch.setOnClickListener(v -> {
//
//            //TODO 1.Only allows a single batch to be selected, if more than one is selected warn user with Toast(small text at bottom of screen) to only select one.
//            //Toast Example:
//            //Toast.makeText(getApplicationContext(), "Barcode Already in Batch", Toast.LENGTH_SHORT).show();
//            //TODO 2.When one batch is selected load a list of the containers in the batch and their details.
//            //TODO 3.A button to delete a container
//            //TODO 4.A button to add a container that sends user to scanning screen to add new containers to that batch.
//            //This will likely require cloning the scanning activity and adding code to add the batch in question, making a addToExistingBatch.activity.
//            //This is because the original scanning activity is built to start on a new batch.
//
//        });
//    }
}
