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

public class VisitorDataSource
{
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = {MySQLiteHelper.COLUMN_ID,MySQLiteHelper.COLUMN_NAME,MySQLiteHelper.COLUMN_DESC,MySQLiteHelper.COLUMN_PIC};

    public  VisitorDataSource(Context context){
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close(){
        dbHelper.close();
    }

    public Visitor createUser(String name, String desc,String pic){
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_NAME, name);
        values.put(MySQLiteHelper.COLUMN_DESC,desc);
        values.put(MySQLiteHelper.COLUMN_PIC,pic);

        long insertId = database.insert(MySQLiteHelper.TABLE, null,
                values);

        Cursor cursor = database.query(MySQLiteHelper.TABLE,
                allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
                null,null,null);
        cursor.moveToFirst();
        Visitor newVisitor = cursorToUser(cursor);
        cursor.close();
        return newVisitor;
    }

    public void deleteUser(Visitor visitor){
        long id = visitor.getId();
        System.out.println("Comment deleted with id: "+id);
        database.delete(MySQLiteHelper.TABLE, MySQLiteHelper.COLUMN_ID+" = "+id,null);

    }

    public List<Visitor> getAllVisitors(){
        List<Visitor> visitors = new ArrayList<Visitor>();
        Cursor cursor = database.query(MySQLiteHelper.TABLE,
                allColumns,null,null,null,null,null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            Visitor visitor = cursorToUser(cursor);
            visitors.add(visitor);
            cursor.moveToNext();
        }

        cursor.close();
        return  visitors;
    }

    private Visitor cursorToUser(Cursor cursor){
        Visitor visitor = new Visitor();
        visitor.setId(cursor.getLong(0));
        visitor.setName(cursor.getString(1));
        visitor.setDescription(cursor.getString(2));
        visitor.setPicture(cursor.getString(3));

        return visitor;
    }
}
