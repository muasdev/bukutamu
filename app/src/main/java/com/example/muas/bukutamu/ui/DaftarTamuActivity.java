package com.example.muas.bukutamu.ui;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.ajts.androidmads.library.SQLiteToExcel;
import com.example.muas.bukutamu.R;
import com.example.muas.bukutamu.db.DatabaseHandler;
import com.example.muas.bukutamu.db.dataAdapter;
import com.example.muas.bukutamu.db.model.Contact;
import com.example.muas.bukutamu.util.Utils;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DaftarTamuActivity extends AppCompatActivity {

    @BindView(R.id.btn_display)
    Button btnDisplay;
    @BindView(R.id.list1)
    ListView list1;
    @BindView(R.id.btn_export)
    Button btnExport;

    private dataAdapter data;
    private Contact dataModel;
    private DatabaseHandler db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_tamu);

        //Instantiate database handler
        db = new DatabaseHandler(this);


        ButterKnife.bind(this);


        /*export data to excel*/
        /*btnExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                *//*Toast.makeText(DaftarTamuActivity.this, "ok fungsi ji je", Toast.LENGTH_SHORT).show();*//*
                String directory_path = Environment.getExternalStorageDirectory().getPath() + "/Backup/";
                File file = new File(directory_path);
                if (!file.exists()) {
                    file.mkdirs();
                }
                // Export SQLite DB as EXCEL FILE
                SQLiteToExcel sqliteToExcel = new SQLiteToExcel(DaftarTamuActivity.this, db.DATABASE_NAME, directory_path);
                sqliteToExcel.exportAllTables("bukutamu.xls", new SQLiteToExcel.ExportListener() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onCompleted(String filePath) {
                        Utils.showSnackBar(view, "Successfully Exported");
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
            }
        });*/
        //change screen orientation to landscape mode
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

    }

    /*convert to pdf*/
    private void ExportData(){
        String directory_path = Environment.getExternalStorageDirectory().getPath() + "/Backup/";
        File file = new File(directory_path);
        if (!file.exists()) {
            file.mkdirs();
        }
        // Export SQLite DB as EXCEL FILE
        SQLiteToExcel sqliteToExcel = new SQLiteToExcel(DaftarTamuActivity.this, db.DATABASE_NAME, directory_path);
        sqliteToExcel.exportAllTables("bukutamu.xls", new SQLiteToExcel.ExportListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onCompleted(String filePath) {
                Toast.makeText(DaftarTamuActivity.this, "ok fungsi ji je", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }


    //Retrieve data from the database and set to the list view
    private void ShowRecords() {
        final ArrayList<Contact> contacts = new ArrayList<>(db.getAllContacts());
        data = new dataAdapter(this, contacts);

        list1.setAdapter(data);

        list1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                dataModel = contacts.get(position);

                Toast.makeText(DaftarTamuActivity.this, String.valueOf(dataModel.get_id()), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @OnClick({R.id.btn_display, R.id.btn_export})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_display:
                ShowRecords();
                break;
            case R.id.btn_export:
                ExportData();
                break;
        }
    }
}

