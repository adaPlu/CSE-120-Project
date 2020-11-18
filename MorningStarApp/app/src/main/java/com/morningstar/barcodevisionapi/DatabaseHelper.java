package com.morningstar.barcodevisionapi;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Table columns
    public static final String c_BatchID = "_id";
    public static final String c_Barcode = "subject";
    public static final String c_Date = "description";
    public static final String c_Row = "_id";
    public static final String c_Section = "subject";
    public static final String b_BatchID = "description";
    public static final String b_Latitude = "_id";
    public static final String b_Longitude = "subject";
    public static final String b_containerCount = "description";
    // Database Information
    static final String DB_NAME = "MorningStar.DB";

    // database version
    static final int DB_VERSION = 1;

    // Creating table query
    private static final String CREATE_CONTAINER_TABLE = "create table Container(" +
            "c_BatchID INTEGER," +
            "c_Barcode TEXT," +
            "c_Date DATETIME," +
            "c_Row INTEGER," +
            "c_Section INTEGER);";

    private static final String CREATE_BATCH_TABLE = "create table Batch(" +
            "b_BatchID INTEGER PRIMARY KEY AUTOINCREMENT," +
            "b_Latitude REAL," +
            "b_Longitude REAL," +
            "b_containerCount INTEGER);";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CONTAINER_TABLE);
        db.execSQL(CREATE_BATCH_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Container");
        db.execSQL("DROP TABLE IF EXISTS Batch");
        onCreate(db);
    }
}