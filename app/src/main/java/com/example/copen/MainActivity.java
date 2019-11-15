package com.example.copen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.opencv.android.OpenCVLoader;

public class MainActivity extends AppCompatActivity {

    private Button cameraActivitybtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e(this.getClass().getSimpleName(),"DZIALA CZY NIE ? XD "+ OpenCVLoader.initDebug());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cameraActivitybtn = findViewById(R.id.button);

        cameraActivitybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),CameraActivity.class);
                startActivity(intent);
            }
        });
    }


}
