package com.example.fabianlopezverdugo.mapit;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by fabianlopezverdugo on 10/30/16.
 */

public class MySQLiteHelper extends SQLiteOpenHelper
{

    public static final String TABLE = "visitors";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DESC = "description";
    public static final String COLUMN_PIC = "image";

    public static final String TABLE2 = "places";
    public static final String COLUMN_ID2 = "_id";
    public static final String COLUMN_NAME2 = "name";
    public static final String COLUMN_LAT = "latitude";
    public static final String COLUMN_LON = "longitude";

    private static final String DATABASE_NAME = "mydatabase.db";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE = "create table "
            +TABLE+" ( "+COLUMN_ID
            +" integer primary key autoincrement, "+COLUMN_NAME
            +" text not null, "+COLUMN_DESC
            +" text not null, "+COLUMN_PIC
            +" text not null);" ;

    private static final String DATABASE_CREATE2 = "create table "
            +TABLE2+" ( "+COLUMN_ID2
            +" integer primary key autoincrement, "+COLUMN_NAME2
            +" text not null, "+COLUMN_LAT
            +" double not null, "+COLUMN_LON
            +" double not null);" ;

    public MySQLiteHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        System.out.println(DATABASE_CREATE);

    }

    @Override
    public void onCreate(SQLiteDatabase database){
        database.execSQL(DATABASE_CREATE);
        database.execSQL(DATABASE_CREATE2);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        Log.w(MySQLiteHelper.class.getName(),
                "Upgrading database from version "+oldVersion+" to "
                        +newVersion+", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE2);
        onCreate(db);

    }
}
