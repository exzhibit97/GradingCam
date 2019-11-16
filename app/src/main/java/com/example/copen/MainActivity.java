package com.example.copen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static org.opencv.imgproc.Imgproc.Canny;

public class MainActivity extends AppCompatActivity {

    private Button cameraActivitybtn;
    private Button perspectiveCorrection;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e(this.getClass().getSimpleName(),"DZIALA CZY NIE ? XD "+ OpenCVLoader.initDebug());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cameraActivitybtn = findViewById(R.id.button);
        imageView = findViewById(R.id.imageView);
        perspectiveCorrection = findViewById(R.id.button2);

        AssetManager assetManager = getAssets();
        Mat mat = new Mat();
        InputStream is = null;
        try {
            is = assetManager.open("10.jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }
//
        Bitmap bitmap = BitmapFactory.decodeStream(is);
        Utils.bitmapToMat(bitmap, mat);
        Canny(mat.clone(), mat, 50, 50);
        Utils.matToBitmap(mat, bitmap);
        imageView.setImageBitmap(bitmap);

        Bundle extras = getIntent().getExtras();
        //File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
        if(extras!=null) {
            String s = extras.getString("imgPath");
            Log.d("passed path:", s);
            //File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
            File dir = new File(s);
            Log.d("abs path:", dir.getAbsolutePath());
            if (dir.exists()) {
                Log.d("path: ", dir.toString());
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                Bitmap bitmap2 = BitmapFactory.decodeFile(String.valueOf(dir), options);
                imageView.setImageBitmap(bitmap2);

            } else {
                Log.d("dupa:", dir.toString());
            }
        } else {
            Log.d("DUPA:", "DUPA BLADA");
        }


        cameraActivitybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),CameraActivity.class);
                startActivity(intent);
            }
        });
    }




}
