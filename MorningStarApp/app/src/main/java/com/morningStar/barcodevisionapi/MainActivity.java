package com.morningstar.barcodevisionapi;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnTakePicture, btnScanBarcode, btnGetLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    private void initViews() {
        btnTakePicture = findViewById(R.id.btnTakePicture);
        btnScanBarcode = findViewById(R.id.btnScanBarcode);
        btnGetLocation = findViewById(R.id.btnGetLocation);
        btnTakePicture.setOnClickListener(this);
        btnScanBarcode.setOnClickListener(this);
        btnGetLocation.setOnClickListener(this);
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
        if (v.getId() == R.id.btnTakePicture) {
            startActivity(new Intent(MainActivity.this, com.morningstar.barcodevisionapi.PictureBarcodeActivity.class));
        }
        if (v.getId() == R.id.btnScanBarcode) {
            startActivity(new Intent(MainActivity.this, com.morningstar.barcodevisionapi.ScannedBarcodeActivity.class));
        }
        if (v.getId() == R.id.btnGetLocation) {
            //Toast.makeText(getApplicationContext(), "Get Location", Toast.LENGTH_LONG).show();
            startActivity(new Intent(MainActivity.this, com.morningstar.barcodevisionapi.BarcodeLocationActivity.class));
        }
    }
}
