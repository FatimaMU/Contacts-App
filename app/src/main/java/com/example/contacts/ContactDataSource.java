package com.example.contacts;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

public class ContactDataSource {
    SQLiteDatabase database;
    ContactDBHelper dbHelper;

    public ContactDataSource(Context context) {
        dbHelper = new ContactDBHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public boolean insertContact(Contact c) {
        boolean didSucceed = false;
        try {
            ContentValues initialValues = new ContentValues();
            initialValues.put("contactname", c.getContactName());
            initialValues.put("streetaddress", c.getStreetAddress());
            initialValues.put("city", c.getCity());
            initialValues.put("state", c.getState());
            initialValues.put("zipcode", c.getZipcode());
            initialValues.put("phonenumber", c.getPhoneNumber());
            initialValues.put("cellnumber", c.getCellNumber());
            initialValues.put("email", c.getEMail());
            initialValues.put("birthday", String.valueOf(c.getBirthday().getTimeInMillis()));
            didSucceed = database.insert("contact", null, initialValues) > 0;
        } catch (Exception e) {
            Log.d("My Database", "Something went wrong!");
        }
        return didSucceed;
    }

    public boolean updateContact(Contact c) {
        boolean didSucceed = false;
        try {
            long rowID = c.getContactID();
            ContentValues updatedValues = new ContentValues();
            updatedValues.put("contactname", c.getContactName());
            updatedValues.put("streetaddress", c.getStreetAddress());
            updatedValues.put("city", c.getCity());
            updatedValues.put("state", c.getState());
            updatedValues.put("zipcode", c.getZipcode());
            updatedValues.put("phonenumber", c.getPhoneNumber());
            updatedValues.put("cellnumber", c.getCellNumber());
            updatedValues.put("email", c.getEMail());
            updatedValues.put("birthday", String.valueOf(c.getBirthday().getTimeInMillis()));
            didSucceed = database.update("contact", updatedValues, "_id = " + rowID, null) > 0;
        } catch (Exception e) {
        }
        return didSucceed;
    }

    public int getLastContact(){
        int lastId;
        try{
            String query = "Select MAX(_id) from contact";
            Cursor cursor = database.rawQuery(query,null);
            cursor.moveToFirst();
            lastId = cursor.getInt(0);
            cursor.close();
        }
        catch (Exception e){
            lastId = -1;
        }
        return lastId;
    }
}
