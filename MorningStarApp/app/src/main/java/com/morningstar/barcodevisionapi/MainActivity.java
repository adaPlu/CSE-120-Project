package com.morningstar.barcodevisionapi;


import android.content.Intent;
import android.database.Cursor;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SimpleCursorAdapter;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener {

    Button btnTakePicture, btnScanBarcode;

    private DBManager dbManager;
    private SimpleCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbManager = new DBManager(this);
        dbManager.open();
        Cursor container_cursor = dbManager.fetch_containers();
        Cursor batch_cursor = dbManager.fetch_batches();

        initViews();
    }

    private void initViews() {
        btnTakePicture = findViewById(R.id.btnTakePicture);
        btnScanBarcode = findViewById(R.id.btnScanBarcode);
        btnTakePicture.setOnClickListener(this);
        btnScanBarcode.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        /*
        switch (v.getId()) {
            case R.id.btnTakePicture:
                startActivity(new Intent(MainActivity.this, PictureBarcodeActivity.class));
                break;
            case R.id.btnScanBarcode:
                startActivity(new Intent(MainActivity.this, ScannedBarcodeActivity.class));
                break;
        }
        */
        if(v.getId() == R.id.btnTakePicture){
            startActivity(new Intent(MainActivity.this, PictureBarcodeActivity.class));
        }
        if(v.getId() == R.id.btnScanBarcode){
            startActivity(new Intent(MainActivity.this, ScannedBarcodeActivity.class));
        }
    }
}
