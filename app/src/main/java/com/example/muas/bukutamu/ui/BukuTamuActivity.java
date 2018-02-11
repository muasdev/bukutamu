package com.example.muas.bukutamu.ui;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.CircularProgressButton;
import com.example.muas.bukutamu.MainActivity;
import com.example.muas.bukutamu.R;
import com.example.muas.bukutamu.db.DatabaseHandler;
import com.example.muas.bukutamu.db.model.Contact;
import com.example.muas.bukutamu.signature.SignatureActivity;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BukuTamuActivity extends AppCompatActivity {


    @BindView(R.id.edt_Name)
    EditText edtName;
    @BindView(R.id.imgView_Foto_tamu)
    ImageView imgViewFotoTamu;
    @BindView(R.id.btn_take_photo)
    Button btnTakePhoto;

    @BindView(R.id.btn_save)
    Button btnSave;
    /*@BindView(R.id.edt_Gender)
    EditText edtGender;*/
    @BindView(R.id.edt_Alamat)
    EditText edtAlamat;
    @BindView(R.id.edt_Instansi)
    EditText edtInstansi;
    @BindView(R.id.edt_Nohp)
    EditText edtNohp;
    @BindView(R.id.edt_Tujuan)
    EditText edtTujuan;
    @BindView(R.id.btn_take_ttd)
    Button btnTakeTtd;
    @BindView(R.id.imgView_Foto_ttd)
    ImageView imgViewFotoTtd;
    @BindView(R.id.btn_reset)
    Button btnReset;
    @BindView(R.id.txt_waktu)
    TextView txtWaktu;
    @BindView(R.id.txt_nomorurut)
    TextView txtNomorurut;

    private Bitmap bp;
    private byte[] img;
    private DatabaseHandler db;
    private String nama, alamat, instansi, nohp, tujuan, timein;

    /*ambil ttd*/

    private String signaturePath;

    final int REQUEST_IMAGE_CAPTURE = 1;

    final int REQUEST_IMAGE_TTD = 2;

    /*waktu*/
    Calendar calendar;
    SimpleDateFormat simpleDateFormat;
    String Date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buku_tamu);

        ButterKnife.bind(this);

        db = new DatabaseHandler(this);
        /*btnTakePhoto.setEnabled(false);*/

        calendar = Calendar.getInstance();
        /*simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");*/
        simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date = simpleDateFormat.format(calendar.getTime());
        txtWaktu.setText(Date);

        db.getProfilesCount();
        int profile_counts = db.getProfilesCount();
        txtNomorurut.setText(String.valueOf("nomor urut : " + (profile_counts + 1)));

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            signaturePath = extras.getString("SignaturePath");
            if (signaturePath != null)
                imgViewFotoTtd.setImageURI(Uri.parse("file://" + signaturePath));
            btnTakePhoto.setEnabled(true);
        }

    }

    /*@Override
    protected void onSaveInstanceState(Bundle outState) {

        nama = edtName.getText().toString();
        alamat = edtAlamat.getText().toString();
        instansi = edtInstansi.getText().toString();
        nohp = edtNohp.getText().toString();
        tujuan = edtTujuan.getText().toString();
        timein = txtWaktu.getText().toString();
        img = profileImage(bp);

        outState.putString("editext1", nama);
        outState.putString("editext2", alamat);
        outState.putString("editext3", instansi);
        outState.putString("editext4", nohp);
        outState.putString("editext5", tujuan);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {

        super.onRestoreInstanceState(savedInstanceState);

        edtName.setText(savedInstanceState.getString("edittext1"));
    }*/

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            /*case REQUEST_IMAGE_TTD:

                if (resultCode == RESULT_OK) {

                    Bundle extras = data.getExtras();

                    if (extras != null) {
                        signaturePath = extras.getString("SignaturePath");
                        if (signaturePath != null) {
                            imgViewFotoTtd.setImageURI(Uri.parse("file://" + signaturePath));
                            btnTakePhoto.setEnabled(true);
                        } else {
                            imgViewFotoTtd.setImageResource(R.drawable.ic_apps_black_24dp);
                        }

                    }*/

                    /*Intent intent = getIntent();
                    Bundle extras = intent.getExtras();

                    if (extras != null) {
                        signaturePath = data.getStringExtra("SignaturePath");
                        if (signaturePath != null)
                            imgViewFotoTtd.setImageURI(Uri.parse("file://" + signaturePath));
                        btnTakePhoto.setEnabled(true);
                    }*/

            case REQUEST_IMAGE_CAPTURE:
                if (resultCode == RESULT_OK) {
                    Uri choosenImage = data.getData();

                    if (choosenImage != null) {

                        bp = decodeUri(choosenImage, 400);
                        imgViewFotoTamu.setImageBitmap(bp);
                    }
                }
                break;

        }
    }


    //COnvert and resize our image to 400dp for faster uploading our images to DB
    protected Bitmap decodeUri(Uri selectedImage, int REQUIRED_SIZE) {

        try {

            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(this.getContentResolver().openInputStream(selectedImage), null, o);

            // The new size we want to scale to
            // final int REQUIRED_SIZE =  size;

            // Find the correct scale value. It should be the power of 2.
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
                if (width_tmp / 2 < REQUIRED_SIZE
                        || height_tmp / 2 < REQUIRED_SIZE) {
                    break;
                }
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(this.getContentResolver().openInputStream(selectedImage), null, o2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //Convert bitmap to bytes
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    private byte[] profileImage(Bitmap b) {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.PNG, 0, bos);
        return bos.toByteArray();

    }


    // function to get values from the Edittext and image
    private void getValues() {
        nama = edtName.getText().toString();
        alamat = edtAlamat.getText().toString();
        instansi = edtInstansi.getText().toString();
        nohp = edtNohp.getText().toString();
        tujuan = edtTujuan.getText().toString();
        timein = txtWaktu.getText().toString();
        img = profileImage(bp);

    }

    //Insert data to the database
    private void addContact() {
        getValues();

        db.addContacts(new Contact(nama, alamat, instansi, nohp, tujuan, timein, img));
        Toast.makeText(this, "Saved successfully", Toast.LENGTH_LONG).show();
    }


    @OnClick({R.id.btn_take_photo, R.id.btn_take_ttd, R.id.btn_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_take_photo:
                cameraIntent();
                break;
            case R.id.btn_take_ttd:
                Intent intent = new Intent(this, SignatureActivity.class);
                startActivityForResult(intent, REQUEST_IMAGE_TTD);
                break;
            case R.id.btn_save:
                /*if (edtName.getText().toString().trim().equals("")) {
                    Toast.makeText(this, "Name edit text is empty, Enter name", Toast.LENGTH_LONG).show();
                } else if (edtAlamat.getText().toString().trim().equals("")) {
                    Toast.makeText(this, "Name edit text is empty, masukkan alamat", Toast.LENGTH_LONG).show();
                } else if (edtInstansi.getText().toString().trim().equals("")) {
                    Toast.makeText(this, "Name edit text is empty, masukkan instansi", Toast.LENGTH_LONG).show();
                } else if (edtNohp.getText().toString().trim().equals("")) {
                    Toast.makeText(this, "Name edit text is empty, masukkan no hp", Toast.LENGTH_LONG).show();
                } else if (edtTujuan.getText().toString().trim().equals("")) {
                    Toast.makeText(this, "Name edit text is empty, masukkan tujuan", Toast.LENGTH_LONG).show();
                } else
                */
                    addContact();
                    Intent intent1 = new Intent(this, MainActivity.class);
                    startActivity(intent1);

                break;
        }
    }

    @OnClick(R.id.btn_reset)
    public void onViewClicked() {

        imgViewFotoTtd.setImageResource(R.mipmap.ic_launcher);
        imgViewFotoTamu.setImageResource(R.mipmap.ic_launcher);
        btnTakePhoto.setEnabled(false);
    }
    /*@OnClick(R.id.btnWithText)
    public void onViewClicked() {
        btnWithText.setIndeterminateProgressMode(true); // turn on indeterminate progress
        btnWithText.setProgress(50); // set progress > 0 & < 100 to display indeterminate progress
        btnWithText.setProgress(100); // set progress to 100 or -1 to indicate complete or error state
        btnWithText.setProgress(0); // set progress to 0 to switch back to normal state
        addContact();
    }*/
}
