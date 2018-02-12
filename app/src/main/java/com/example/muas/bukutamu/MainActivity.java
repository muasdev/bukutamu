package com.example.muas.bukutamu;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.muas.bukutamu.ui.BukuTamuActivity;
import com.example.muas.bukutamu.ui.DaftarTamuActivity;
import com.spark.submitbutton.SubmitButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.button_bukutamu)
    Button buttonBukutamu;
    @BindView(R.id.button_listtamu)
    Button buttonListtamu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*butterknife*/
        ButterKnife.bind(this);

        //change screen orientation to landscape mode
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    /*button di layout*/
    @OnClick({R.id.button_bukutamu, R.id.button_listtamu})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.button_bukutamu:
                Intent intent = new Intent(this, BukuTamuActivity.class);
                startActivity(intent);
                break;
            case R.id.button_listtamu:
                Intent intenttolist = new Intent(this, DaftarTamuActivity.class);
                startActivity(intenttolist);
                break;
        }
    }

}
