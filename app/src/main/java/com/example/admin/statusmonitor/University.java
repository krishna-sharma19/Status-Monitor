package com.example.admin.statusmonitor;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;

import se.simbio.encryption.Encryption;

/**
 * Created by admin on 10/03/17.
 */

public class University {
    private String UniName;
    private String UniLink;
    //private HashMap<String,String> hmfieldNamesValues;
    //private HashMap<String,String> hmfieldNamesIds;
    private HashMap<String,String> hmIdValues;
    private ArrayList<String> IdNames;
    private SQLiteDatabase database;
    private  SQLiteDatabase databaseRead;
    private DataResource dr;
    private Context c;

    public HashMap<String, String> getHmIdValues() {
        return hmIdValues;
    }

    public University(Context c, String UniName, String UniLink, HashMap<String,String>hmIdValues, ArrayList<String>names)//hmfieldNamesValues,HashMap<String,String>hmfieldNamesIds
    {
        this.c = c;
        this.UniName = UniName;
        this.UniLink = UniLink;
        //this.hmfieldNamesIds = hmfieldNamesIds;
        //this.hmfieldNamesValues = hmfieldNamesValues;
        this.hmIdValues = hmIdValues;
        this.IdNames = names;
        dr = new DataResource(c);
    }
    public University(Context context)
    {
        dr = new DataResource(context);
        c = context;

    }


    public ArrayList<String> getNames() {
        return IdNames;
    }

    public String getUniName() {
        return UniName;
    }

    public String getUniLink() {
        return UniLink;
    }

    private void setUniName(String uniName) {
        UniName = uniName;
    }

    private void setUniLink(String uniLink) {
        UniLink = uniLink;
    }


    private void setHmIdValues(HashMap<String, String> hmIdValues) {
        this.hmIdValues = hmIdValues;
    }

    private void setNames(ArrayList<String> names) {
        this.IdNames = names;
    }



    public void open()
    {
        database = dr.getWritableDatabase();
        databaseRead = dr.getReadableDatabase();
    }

    public void close()
    {
        dr.close();

    }

    public boolean insertUniLink()
    {
        String insertQuery = "insert into UniLinks values('"+UniName.toUpperCase()+"','"+UniLink+"');";
        database.execSQL(insertQuery);
        System.out.println(insertQuery);
        return true;

    }
    public boolean insertUniDetails()
    {
        Encryption encryption = Encryption.getDefault("Key", "Salt", new byte[16]);
        String encrypted = "";

        for(String id : IdNames) {
            ContentValues cv = new ContentValues();

            cv.put("UniName", UniName.toUpperCase());
            //cv.put("FieldName", name);
            cv.put("FieldId", id);
            encrypted = encryption.encryptOrNull(hmIdValues.get(id));
            cv.put("FieldValue",encrypted );

            database.insert("UniD",null,cv);
        }
        System.out.println("inserted in UniD");
        return true;
    }

    public boolean deleteAll()
    {
        database.delete("UniLinks",null,null);
        database.delete("UniD",null,null);
        return true;

    }
    public static ArrayList<String> displayAll(Context c)
    {
        DataResource dr = new DataResource(c);

        SQLiteDatabase databaseRead = dr.getReadableDatabase();

        Cursor cursor = databaseRead.rawQuery("select*from UniLinks",null);//new String[]{"UniName"});
        ArrayList<String> UniNames = new ArrayList<>();
        while(cursor.moveToNext())
        {
            UniNames.add(cursor.getString(0));
        }

        dr.close();
        return UniNames;

    }
    public University displayDetail(String UniName)
    {
        University uni = new University(this.c);
        SQLiteDatabase databaseRead = dr.getReadableDatabase();
        Cursor cursor1 = databaseRead.rawQuery("select*from UniLinks where UniName=?",new String[]{UniName});

        cursor1.moveToNext();
        uni.setUniLink(cursor1.getString(1));
        Cursor cursor = databaseRead.rawQuery("select*from UniD where UniName='"+UniName+"'",null);
        HashMap<String,String> hmIdValues = new HashMap<>();
        ArrayList<String> names = new ArrayList<>();
        String FieldName,FieldValue,FieldId;
        String decrypted = "";
        Encryption encryption = Encryption.getDefault("Key", "Salt", new byte[16]);
        while(cursor.moveToNext())
        {
            System.out.println("no of rows="+cursor.getString(0));

            FieldId = cursor.getString(1);
            FieldValue = cursor.getString(2);
            decrypted = encryption.decryptOrNull(FieldValue);
            hmIdValues.put(FieldId,decrypted);
            names.add(FieldId);

        }
        System.out.println("inside datares "+hmIdValues);
        //uni.setHmfieldNamesValues(hmNameValues);
        //uni.setHmfieldNamesIds(hmNameIds);
        uni.setHmIdValues(hmIdValues);
        uni.setNames(names);
        uni.setUniName(UniName);
        dr.close();
        return uni;



    }
    public void deleteUni(String uniName)
    {
        database.delete("UniLinks","UniName=?",new String[]{uniName});
        database.delete("UniD","UniName=?",new String[]{uniName});
    }


}
