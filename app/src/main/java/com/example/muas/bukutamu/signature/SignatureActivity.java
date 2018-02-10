package com.example.muas.bukutamu.signature;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.muas.bukutamu.R;
import com.example.muas.bukutamu.ui.BukuTamuActivity;
import com.github.gcacace.signaturepad.views.SignaturePad;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignatureActivity extends AppCompatActivity {

    @BindView(R.id.signaturePad)
    SignaturePad signaturePad;
    @BindView(R.id.saveButton)
    Button saveButton;
    @BindView(R.id.clearButton)
    Button clearButton;

    public static final int STORAGE_PERMISSION_CODE = 1001;
    @BindView(R.id.viewButton)
    Button viewButton;
    //Declare private variables
    public String signaturePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature);
        ButterKnife.bind(this);

        viewButton = (Button) findViewById(R.id.viewButton);
        saveButton = (Button)findViewById(R.id.saveButton);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            requestStoragePermission();
        }

        //disable both buttons at start
        saveButton.setEnabled(false);
        clearButton.setEnabled(false);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //write code for saving the signature as image
                Bitmap bitmapSignature = signaturePad.getSignatureBitmap();
                // Create image from bitmap and store it in memory
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmapSignature.compress(Bitmap.CompressFormat.JPEG, 40, byteArrayOutputStream);
                Random rand = new Random();
                int randomValue = rand.nextInt(9999);
                File file = new File(Environment.getExternalStorageDirectory().getAbsoluteFile() +
                        "/" + String.valueOf(randomValue) + "capturedsignature.jpg");
                try {
                    if (file.createNewFile()) {
                        file.createNewFile();
                    }
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    fileOutputStream.write(byteArrayOutputStream.toByteArray());
                    fileOutputStream.close();

                    signaturePath = file.getAbsolutePath();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                Toast.makeText(SignatureActivity.this, "Signature Saved", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(SignatureActivity.this, BukuTamuActivity.class);
                i.putExtra("SignaturePath", signaturePath);
                startActivity(i);
            }

        });

        /*viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SignatureActivity.this, BukuTamuActivity.class);
                i.putExtra("SignaturePath", signaturePath);
                startActivity(i);


            }
        });*/

        //change screen orientation to landscape mode
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        signaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {

            }

            @Override
            public void onSigned() {
                saveButton.setEnabled(true);
                clearButton.setEnabled(true);
            }

            @Override
            public void onClear() {
                saveButton.setEnabled(false);
                clearButton.setEnabled(false);
            }
        });

    }


    public void OnClearSignatureClick(View v) {
        signaturePad.clear();
    }



    private void enableDisableButtons(boolean enableButton) {
        saveButton.setEnabled(enableButton);
        clearButton.setEnabled(enableButton);
    }

    //Requesting permission
    private void requestStoragePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }

        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }


    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                //Displaying a toast
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }

    }


    /*@OnClick(R.id.viewButton)
    public void onViewClicked() {
        Intent i = new Intent(this, CobaTtdActivity.class);
        i.putExtra("SignaturePath", signaturePath);
        startActivity(i);
    }*/


    /*@OnClick({R.id.saveButton, R.id.clearButton})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.saveButton:
                Toast.makeText(SignatureActivity.this, "Signature Saved", Toast.LENGTH_SHORT).show();
                break;
            case R.id.clearButton:
                signaturePad.clear();
                break;
        }
    }*/
}
