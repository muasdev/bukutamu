package com.example.muas.bukutamu.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.muas.bukutamu.MainActivity;
import com.example.muas.bukutamu.R;
import com.example.muas.bukutamu.db.DatabaseHandler;
import com.example.muas.bukutamu.db.model.Contact;
import com.example.muas.bukutamu.signature.SignatureActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
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
    @BindView(R.id.daftar_nama_layout)
    TextInputLayout daftarNamaLayout;
    @BindView(R.id.daftar_alamat_layout)
    TextInputLayout daftarAlamatLayout;
    @BindView(R.id.daftar_instansi_layout)
    TextInputLayout daftarInstansiLayout;
    @BindView(R.id.daftar_nohp_layout)
    TextInputLayout daftarNohpLayout;
    @BindView(R.id.daftar_tujuan_layout)
    TextInputLayout daftarTujuanLayout;

    private Bitmap bp;
    private byte[] img;
    private byte[] signature;
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

    SharedPreferences sharedPreferences;

    static Uri capturedImageUri = null;

    Vibrator vib;

    Animation animShake;

    String encodedImage;


    @Override
    public void onDestroy() {
        super.onDestroy();

        sharedPreferences = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
        sharedPreferences.edit().remove("ED_NAME").apply();
        sharedPreferences.edit().remove("ED_ALAMAT").apply();
        sharedPreferences.edit().remove("ED_INSTANSI").apply();
        sharedPreferences.edit().remove("ED_NOHP").apply();
        sharedPreferences.edit().remove("ED_TUJUAN").apply();
        sharedPreferences.edit().remove("my_image").apply();
        finish();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buku_tamu);

        ButterKnife.bind(this);

        db = new DatabaseHandler(this);
        /*btnTakePhoto.setEnabled(false);*/

        calendar = Calendar.getInstance();
        /*simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");*/
        simpleDateFormat = new SimpleDateFormat("EEE dd-MM-yyyy HH:mm:ss");
        Date = simpleDateFormat.format(calendar.getTime());
        txtWaktu.setText(Date);

        db.getProfilesCount();
        int profile_counts = db.getProfilesCount();
        txtNomorurut.setText(String.valueOf("No. urut : " + (profile_counts + 1)));

        animShake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
        vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        if (Build.VERSION.SDK_INT >= 24) {
            try {
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        sharedPreferences = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
        if (sharedPreferences != null) {
            edtName.setText(sharedPreferences.getString("ED_NAME", ""));
        }
        if (sharedPreferences.contains("ED_NAME")) {
            edtName.setText(sharedPreferences.getString("ED_NAME", ""));
        }
        if (sharedPreferences.contains("ED_ALAMAT")) {
            edtAlamat.setText(sharedPreferences.getString("ED_ALAMAT", ""));
        }
        if (sharedPreferences.contains("ED_INSTANSI")) {
            edtInstansi.setText(sharedPreferences.getString("ED_INSTANSI", ""));
        }
        if (sharedPreferences.contains("ED_NOHP")) {
            edtNohp.setText(sharedPreferences.getString("ED_NOHP", ""));
        }
        if (sharedPreferences.contains("ED_TUJUAN")) {
            edtTujuan.setText(sharedPreferences.getString("ED_TUJUAN", ""));
        }
        encodedImage = sharedPreferences.getString("my_image", "");
        if (!encodedImage.equalsIgnoreCase("")) {
            //Decoding the Image and display in ImageView
            byte[] b = Base64.decode(encodedImage, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
            imgViewFotoTamu.setImageBitmap(bitmap);
        }

        /*get data from signature activity*/
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            signaturePath = extras.getString("SignaturePath");
            if (signaturePath != null)
                imgViewFotoTtd.setImageURI(Uri.parse("file://" + signaturePath));
            btnTakePhoto.setEnabled(true);
        }

        //change screen orientation to landscape mode
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

    }


    @Override
    protected void onPause() {
        super.onPause();

        // Store values between instances here
        sharedPreferences = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Put the values from the UI
        nama = edtName.getText().toString();
        alamat = edtAlamat.getText().toString();
        instansi = edtInstansi.getText().toString();
        nohp = edtNohp.getText().toString();
        tujuan = edtTujuan.getText().toString();

        editor.putString("ED_NAME", nama);
        editor.putString("ED_ALAMAT", alamat);
        editor.putString("ED_INSTANSI", instansi);
        editor.putString("ED_NOHP", nohp);
        editor.putString("ED_TUJUAN", tujuan);
        editor.putString("my_image", encodedImage);

        editor.commit();

    }

    private void cameraIntent() {
        /*Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }*/
        Calendar cal = Calendar.getInstance();
        File file = new File(Environment.getExternalStorageDirectory(), "/Backup/" + (cal.getTimeInMillis() + ".jpg"));
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            file.delete();
            try {
                file.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        capturedImageUri = Uri.fromFile(file);
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        i.putExtra(MediaStore.EXTRA_OUTPUT, capturedImageUri);
        startActivityForResult(i, REQUEST_IMAGE_CAPTURE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /*if(requestCode==REQUEST_IMAGE_CAPTURE&&resultCode==RESULT_OK){
        Bundle extras=data.getExtras();
        Bitmap imageBitmap=(Bitmap)extras.get("data");
        imgViewFotoTamu.setImageBitmap(imageBitmap);

        }*/

        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            //Bitmap photo = (Bitmap) data.getExtras().get("data");
            //imageView.setImageBitmap(photo);

            try {

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), capturedImageUri);
                // Encoding Image into Base64
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] b = baos.toByteArray();
                //Converting Base64 into String to Store in SharedPreferences
                encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                sharedPreferences = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("my_image", encodedImage);
                editor.commit();
                imgViewFotoTamu.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    //Convert bitmap to bytes
    /*@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)*/
    private byte[] profileImage(Bitmap b) {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.PNG, 0, bos);
        return bos.toByteArray();

    }


    // function to get values from the Edittext and image
    private void getValues() {
        /*get bitmap from imageviewfotottd*/
        Bitmap bitmap = ((BitmapDrawable) imgViewFotoTtd.getDrawable()).getBitmap();
        Bitmap bitmap1 = ((BitmapDrawable) imgViewFotoTamu.getDrawable()).getBitmap();
        nama = edtName.getText().toString();
        alamat = edtAlamat.getText().toString();
        instansi = edtInstansi.getText().toString();
        nohp = edtNohp.getText().toString();
        tujuan = edtTujuan.getText().toString();
        timein = txtWaktu.getText().toString();
        signature = profileImage(bitmap);
        img = profileImage(bitmap1);

    }

    //Insert data to the database
    private void addContact() {
        getValues();

        db.addContacts(new Contact(nama, alamat, instansi, nohp, tujuan, timein, signature, img));
        Toast.makeText(this, "Data Tersimpan, Terimakasih telah berkunjung di kantor kami", Toast.LENGTH_LONG).show();
    }

    /*kode for validate edittext*/
    private boolean checkNama() {
        if (edtName.getText().toString().trim().isEmpty()) {
            daftarNamaLayout.setErrorEnabled(true);
            daftarNamaLayout.setError("Masukkan Nama");
            return false;
        }
        daftarNamaLayout.setErrorEnabled(false);
        return true;
    }

    private boolean checkAlamat() {
        if (edtAlamat.getText().toString().trim().isEmpty()) {
            daftarAlamatLayout.setErrorEnabled(true);
            daftarAlamatLayout.setError("Masukkan Alamat");
            return false;
        }
        daftarAlamatLayout.setErrorEnabled(false);
        return true;
    }

    private boolean checkInstansi() {
        if (edtInstansi.getText().toString().trim().isEmpty()) {
            daftarInstansiLayout.setErrorEnabled(true);
            daftarInstansiLayout.setError("Masukkan Instansi");
            return false;
        }
        daftarInstansiLayout.setErrorEnabled(false);
        return true;
    }

    private boolean checkNohp() {
        if (edtNohp.getText().toString().trim().isEmpty()) {
            daftarNohpLayout.setErrorEnabled(true);
            daftarNohpLayout.setError("Masukkan No. HP");
            return false;
        }
        daftarNohpLayout.setErrorEnabled(false);
        return true;
    }

    private boolean checkTujuan() {
        if (edtTujuan.getText().toString().trim().isEmpty()) {
            daftarTujuanLayout.setErrorEnabled(true);
            daftarTujuanLayout.setError("Masukkan Tujuan");
            return false;
        }
        daftarTujuanLayout.setErrorEnabled(false);
        return true;
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
                if (!checkNama()) {
                    /*daftarNamaLayout.setAnimation(animShake);
                    daftarNamaLayout.startAnimation(animShake);*/
                    vib.vibrate(120);
                    return;
                }
                if (!checkAlamat()) {
                    vib.vibrate(120);
                    return;
                }
                if (!checkInstansi()) {
                    vib.vibrate(120);
                    return;
                }
                if (!checkNohp()) {
                    vib.vibrate(120);
                    return;
                }
                if (!checkTujuan()) {
                    vib.vibrate(120);
                    return;
                }

                daftarNamaLayout.setErrorEnabled(false);
                daftarAlamatLayout.setErrorEnabled(false);
                daftarInstansiLayout.setErrorEnabled(false);
                daftarNohpLayout.setErrorEnabled(false);
                daftarTujuanLayout.setErrorEnabled(false);

                addContact();

                sharedPreferences = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
                sharedPreferences.edit().clear().commit();
                finish();

                Intent intent1 = new Intent(this, MainActivity.class);
                startActivity(intent1);

                break;
        }
    }

    @OnClick(R.id.btn_reset)
    public void onViewClicked() {
        edtName.setText("");
        edtAlamat.setText("");
        edtInstansi.setText("");
        edtNohp.setText("");
        edtTujuan.setText("");
        imgViewFotoTtd.setImageResource(R.drawable.signature_pic);
        imgViewFotoTamu.setImageResource(R.drawable.user_pic);
    }
    /*@OnClick(R.id.btnWithText)
    public void onViewClicked() {
        btnWithText.setIndeterminateProgressMode(true); // turn on indeterminate progress
        btnWithText.setProgress(50); // set progress > 0 & < 100 to display indeterminate progress
        btnWithText.setProgress(100); // set progress to 100 or -1 to indicate complete or error state
        btnWithText.setProgress(0); // set progress to 0 to switch back to normal state
        addContact();
    }*/

    /*@Override
    protected void onDestroy() {
        super.onDestroy();
    }*/
}
