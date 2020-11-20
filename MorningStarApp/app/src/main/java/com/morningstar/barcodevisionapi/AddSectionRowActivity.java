
package com.morningstar.barcodevisionapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class AddSectionRowActivity extends Activity implements OnClickListener {

    private Button btnAddSectionRow;
    private EditText rowEditText;
    private EditText sectionEditText;

    private DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(getString(R.string.add_sectionrow));
        setContentView(R.layout.activity_add_section_row);

        rowEditText = (EditText) findViewById(R.id.subject_edittext);
        sectionEditText = (EditText) findViewById(R.id.description_edittext);

        btnAddSectionRow = (Button) findViewById(R.id.add_row_section);

        dbManager = new DBManager(this);
        dbManager.open();
        btnAddSectionRow.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.add_row_section) {


                final String row = rowEditText.getText().toString();
                final String section = sectionEditText.getText().toString();

                //dbManager.insert(name, desc);
                //dbManager.insert_container();
                //dbManager.insert_batch();
                Intent main = new Intent(AddSectionRowActivity.this, ScannedBarcodeActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                startActivity(main);
        }
    }

}