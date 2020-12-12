package com.morningstar.barcodevisionapi;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

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
    ArrayList<String> StringArray = new ArrayList<String>();

    ///TextView textView;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_batch);
        //textView = findViewById(R.id.textView2);
//        Intent main = getIntent();
//        Bundle args = main.getBundleExtra("BUNDLE");
//        ArrayList<Container> containers = (ArrayList<Container>) args.getSerializable("batches");
        //textView.setText(Container.get(containers));
        //Container cnt1 = new Container(8, 1, '1996-01-15', 8, 13);
//        ArrayList<Container> containers = (ArrayList<Container>)getIntent().getSerializableExtra("batches");
//ArrayValueAddFunction();
//        //        //int r = containers.get(i).getRow();
        //containers.add(cnt1);
//        LinearLayout LinearLayoutView = new LinearLayout(this);
//        TextView DisplayStringArray = new TextView(this);
//        DisplayStringArray.setTextSize(25);
//        LinearLayoutView.addView(DisplayStringArray);
//        for (int i=0; i<containers.size();i++){
//            DisplayStringArray.append((CharSequence) containers.get(i));
//            DisplayStringArray.append("\n");
//        }
//        setContentView(LinearLayoutView);

        StringArray.add("8, 1, '1996-01-15', 8, 13");
        StringArray.add("1, 1, '1996-05-01', 4, 10");
        StringArray.add("2, 1, '1996-03-15', 3, 2");
        StringArray.add("3, 1, '1996-02-01', 8, 4");
        StringArray.add("4, 2, '1996-01-15', 7, 10");
        StringArray.add("5, 2, '1996-05-01', 4, 10");
        StringArray.add("6, 3, '1996-03-15', 8, 2");
        StringArray.add("7, 4, '1996-02-01', 8, 4");



        LinearLayout LinearLayoutView = new LinearLayout(this);
        TextView DisplayStringArray = new TextView(this);
        DisplayStringArray.setTextSize(25);
        LinearLayoutView.addView(DisplayStringArray);
        for (int i=0; i<StringArray.size();i++){
            DisplayStringArray.append((CharSequence) StringArray.get(i));
            DisplayStringArray.append("\n");
        }
        setContentView(LinearLayoutView);


//    Intent main = getIntent();
//        Bundle e_batch = main.getExtras();
//        if(e_batch != null){
//            String getbatch = (String) e_batch.get("batches");
//
//        }
    }

//    private void ArrayValueAddFunction() {
//        //containers.add(cnt1);

//
//    }

}
