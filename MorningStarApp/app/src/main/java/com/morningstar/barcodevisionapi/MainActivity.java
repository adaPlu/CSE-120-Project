package com.morningstar.barcodevisionapi;
/*Ada Pluguez adapluguez@gmail.com - Morning Star Scanning and Tracking V0.2 11/17/20*/

import android.content.Intent;
import android.database.Cursor;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SimpleCursorAdapter;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener {

    Button btnBatchManagement, btnScanBarcode, btnSync, btnSearch;
    private DBManager dbManager;
    private SimpleCursorAdapter adapter;

    //Create database if doesnt exist, connect to existing if exists
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Create or load last instance of the application
        super.onCreate(savedInstanceState);
        //Set starting view to main layout
        setContentView(R.layout.activity_main);

        //Create DB if doesnt exist
        dbManager = new DBManager(this);
        dbManager.open();
        //Cursors for db access?
        Cursor container_cursor = dbManager.fetch_containers();
        Cursor batch_cursor = dbManager.fetch_batches();
        initViews();
    }

    //Setup Screen Transitions on button clicks
    private void initViews() {
        btnScanBarcode = findViewById(R.id.btnScanBarcode);
        btnScanBarcode.setOnClickListener(this);
        btnBatchManagement = findViewById(R.id.btnBatchManagement);
        btnBatchManagement.setOnClickListener(this);
        btnSearch = findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(this);
        btnSync = findViewById(R.id.btnSync);
        btnSync.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        //Launch correct activity based on click, activate screen transitions
        if(v.getId() == R.id.btnScanBarcode){
            startActivity(new Intent(MainActivity.this, ScannedBarcodeActivity.class));
        }
        if(v.getId() == R.id.btnBatchManagement){
            startActivity(new Intent(MainActivity.this, BatchManagementActivity.class));
        }
        if(v.getId() == R.id.btnSearch){
            //startActivity(new Intent(MainActivity.this, SearchActivity.class));
        }
        if(v.getId() == R.id.btnSync){
            //startActivity(new Intent(MainActivity.this, SyncActivity.class));
        }
    }
}
