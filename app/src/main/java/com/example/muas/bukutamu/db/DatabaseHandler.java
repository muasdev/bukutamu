package com.example.muas.bukutamu.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.muas.bukutamu.db.model.Contact;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class DatabaseHandler extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 4;

    // Database Name
    public static final String DATABASE_NAME = "bukuTamu";

    // Contacts table name
    private static final String TABLE_TAMU = "tamu";

    // Contacts Table Columns names
    private static final String KEY_ID = "no";
    private static final String KEY_NAMA = "Nama";
    private static final String KEY_ALAMAT = "Alamat";
    private static final String KEY_INSTANSI = "Instansi";
    private static final String KEY_NOHP = "nomorhp";
    private static final String KEY_TUJUAN = "tujuan";
    private static final String KEY_TIMEIN = "timein";
    private static final String KEY_SIGNATURE = "signature";
    private static final String KEY_POTO = "photo";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //Create tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_TAMU = "CREATE TABLE " + TABLE_TAMU + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAMA + " TEXT,"
                + KEY_ALAMAT + " TEXT,"
                + KEY_INSTANSI + " TEXT,"
                + KEY_NOHP + " TEXT,"
                + KEY_TUJUAN + " TEXT,"
                + KEY_TIMEIN + " TEXT,"
                + KEY_SIGNATURE + " BLOB,"
                + KEY_POTO + " BLOB" + ")";
        db.execSQL(CREATE_TABLE_TAMU);
    }


    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TAMU);

        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */


    //Insert values to the table contacts
    public void addContacts(Contact contact) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_NAMA, contact.getnama_model());
        values.put(KEY_ALAMAT, contact.getalamat_model());
        values.put(KEY_INSTANSI, contact.getinstansi_model());
        values.put(KEY_NOHP, contact.getnohp_model());
        values.put(KEY_TUJUAN, contact.gettujuan_model());
        values.put(KEY_TIMEIN, contact.getTimein_model());
        values.put(KEY_SIGNATURE, contact.getSignature_model());
        values.put(KEY_POTO, contact.get_img());


        db.insert(TABLE_TAMU, null, values);
        db.close();
    }

    /**
     * Getting All Contacts
     **/

    public List<Contact> getAllContacts() {
        List<Contact> contactList = new ArrayList<Contact>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_TAMU;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {

            do {
                Contact contact = new Contact();
                contact.set_id(Integer.parseInt(cursor.getString(0)));
                contact.setnama_model(cursor.getString(1));
                contact.setalamat_model(cursor.getString(2));
                contact.setinstansi_model(cursor.getString(3));
                contact.setnohp_model(cursor.getString(4));
                contact.settujuan_model(cursor.getString(5));
                contact.setTimein_model(cursor.getString(6));
                contact.setSignature_model(cursor.getBlob(7));
                contact.set_img(cursor.getBlob(8));

                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }


        // return contact list
        return contactList;
    }


    /**
     * Updating single contact
     **/

    public int updateContact(Contact contact, int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAMA, contact.getnama_model());
        values.put(KEY_ALAMAT, contact.getalamat_model());
        values.put(KEY_INSTANSI, contact.getinstansi_model());
        values.put(KEY_NOHP, contact.getnohp_model());
        values.put(KEY_TUJUAN, contact.gettujuan_model());
        values.put(KEY_TIMEIN, contact.getTimein_model());
        values.put(KEY_SIGNATURE, contact.getSignature_model());
        values.put(KEY_POTO, contact.get_img());


        // updating row
        return db.update(TABLE_TAMU, values, KEY_ID + " = ?",
                new String[]{String.valueOf(id)});
    }

    /**
     * Deleting single contact
     **/

    public void deleteContact(int Id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TAMU, KEY_ID + " = ?",
                new String[]{String.valueOf(Id)});
        db.close();
    }


    /*hitung jumlah baris*/
    public int getProfilesCount() {
        String countQuery = "SELECT  * FROM " + TABLE_TAMU;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        return count;
    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }


}
