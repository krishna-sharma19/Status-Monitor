package com.example.admin.statusmonitor;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by admin on 10/03/17.
 */

public class DataResource extends SQLiteOpenHelper{
    private static final String DB_NAME = "Uni_DB";
    private static final String create_table_UniLinks = "create table UniLinks(UniName varchar(100) primary key,UniLink varchar(9000));";
    //private static final String create_table_UniDetails = "create table UniD(UniName varchar(100),FieldName varchar(100),FieldValue varchar(100),FieldId varchar(100));";
    private static final String create_table_UniDetails = "create table UniD(UniName varchar(100),FieldId varchar(100),FieldValue varchar(100));";
    public DataResource(Context c)
    {
        super(c,DB_NAME,null,2);
        System.out.println("inside constructor");
    }
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        sqLiteDatabase.execSQL(create_table_UniLinks);
        sqLiteDatabase.execSQL(create_table_UniDetails);
        System.out.println("Tables created!");

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("drop table UniLinks");
        db.execSQL("drop table UniD");
        db.execSQL(create_table_UniLinks);
        db.execSQL(create_table_UniDetails);
        System.out.println("Tables schema changeded!");



    }


}
