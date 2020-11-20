package com.morningstar.barcodevisionapi;
/*Ada Pluguez - Morning Star Scanning and Tracking V0.2 11/17/20
 * Description: Allows user to search by barcode via scan or manual entry?
 * Ideally displays a google map of the batch GPS position --See Reference Below
 * https://developers.google.com/maps/documentation/urls/android-intents
 * */
import android.content.Intent;
import android.database.Cursor;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.os.Bundle;

import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;


public class SearchActivity extends AppCompatActivity{
    //Variables
    private DBManager dbManager;
    private SimpleCursorAdapter adapter;
    //Widgets
    SearchView searchBar;


    //Same as main
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

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
        //searchBar = findViewById(R.id.search);


    }

}
