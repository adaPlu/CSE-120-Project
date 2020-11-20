package com.morningstar.barcodevisionapi;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.SimpleCursorAdapter;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

public class SearchListActivity extends AppCompatActivity {

    private DBManager dbManager;

    private ListView listView;

    private SimpleCursorAdapter adapter;
    //Batch or conrtainer? code for both?
    final String[] from = new String[] { DatabaseHelper.b_BatchID,
            DatabaseHelper.b_Latitude, DatabaseHelper.b_Longitude, DatabaseHelper.b_containerCount };

    final int[] to = new int[] { R.id.id, R.id.title, R.id.desc };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setContentView(R.layout.fragment_emp_list);

        dbManager = new DBManager(this);
        dbManager.open();
        Cursor container_cursor = dbManager.fetch_containers();
        Cursor batch_cursor = dbManager.fetch_batches();

        //listView = (ListView) findViewById(R.id.list_view);
        //listView.setEmptyView(findViewById(R.id.empty));

        adapter = new SimpleCursorAdapter(this, R.layout.activity_view_record, batch_cursor, from, to, 0);
        adapter.notifyDataSetChanged();

        listView.setAdapter(adapter);

        // OnCLickListiner For List Items
        listView.setOnItemClickListener((AdapterView.OnItemClickListener) (parent, view, position, viewId) -> {
            TextView idTextView = (TextView) view.findViewById(R.id.id);
            TextView titleTextView = (TextView) view.findViewById(R.id.title);
            TextView descTextView = (TextView) view.findViewById(R.id.desc);

            String id = idTextView.getText().toString();
            String title = titleTextView.getText().toString();
            String desc = descTextView.getText().toString();

            Intent modify_intent = new Intent(getApplicationContext(), ModifyCountryActivity.class);
            modify_intent.putExtra("title", title);
            modify_intent.putExtra("desc", desc);
            modify_intent.putExtra("id", id);

            startActivity(modify_intent);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.add_row_section) {

            Intent add_mem = new Intent(this, AddSectionRowActivity.class);
            startActivity(add_mem);

        }
        return super.onOptionsItemSelected(item);
    }

}