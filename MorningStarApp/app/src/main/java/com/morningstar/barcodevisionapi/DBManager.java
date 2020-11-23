package com.morningstar.barcodevisionapi;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DBManager {

    private DatabaseHelper dbHelper;

    private Context context;

    private SQLiteDatabase database;

    public DBManager(Context c) {
        context = c;
    }

    public DBManager open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public void insert_container(int batchID, String barcode, String date, int row, int section) {
        ContentValues contentValue = new ContentValues();
        contentValue.put("c_BatchID", batchID);
        contentValue.put("c_Barcode", barcode);
        contentValue.put("c_Date", date);
        contentValue.put("c_Row", row);
        contentValue.put("c_Section", section);
        database.insert("Container", null, contentValue);
    }
    public void insert_batch(String batchID, int count) {
        double latitude = 0;
        double longitude = 0;
        ContentValues contentValue = new ContentValues();
        contentValue.put("b_batchID", batchID);
        contentValue.put("b_Latitude", latitude);
        contentValue.put("b_longitude", longitude);
        contentValue.put("b_containerCount", count);
        database.insert("Batch", null, contentValue);
    }


    public Cursor search_containers_barcode(String barcode) {
        String[] columns = new String[]{"c_BatchID", "c_Barcode", "c_Date", "c_Row", "c_Section"};
        Cursor cursor = database.query("Container", null, "c_Barcode = " + barcode, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor search_containers_batchID(String batchID) {
        String[] columns = new String[]{"c_BatchID", "c_Barcode", "c_Date", "c_Row", "c_Section"};
        Cursor cursor = database.query("Container", null, "c_batchID = " + batchID, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor search_batches(String batchID) {
        String[] columns = new String[] {"b_BatchID", "b_Latitude", "b_Longitude", "b_containerCount"};
        Cursor cursor = database.query("Batch", null, "b_BatchID = " + batchID, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor fetch_containers() {
        String[] columns = new String[] {"c_BatchID", "c_Barcode", "c_Date", "c_Row", "c_Section"};
        Cursor cursor = database.query("Container", null, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor fetch_batches() {
        String[] columns = new String[] {"b_BatchID", "b_Latitude", "b_Longitude", "b_containerCount"};
        Cursor cursor = database.query("Batch", null, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    //Update Functions
    public int update_container(int batchID, String barcode, String date, int row, int section) {
        ContentValues contentValue = new ContentValues();
        contentValue.put("c_BatchID", batchID);
        contentValue.put("c_Barcode", barcode);
        contentValue.put("c_Date", date);
        contentValue.put("c_Row", row);
        contentValue.put("c_Section", section);
        return database.update("Container", contentValue, "c_Barcode = " + barcode, null);
    }
    public int update_batch(int batchID, double latitude, double longitude, int containerCount) {
        ContentValues contentValue = new ContentValues();
        contentValue.put("b_Latitude", latitude);
        contentValue.put("b_longitude", longitude);
        contentValue.put("b_containerCount", containerCount);
        return database.update("Batch", contentValue, "b_BatchID = " + batchID, null);
    }
    public int update_batchCount(String batchID, int containerCount) {
        ContentValues contentValue = new ContentValues();
        contentValue.put("b_containerCount", containerCount);
        return database.update("Batch", contentValue, "b_BatchID = " + batchID, null);
    }

    public int update_batchGPS(String batchID, double latitude, double longitude) {
        ContentValues contentValue = new ContentValues();
        contentValue.put("b_Latitude", latitude);
        contentValue.put("b_longitude", longitude);
        return database.update("Batch", contentValue, "b_BatchID = " + batchID, null);
    }

    public void delete_container(String barcode) {
        database.delete("Container", "c_Barcode = " + barcode, null);
    }
    public void delete_batch(int batchID) {
        database.delete("Batch", "b_BatchID" + batchID, null);
    }
}