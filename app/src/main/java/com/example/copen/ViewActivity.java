package com.example.copen;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.opencv.android.Utils;
import org.opencv.core.Mat;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import static com.example.copen.Extensions.AnswerRecognition.recognize;
import static com.example.copen.Extensions.PerspectiveCorrection.correctPerspective;

public class ViewActivity extends AppCompatActivity {

    private ImageView imageView2;
    private TextView textView;
    private String recognitionResult;
    private Mat result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        imageView2 = findViewById(R.id.imageView2);
        textView = findViewById(R.id.textView);
        Log.d("successfull", "SUCCESS");

        Bundle extras = getIntent().getExtras();

        result = new Mat();
        if (extras != null) {
            String path = extras.getString("imgPath");
//            Mat result = new Mat();
            File directory = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
            if (directory.exists()) {
                Log.d("image source:", directory.toString());
                result = correctPerspective(path);
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                Bitmap resultBitmap = Bitmap.createBitmap(result.cols(), result.rows(), Bitmap.Config.ARGB_8888);
                Utils.matToBitmap(result, resultBitmap);
                imageView2.setImageBitmap(resultBitmap);
            } else {
                Log.d("path wrong", directory.toString());
            }


            InputStream stream = null;
            try {
                stream = getAssets().open("templates/questions.jpg");
            } catch (IOException e) {
                e.printStackTrace();
            }
            Bitmap bitmap = BitmapFactory.decodeStream(stream);
            Mat questions = new Mat();
            Utils.bitmapToMat(bitmap, questions);

            ArrayList<Bitmap> answers = new ArrayList<>();
            for (int i = 1; i < 5; i++) {
                InputStream stream2 = null;
                try {
                    stream = getAssets().open("templates/templates_bigger/"+i+".jpg");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Bitmap bitmap2 = BitmapFactory.decodeStream(stream);
                answers.add(bitmap2);
            }

            recognitionResult = recognize(result, questions, answers);
            textView.setText(recognitionResult);
        } else {
            Log.d("Extras empty", extras.toString());
        }
    }
}