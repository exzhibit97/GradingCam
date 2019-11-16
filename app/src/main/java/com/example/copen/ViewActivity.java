package com.example.copen;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

import org.opencv.android.Utils;
import org.opencv.core.Mat;

import java.io.File;

import static com.example.copen.Extensions.PerspectiveCorrection.correctPerspective;

public class ViewActivity extends AppCompatActivity {

    private ImageView imageView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        imageView2 = findViewById(R.id.imageView2);
        Log.d("successfull", "SUCCESS");

        Bundle extras = getIntent().getExtras();


        if (extras != null) {
            String path = extras.getString("imgPath");
            File directory = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
            if (directory.exists()) {
                Log.d("image source:", directory.toString());
                Mat result = new Mat();
                result = correctPerspective(path);
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                Bitmap resultBitmap = Bitmap.createBitmap(result.cols(), result.rows(), Bitmap.Config.ARGB_8888);
                Utils.matToBitmap(result, resultBitmap);
                imageView2.setImageBitmap(resultBitmap);
            } else {
                Log.d("path wrong", directory.toString());
            }
        } else {
            Log.d("Extras empty", extras.toString());
        }


    }
}
