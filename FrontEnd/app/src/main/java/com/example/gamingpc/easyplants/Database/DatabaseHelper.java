package com.example.gamingpc.easyplants.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.gamingpc.easyplants.Models.SensorData;
import com.example.gamingpc.easyplants.Models.SensorLinks;
import com.example.gamingpc.easyplants.Models.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = Config.DATABASE_NAME;
    private Context context = null;

    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_USER_TABLE =
        "CREATE TABLE " + Config.TABLE_USER + "(" +
                Config.COLUMN_USER_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                Config.COLUMN_USERNAME + " TEXT NOT NULL, " +
                Config.COLUMN_USER_PASS + " TEXT NOT NULL, " +
                Config.COLUMN_USER_EMAIL + " TEXT NOT NULL, " +
                Config.COLUMN_USER_TYPE + " INTEGER NOT NULL, " +
                Config.COLUMN_USER_SENSOR_COUNT + " INTEGER NOT NULL DEFAULT 0" +
                Config.COLUMN_USER_NOTIFICATION_ON + " INTEGER NOT NULL DEFAULT 1" +
                Config.COLUMN_USER_DELETED + " INTEGER NOT NULL DEFAULT 0" +
                "UNIQUE INDEX " + Config.COLUMN_USER_NAME_UNIQUE + "(" + Config.COLUMN_USER_EMAIL + ")" +
        ")";

        Log.d(TAG, "Table create SQL: " + CREATE_USER_TABLE);

        db.execSQL(CREATE_USER_TABLE);

        String CREATE_SENSOR_DATA_TABLE =
        "CREATE TABLE " + Config.TABLE_SENSOR_DATA + "(" +
                Config.COLUMN_DATA_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                Config.COLUMN_SENSOR_ID + " INTEGER NOT NULL PRIMARY KEY, " +
                Config.COLUMN_HUMIDITY_VALUE + " INTEGER NOT NULL, " +
                Config.COLUMN_HUMIDITY_ALERT + " INTEGER NOT NULL DEFAULT 0, " +
                Config.COLUMN_DATA_DATE + " DATETIME NOT NULL, " +
                Config.COLUMN_READ_STATUS + " INTEGER NOT NULL DEFAULT 0" +
         ")";

        Log.d(TAG, "Table create SQL: " + CREATE_SENSOR_DATA_TABLE);

        db.execSQL(CREATE_SENSOR_DATA_TABLE);

        String CREATE_USER_SENSOR_PAIRING_TABLE =
                "CREATE TABLE " + Config.TABLE_USER_SENSOR_PAIRING + "(" +
                        Config.COLUMN_SENSOR_ID + " INTEGER NOT NULL PRIMARY KEY, " +
                        Config.COLUMN_USER_ID + " INTEGER NOT NULL, " +
                        Config.COLUMN_SENSOR_SECURITY_CODE + " INTEGER NOT NULL, " +
                        Config.COLUMN_PAIRING_SUCCESS + " INTEGER NOT NULL DEFAULT 0, " +
                        Config.COLUMN_SENSOR_DELETED + " INTEGER NOT NULL DEFAULT 0, " +
                        ")";

        Log.d(TAG, "Table create SQL: " + CREATE_USER_SENSOR_PAIRING_TABLE);

        db.execSQL(CREATE_USER_SENSOR_PAIRING_TABLE);

    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        db.execSQL("PRAGMA foreign_keys=ON");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + Config.TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + Config.TABLE_SENSOR_DATA);
        db.execSQL("DROP TABLE IF EXISTS " + Config.TABLE_USER_SENSOR_PAIRING);

        onCreate(db);
    }

    public long insertUser(User user){
        long result = 0;
        //TODO : FILL IN FUNCTION
        return result;
    }

    public long insertSensorData(SensorData sensorData){
        long result = 0;
        //TODO : FILL IN FUNCTION
        return result;
    }

    public long insertUserSensorPairing(SensorLinks link){
        long result = 0;
        //TODO : FILL IN FUNCTION
        return result;
    }

    public List<User> getAllUsers(){
        List<User> result = new ArrayList<User>();
        //TODO : FILL IN FUNCTION
        return result;
    }

    public List<SensorData> getAllSensorData(){
        List<SensorData> result = new ArrayList<>();
        //TODO : FILL IN FUNCTION
        return result;
    }

    public List<SensorLinks> getAllSensorLinks(){
        List<SensorLinks> result = new ArrayList<>();
        //TODO : FILL IN FUNCTION
        return result;
    }

    public List<SensorData> getAllSensorDataBySensorId(int sensorId){
        List<SensorData> result = new ArrayList<>();
        //TODO : FILL IN FUNCTION
        return result;
    }

    public List<SensorData> getAllSensorDataFromSensorIdAndDate(int sensorId, Date date){
        List<SensorData> result = new ArrayList<>();
        //TODO : FILL IN FUNCTION
        return result;
    }

    public SensorData getSensorDataByDataId(int dataId){
        SensorData result = new SensorData();
        //TODO : FILL IN FUNCTION
        return result;
    }

    public User getUserById(int userId){
        User result = new User();
        //TODO : FILL IN FUNCTION
        return result;
    }

    public long updateUserInfo(User user){
        long rowCount = 0;
        //TODO : FILL IN FUNCTION
        return rowCount;
    }

    public long updateSensorData(SensorData sensorData){
        long rowCount = 0;
        //TODO : FILL IN FUNCTION
        return rowCount;
    }

    public long updateSensorLink(SensorLinks sensorLink){
        long rowCount = 0;
        //TODO : FILL IN FUNCTION
        return rowCount;
    }

    public long deleteUserById(int userId){
        long wasDeleted = -1;
        //TODO : FILL IN FUNCTION
        return wasDeleted;
    }

}
