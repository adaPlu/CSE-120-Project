package com.morningstar.barcodevisionapi;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.morningStar.barcodevisionapi.SearchActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button  btnScanBarcode, btnBatch, btnSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    private void initViews() {
       // btnTakePicture = findViewById(R.id.btnTakePicture);
        btnScanBarcode = findViewById(R.id.btnScanBarcode);
        btnBatch = findViewById(R.id.btnBatch);
        btnSearch = findViewById(R.id.btnSearch);
       // btnTakePicture.setOnClickListener(this);
        btnScanBarcode.setOnClickListener(this);
        btnBatch.setOnClickListener(this);
        btnSearch.setOnClickListener(this);
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
//        if(v.getId() == R.id.btnTakePicture){
//            startActivity(new Intent(MainActivity.this, PictureBarcodeActivity.class));
//        }
        if(v.getId() == R.id.btnScanBarcode){
            startActivity(new Intent(MainActivity.this, ScannedBarcodeActivity.class));
        }
        if(v.getId() == R.id.btnBatch){
            startActivity(new Intent(MainActivity.this, BatchManagement.class));
        }

        if(v.getId() == R.id.btnSearch){
            startActivity(new Intent(MainActivity.this, SearchActivity.class));
        }
    }
}
