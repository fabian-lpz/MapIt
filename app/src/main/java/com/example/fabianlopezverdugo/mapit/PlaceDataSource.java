package com.example.fabianlopezverdugo.mapit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fabianlopezverdugo on 10/30/16.
 */

public class PlaceDataSource
{
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = {MySQLiteHelper.COLUMN_ID,MySQLiteHelper.COLUMN_NAME,MySQLiteHelper.COLUMN_LAT,MySQLiteHelper.COLUMN_LON};

    public  PlaceDataSource(Context context){
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close(){
        dbHelper.close();
    }

    public Place createPlace(String name, double lat, double lon){
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_NAME, name);
        values.put(MySQLiteHelper.COLUMN_LAT,lat);
        values.put(MySQLiteHelper.COLUMN_LON,lon);

        long insertId = database.insert(MySQLiteHelper.TABLE2, null,
                values);

        Cursor cursor = database.query(MySQLiteHelper.TABLE2,
                allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
                null,null,null);
        cursor.moveToFirst();
        Place newPlace = cursorToUser(cursor);
        cursor.close();
        return newPlace;
    }

    public void deletePlace(Place place){
        long id = place.getId();
        System.out.println("Comment deleted with id: "+id);
        database.delete(MySQLiteHelper.TABLE2, MySQLiteHelper.COLUMN_ID+" = "+id,null);

    }

    public List<Place> getAllPlaces(){
        List<Place> places = new ArrayList<Place>();
        Cursor cursor = database.query(MySQLiteHelper.TABLE2,
                allColumns,null,null,null,null,null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            Place place = cursorToUser(cursor);
            places.add(place);
            cursor.moveToNext();
        }

        cursor.close();
        return  places;
    }

    private Place cursorToUser(Cursor cursor){
        Place place = new Place();
        place.setId(cursor.getLong(0));
        place.setName(cursor.getString(1));
        place.setLatitude(cursor.getDouble(2));
        place.setLongitude(cursor.getDouble(3));

        return place;
    }
}
