package com.example.gamingpc.easyplants.Database;

public class Config {

    public static final String DATABASE_NAME = "EasyPlantDB";

    public static final String TABLE_USER = "users";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_USERNAME = "user_name";
    public static final String COLUMN_USER_PASS = "user_pass";
    public static final String COLUMN_USER_EMAIL = "user_email";
    public static final String COLUMN_USER_TYPE = "user_type";
    public static final String COLUMN_USER_SENSOR_COUNT = "user_sensor_count";
    public static final String COLUMN_USER_NOTIFICATION_ON = "user_notification_on";
    public static final String COLUMN_USER_DELETED = "user_deleted";
    public static final String COLUMN_USER_NAME_UNIQUE = "user_name_unique";

    public static final String TABLE_SENSOR_DATA = "sensor_data";
    public static final String COLUMN_DATA_ID = "data_id";
    public static final String COLUMN_SENSOR_ID = "sensor_id";
    public static final String COLUMN_HUMIDITY_VALUE = "humidity_value";
    public static final String COLUMN_HUMIDITY_ALERT = "humidity_alert";
    public static final String COLUMN_DATA_DATE = "data_date";
    public static final String COLUMN_READ_STATUS = "read_status";

    public static final String TABLE_USER_SENSOR_PAIRING = "user_sensor_pairing";
    //public static final String COLUMN_SENSOR_ID = "sensor_id";
    //public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_SENSOR_SECURITY_CODE = "sensor_security_code";
    public static final String COLUMN_PAIRING_SUCCESS = "pairing_success";
    public static final String COLUMN_SENSOR_DELETED = "sensor_deleted";

}
