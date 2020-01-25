package com.example.copen;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.opencv.android.Utils;
import org.opencv.core.Mat;

import java.io.File;

import static com.example.copen.Extensions.PerspectiveCorrection.correctPerspective;

public class ViewActivity extends AppCompatActivity {

    private Handler recognizeHandler = new Handler();

    private ImageView imageView2;
    private TextView textView;
    private TextView textView2;
    private String recognitionResult;
    private Mat result;


    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), CameraActivity.class));
        finish();

    }

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        imageView2 = findViewById(R.id.imageView2);
        textView = findViewById(R.id.textView);
        textView2 = findViewById(R.id.textView2);
        Log.d("successfull", "SUCCESS");

        Bundle extras = getIntent().getExtras();

//
//        result = new Mat();
        if (extras != null) {
            String path = extras.getString("imgPath");
            String resultRec = extras.getString("result");
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
                textView.setText(String.format("%s", resultRec));
            } else {
                Log.d("path wrong", directory.toString());
            }
//
//
//            InputStream stream = null;
//            try {
//                stream = getAssets().open("templates/questions2.jpg");
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            Bitmap bitmap = BitmapFactory.decodeStream(stream);
//            final Mat questions = new Mat();
//            Utils.bitmapToMat(bitmap, questions);
//
//            final ArrayList<Bitmap> answers = new ArrayList<>();
//            for (int i = 1; i < 5; i++) {
//                InputStream stream2 = null;
//                try {
//                    stream = getAssets().open("templates/kropa.jpg");
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                Bitmap bitmap2 = BitmapFactory.decodeStream(stream);
//                answers.add(bitmap2);
//            }
////            Thread thread = new Thread(new Runnable(){
////                @Override
////                public void run(){
////                    recognitionResult = recognize(result, questions, answers);
////                    textView.setText(String.format("%s height: %d width: %d", recognitionResult, result.height(), result.width()));
////                }
////            });
////            recognizeHandler.post(new Runnable() {
////                @Override
////                public void run() {
////                    recognitionResult = recognize(result, questions, answers);
////                    textView.setText(String.format("%s height: %d width: %d", recognitionResult, result.height(), result.width()));
////                }
////            });
//            AsyncTask.execute(new Runnable() {
//                @Override
//                public void run() {
//                    recognitionResult = recognize(result, questions, answers);
//                    textView.setText(String.format("%s height: %d width: %d", recognitionResult, result.height(), result.width()));
//                }
//            });
        } else {
            Log.d("Extras empty", extras.toString());
        }
    }


}
