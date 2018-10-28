package com.example.gamingpc.easyplants.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.example.gamingpc.easyplants.Models.Password;
import com.example.gamingpc.easyplants.Models.SensorData;
import com.example.gamingpc.easyplants.Models.SensorLinks;
import com.example.gamingpc.easyplants.Models.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = Config.DATABASE_NAME;
    private Context context = null;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

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
                Config.COLUMN_USER_TYPE + " INTEGER NOT NULL DEFAULT 0, " +
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
        long id = -1;
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Config.COLUMN_USERNAME, user.getUserName());
        contentValues.put(Config.COLUMN_USER_PASS, user.getUserPass());
        contentValues.put(Config.COLUMN_USER_EMAIL, user.getUserEmail());

        try{
            id = db.insertOrThrow(Config.TABLE_USER, null, contentValues);
        }
        catch(SQLException e){
            Log.d(TAG, "Exception: " + e.getMessage());
            Toast.makeText(context, "Operation failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        finally{
            db.close();
        }

        return id;
    }

    public long insertSensorData(SensorData sensorData){
        long id = -1;
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Config.COLUMN_SENSOR_ID, sensorData.getSensorId());
        contentValues.put(Config.COLUMN_HUMIDITY_VALUE, sensorData.getHumidityValue());
        contentValues.put(Config.COLUMN_HUMIDITY_ALERT, sensorData.getHumidityAlert());
        contentValues.put(Config.COLUMN_DATA_DATE, dateFormat.format(sensorData.getAlertDate()));

        try{
            id = db.insertOrThrow(Config.TABLE_SENSOR_DATA, null, contentValues);
        }
        catch(SQLException e){
            Log.d(TAG, "Exception: " + e.getMessage());
            Toast.makeText(context, "Operation failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        finally{
            db.close();
        }

        return id;
    }

    public long insertUserSensorPairing(SensorLinks link){
        long id = -1;

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Config.COLUMN_SENSOR_ID, link.getSensorId());
        contentValues.put(Config.COLUMN_USER_ID, link.getUserId());
        contentValues.put(Config.COLUMN_SENSOR_SECURITY_CODE, link.getSensorSecurityCode());

        try{
        id = db.insertOrThrow(Config.TABLE_USER_SENSOR_PAIRING, null, contentValues);
        }
        catch(SQLException e){
            Log.d(TAG, "Exception: " + e.getMessage());
            Toast.makeText(context, "Operation failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        finally{
            db.close();
        }


        return id;
    }

    public List<User> getAllUsers(){
        List<User> result = new ArrayList<User>();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor userCursor = null;

        try{

            userCursor = db.rawQuery("SELECT * FROM " + Config.TABLE_USER + " WHERE " +
                    Config.COLUMN_USER_DELETED + " = " + 0, null);

            if(userCursor != null && userCursor.getCount()>0){
                if(userCursor.moveToFirst()){
                    do{
                        int userId = userCursor.getInt(userCursor.getColumnIndex(Config.COLUMN_USER_ID));
                        String userName = userCursor.getString(userCursor.getColumnIndex(Config.COLUMN_USERNAME));
                        String userPass = userCursor.getString(userCursor.getColumnIndex(Config.COLUMN_USER_PASS));
                        String userEmail = userCursor.getString(userCursor.getColumnIndex(Config.COLUMN_USER_EMAIL));
                        int userType = userCursor.getInt(userCursor.getColumnIndex(Config.COLUMN_USER_TYPE));
                        int userSensorCount = userCursor.getInt(userCursor.getColumnIndex(Config.COLUMN_USER_SENSOR_COUNT));
                        int userNotificationOn = userCursor.getInt(userCursor.getColumnIndex(Config.COLUMN_USER_NOTIFICATION_ON));
                        int userDeleted = userCursor.getInt(userCursor.getColumnIndex(Config.COLUMN_USER_DELETED));

                        result.add(new User(userId, userName ,userPass, userEmail, userType, userSensorCount, userNotificationOn, userDeleted));
                    }while(userCursor.moveToNext());

                    return result;
                }
            }

        }
        catch(SQLException e){
            Log.d(TAG, "Exception " + e.getMessage());
            Toast.makeText(context, "Operation failed - getAllUsers", Toast.LENGTH_LONG).show();
        }
        finally{
            if(userCursor != null){
                userCursor.close();
            }
            db.close();
        }

        return result;
    }

    public List<SensorData> getAllSensorData(){
        List<SensorData> result = new ArrayList<>();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor dataCursor = null;

        try{

            dataCursor = db.query(Config.TABLE_SENSOR_DATA, null, null, null, null, null,null);

            if(dataCursor != null && dataCursor.getCount()>0){
                if(dataCursor.moveToFirst()){
                    do{
                        try {

                            int dataId = dataCursor.getInt(dataCursor.getColumnIndex(Config.COLUMN_DATA_ID));
                            int sensorId = dataCursor.getInt(dataCursor.getColumnIndex(Config.COLUMN_SENSOR_ID));
                            int humidityValue = dataCursor.getInt(dataCursor.getColumnIndex(Config.COLUMN_HUMIDITY_VALUE));
                            int humidityAlert = dataCursor.getInt(dataCursor.getColumnIndex(Config.COLUMN_HUMIDITY_ALERT));
                            Date alertDate = dateFormat.parse(dataCursor.getString(dataCursor.getColumnIndex(Config.COLUMN_DATA_DATE)));
                            int readStatus = dataCursor.getInt(dataCursor.getColumnIndex(Config.COLUMN_READ_STATUS));

                            result.add(new SensorData(dataId, sensorId, humidityValue, humidityAlert, alertDate, readStatus));
                        }
                        catch(ParseException parseExce){
                            Log.d(TAG, "Exception " + parseExce.getMessage());
                            Toast.makeText(context, "Operation failed - Date parsing", Toast.LENGTH_LONG).show();
                        }
                    }while(dataCursor.moveToNext());

                    return result;
                }
            }

        }
        catch(SQLException e){
            Log.d(TAG, "Exception " + e.getMessage());
            Toast.makeText(context, "Operation failed - getAllSensorData", Toast.LENGTH_LONG).show();
        }
        finally {
            if(dataCursor != null){
                dataCursor.close();
            }
            db.close();
        }


        return result;
    }

    public List<SensorLinks> getAllSensorLinks(){
        List<SensorLinks> result = new ArrayList<>();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor sensorLinkCursor = null;

        try{

            sensorLinkCursor = db.rawQuery("SELECT * FROM " + Config.TABLE_USER_SENSOR_PAIRING +
                    " WHERE " + Config.COLUMN_SENSOR_DELETED + " = " + 0, null);

            if(sensorLinkCursor != null && sensorLinkCursor.getCount()>0){
                if(sensorLinkCursor.moveToFirst()){
                    do{
                        int sensorId = sensorLinkCursor.getInt(sensorLinkCursor.getColumnIndex(Config.COLUMN_SENSOR_ID));
                        String sensorSecurityCode = sensorLinkCursor.getString(sensorLinkCursor.getColumnIndex(Config.COLUMN_USER_ID));
                        int sensorPairingSuccess = sensorLinkCursor.getInt(sensorLinkCursor.getColumnIndex(Config.COLUMN_SENSOR_SECURITY_CODE));
                        int sensorDeleted = sensorLinkCursor.getInt(sensorLinkCursor.getColumnIndex(Config.COLUMN_PAIRING_SUCCESS));
                        int userId = sensorLinkCursor.getInt(sensorLinkCursor.getColumnIndex(Config.COLUMN_SENSOR_DELETED));

                        result.add(new SensorLinks(sensorId, userId, sensorSecurityCode, sensorPairingSuccess, sensorDeleted));
                    }while(sensorLinkCursor.moveToNext());

                    return result;
                }
            }

        }
        catch(SQLException e){
            Log.d(TAG, "Exception " + e.getMessage());
            Toast.makeText(context, "Operation failed - getAllSensorLinks", Toast.LENGTH_LONG).show();
        }
        finally{
            if(sensorLinkCursor != null){
                sensorLinkCursor.close();
            }
            db.close();
        }

        return result;
    }

    public SensorLinks getSensorLinkByIds(int sensorId, int userId){

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor sensorLinkCursor = null;

        try{

            sensorLinkCursor = db.rawQuery("SELECT * FROM " + Config.TABLE_USER_SENSOR_PAIRING + " WHERE " +
                    Config.COLUMN_SENSOR_ID + " = " + sensorId + " AND WHERE " +
                    Config.COLUMN_USER_ID + " = " + userId + " AND WHERE " +
                    Config.COLUMN_SENSOR_DELETED + " = " + 0,null);



                if(sensorLinkCursor.moveToFirst()){

                        String sensorSecurityCode = sensorLinkCursor.getString(sensorLinkCursor.getColumnIndex(Config.COLUMN_USER_ID));
                        int sensorPairingSuccess = sensorLinkCursor.getInt(sensorLinkCursor.getColumnIndex(Config.COLUMN_SENSOR_SECURITY_CODE));
                        int sensorDeleted = sensorLinkCursor.getInt(sensorLinkCursor.getColumnIndex(Config.COLUMN_PAIRING_SUCCESS));

                        return new SensorLinks(sensorId, userId, sensorSecurityCode, sensorPairingSuccess, sensorDeleted);

                }

        }
        catch(SQLException e){
            Log.d(TAG, "Exception " + e.getMessage());
            Toast.makeText(context, "Operation failed - getAllSensorLinks", Toast.LENGTH_LONG).show();
        }
        finally{
            if(sensorLinkCursor != null){
                sensorLinkCursor.close();
            }
            db.close();
        }

        return null;
    }

    public List<SensorData> getAllSensorDataBySensorId(int sensorId){
        List<SensorData> result = new ArrayList<>();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor dataCursor = null;

        try{

            dataCursor = db.rawQuery("SELECT * FROM " + Config.TABLE_SENSOR_DATA +
                    " WHERE " + Config.COLUMN_SENSOR_ID + " = " + sensorId,null);

            if(dataCursor != null && dataCursor.getCount()>0){
                if(dataCursor.moveToFirst()){
                    do{
                        try {

                            int dataId = dataCursor.getInt(dataCursor.getColumnIndex(Config.COLUMN_DATA_ID));
                            int humidityValue = dataCursor.getInt(dataCursor.getColumnIndex(Config.COLUMN_HUMIDITY_VALUE));
                            int humidityAlert = dataCursor.getInt(dataCursor.getColumnIndex(Config.COLUMN_HUMIDITY_ALERT));
                            Date alertDate = dateFormat.parse(dataCursor.getString(dataCursor.getColumnIndex(Config.COLUMN_DATA_DATE)));
                            int readStatus = dataCursor.getInt(dataCursor.getColumnIndex(Config.COLUMN_READ_STATUS));

                            result.add(new SensorData(dataId, sensorId, humidityValue, humidityAlert, alertDate, readStatus));
                        }
                        catch(ParseException parseExce){
                            Log.d(TAG, "Exception " + parseExce.getMessage());
                            Toast.makeText(context, "Operation failed - Date parsing", Toast.LENGTH_LONG).show();
                        }
                    }while(dataCursor.moveToNext());

                    return result;
                }
            }

        }
        catch(SQLException e){
            Log.d(TAG, "Exception " + e.getMessage());
            Toast.makeText(context, "Operation failed - getAllSensorDataBySensorId", Toast.LENGTH_LONG).show();
        }
        finally {
            if(dataCursor != null){
                dataCursor.close();
            }
            db.close();
        }

        return result;
    }

    public List<SensorData> getAllSensorDataFromSensorIdAndDate(int sensorId, Date date){
        List<SensorData> result = new ArrayList<>();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor dataCursor = null;

        try{

            dataCursor = db.rawQuery("SELECT * FROM " + Config.TABLE_SENSOR_DATA +
                    " WHERE " + Config.COLUMN_SENSOR_ID + " = " + sensorId + " AND WHERE " +
                    Config.COLUMN_DATA_DATE + " = " + dateFormat.format(date),null);

            if(dataCursor != null && dataCursor.getCount()>0){
                if(dataCursor.moveToFirst()){
                    do{
                        try {

                            int dataId = dataCursor.getInt(dataCursor.getColumnIndex(Config.COLUMN_DATA_ID));
                            int humidityValue = dataCursor.getInt(dataCursor.getColumnIndex(Config.COLUMN_HUMIDITY_VALUE));
                            int humidityAlert = dataCursor.getInt(dataCursor.getColumnIndex(Config.COLUMN_HUMIDITY_ALERT));
                            Date alertDate = dateFormat.parse(dataCursor.getString(dataCursor.getColumnIndex(Config.COLUMN_DATA_DATE)));
                            int readStatus = dataCursor.getInt(dataCursor.getColumnIndex(Config.COLUMN_READ_STATUS));

                            result.add(new SensorData(dataId, sensorId, humidityValue, humidityAlert, alertDate, readStatus));
                        }
                        catch(ParseException parseExce){
                            Log.d(TAG, "Exception " + parseExce.getMessage());
                            Toast.makeText(context, "Operation failed - Date parsing", Toast.LENGTH_LONG).show();
                        }
                    }while(dataCursor.moveToNext());

                    return result;
                }
            }

        }
        catch(SQLException e){
            Log.d(TAG, "Exception " + e.getMessage());
            Toast.makeText(context, "Operation failed - getAllSensorDataBySensorId", Toast.LENGTH_LONG).show();
        }
        finally {
            if(dataCursor != null){
                dataCursor.close();
            }
            db.close();
        }

        return result;
    }

    public SensorData getSensorDataByDataId(int dataId){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor dataCursor = null;

        try{

            dataCursor = db.rawQuery("SELECT * FROM " + Config.TABLE_SENSOR_DATA +
                    " WHERE " + Config.COLUMN_DATA_ID + " = " + dataId ,null);

            if(dataCursor != null && dataCursor.getCount()>0){
                if(dataCursor.moveToFirst()){
                        try {

                            int sensorId = dataCursor.getInt(dataCursor.getColumnIndex(Config.COLUMN_SENSOR_ID));
                            int humidityValue = dataCursor.getInt(dataCursor.getColumnIndex(Config.COLUMN_HUMIDITY_VALUE));
                            int humidityAlert = dataCursor.getInt(dataCursor.getColumnIndex(Config.COLUMN_HUMIDITY_ALERT));
                            Date alertDate = dateFormat.parse(dataCursor.getString(dataCursor.getColumnIndex(Config.COLUMN_DATA_DATE)));
                            int readStatus = dataCursor.getInt(dataCursor.getColumnIndex(Config.COLUMN_READ_STATUS));

                            return new SensorData(dataId, sensorId, humidityValue, humidityAlert, alertDate, readStatus);
                        }
                        catch(ParseException parseExce){
                            Log.d(TAG, "Exception " + parseExce.getMessage());
                            Toast.makeText(context, "Operation failed - Date parsing", Toast.LENGTH_LONG).show();
                        }

                    return null;
                }
            }

        }
        catch(SQLException e){
            Log.d(TAG, "Exception " + e.getMessage());
            Toast.makeText(context, "Operation failed - getAllSensorDataBySensorId", Toast.LENGTH_LONG).show();
        }
        finally {
            if(dataCursor != null){
                dataCursor.close();
            }
            db.close();
        }

        return null;
    }

    public User getUserById(int userId){

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor userCursor = null;

        try{
            userCursor = db.rawQuery(
                    "SELECT * FROM " + Config.TABLE_USER + " WHERE " +
                         Config.COLUMN_USER_ID + " = " + userId + " AND WHERE "
                    + Config.COLUMN_USER_DELETED + " = " + 0,null);

            if(userCursor.moveToFirst()) {
                String userName = userCursor.getString(userCursor.getColumnIndex(Config.COLUMN_USERNAME));
                String userPass = userCursor.getString(userCursor.getColumnIndex(Config.COLUMN_USER_PASS));
                String userEmail = userCursor.getString(userCursor.getColumnIndex(Config.COLUMN_USER_EMAIL));
                int userType = userCursor.getInt(userCursor.getColumnIndex(Config.COLUMN_USER_TYPE));
                int userSensorCount = userCursor.getInt(userCursor.getColumnIndex(Config.COLUMN_USER_SENSOR_COUNT));
                int userNotificationOn = userCursor.getInt(userCursor.getColumnIndex(Config.COLUMN_USER_NOTIFICATION_ON));
                int userDeleted = userCursor.getInt(userCursor.getColumnIndex(Config.COLUMN_USER_DELETED));

                return new User(userId, userName, userPass, userEmail, userType, userSensorCount, userNotificationOn, userDeleted);

            }
        }
        catch(SQLException e){
            Log.d(TAG, "Exception: " + e.getMessage());
            Toast.makeText(context, "Operation failed", Toast.LENGTH_LONG).show();
        }
        finally{
            if(userCursor != null){
                userCursor.close();
            }
            db.close();
        }

        return null;
    }

    public long updateUserInfo(User user){
        long rowCount = 0;
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Config.COLUMN_USERNAME, user.getUserName());
        contentValues.put(Config.COLUMN_USER_PASS, user.getUserPass());
        contentValues.put(Config.COLUMN_USER_EMAIL, user.getUserEmail());
        contentValues.put(Config.COLUMN_USER_TYPE, user.getUserType());
        contentValues.put(Config.COLUMN_USER_NOTIFICATION_ON, user.getUserNotificationOn());
        contentValues.put(Config.COLUMN_USER_DELETED, user.getUserDeleted());

        try{
            rowCount = db.update(Config.TABLE_USER, contentValues,
                    Config.COLUMN_USER_ID + " = " + user.getUserId(),null);
        }
        catch(SQLException e){
            Log.d(TAG, "Exception: " + e.getMessage());
            Toast.makeText(context, "Operation failed", Toast.LENGTH_LONG).show();
        }
        finally{
            db.close();
        }
        return rowCount;
    }

    public long updateSensorData(SensorData sensorData){
        long rowCount = 0;

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Config.COLUMN_SENSOR_ID, sensorData.getSensorId());
        contentValues.put(Config.COLUMN_HUMIDITY_VALUE, sensorData.getHumidityValue());
        contentValues.put(Config.COLUMN_HUMIDITY_ALERT, sensorData.getHumidityAlert());
        contentValues.put(Config.COLUMN_DATA_DATE, dateFormat.format(sensorData.getAlertDate()));
        contentValues.put(Config.COLUMN_READ_STATUS, sensorData.getReadStatus());

        try{
            rowCount = db.update(Config.TABLE_SENSOR_DATA, contentValues,
                    Config.COLUMN_DATA_ID + " = " + sensorData.getDataId(),null);
        }
        catch(SQLException e){
            Log.d(TAG, "Exception: " + e.getMessage());
            Toast.makeText(context, "Operation failed", Toast.LENGTH_LONG).show();
        }
        finally{
            db.close();
        }

        return rowCount;
    }

    public long updateSensorLink(SensorLinks sensorLink){
        long rowCount = 0;
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Config.COLUMN_SENSOR_SECURITY_CODE, sensorLink.getSensorSecurityCode());
        contentValues.put(Config.COLUMN_SENSOR_DELETED, sensorLink.getSensorDeleted());
        contentValues.put(Config.COLUMN_PAIRING_SUCCESS, sensorLink.getSensorPairingSuccess());

        try{
            rowCount = db.update(Config.TABLE_USER, contentValues,
                    Config.COLUMN_SENSOR_ID + " = " + sensorLink.getSensorId() + " AND WHERE " +
                    Config.COLUMN_USER_ID + " = " + sensorLink.getUserId(),null);
        }
        catch(SQLException e){
            Log.d(TAG, "Exception: " + e.getMessage());
            Toast.makeText(context, "Operation failed", Toast.LENGTH_LONG).show();
        }
        finally{
            db.close();
        }
        return rowCount;
    }

    public long deleteUserById(int userId){
        long rowCount = 0;
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Config.COLUMN_USER_DELETED, 1);

        try{
            rowCount = db.update(Config.TABLE_USER, contentValues,
                    Config.COLUMN_USER_ID + " = " + userId,null);
        }
        catch(SQLException e){
            Log.d(TAG, "Exception: " + e.getMessage());
            Toast.makeText(context, "Operation failed", Toast.LENGTH_LONG).show();
        }
        finally{
            db.close();
        }
        return rowCount;
    }

    public long deleteSensorLinkByIds(int sensorId, int userId){
        long rowCount = 0;
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Config.COLUMN_SENSOR_DELETED, 1);

        try{
            rowCount = db.update(Config.TABLE_USER_SENSOR_PAIRING, contentValues,
                    Config.COLUMN_SENSOR_ID + " = " + sensorId + " AND WHERE " +
                            Config.COLUMN_USER_ID + " = " + userId,null);
        }
        catch(SQLException e){
            Log.d(TAG, "Exception: " + e.getMessage());
            Toast.makeText(context, "Operation failed", Toast.LENGTH_LONG).show();
        }
        finally{
            db.close();
        }
        return rowCount;
    }

}
