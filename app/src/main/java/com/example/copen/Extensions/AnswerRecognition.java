package com.example.copen.Extensions;


import android.graphics.Bitmap;
import android.util.Log;

import com.example.copen.Classes.Blueprint;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;

import static org.opencv.imgproc.Imgproc.COLOR_BGR2GRAY;
import static org.opencv.imgproc.Imgproc.TM_CCOEFF_NORMED;

public class AnswerRecognition {

    public static void recognize(Mat sourceMat, Mat templateMat, ArrayList answersArray) {
        //System.loadLibrary(Core.NATIVE_LIBRARY_NAME);


        Mat source = sourceMat;
        Imgproc.cvtColor(source, source, COLOR_BGR2GRAY);
        Mat template = templateMat;
        Imgproc.cvtColor(template, template, COLOR_BGR2GRAY);
        String filePath = "C:\\Users\\Patryk\\Desktop\\template\\";
        char[] maxValMatches = new char[]{'A', 'B', 'C', 'D'};
        char[] finalAnswers = new char[20];
        char[] tempKey = new char[]{'A','B','A','A','B','A','A','B','C','D','C','C','B','D','A','B','C','B','B','C'};
        double[] eachQuestionMaxVals = new double[4];
        //Load image file
//    source= Imgcodecs.imread(filePath+"corrected.jpg", IMREAD_GRAYSCALE);

//    template=Imgcodecs.imread(filePath+"questions.jpg", IMREAD_GRAYSCALE);
        //(template.height()/20);
        int interval = template.height() / 20;
        Log.d("INTERVAL", String.valueOf(interval) + "source width" + source.width() + "source height" + source.height() + " template width" + template.width() + "template height" + template.height());
        Mat outputImage = new Mat();
        //Matching question header to whole image
        Imgproc.matchTemplate(source, template, outputImage, TM_CCOEFF_NORMED);
        Core.MinMaxLocResult mmr = Core.minMaxLoc(outputImage);
        //create point with coordinates from where to draw rectangle
        Point matchLoc = mmr.maxLoc;
        //System.out.println(matchLoc);

        //drawing rectangle around question header matched template
        Imgproc.rectangle(source, matchLoc, new Point(matchLoc.x + interval,
                matchLoc.y + template.height()), new Scalar(0, 0, 255));
        //rectangle with first question answers area inside
        for (int i = 0; i < 20; i++) {
            Rect question = new Rect(0, (int)matchLoc.y, source.width() - template.width(), interval);
            Imgproc.rectangle(source, matchLoc, new Point(matchLoc.x + (template.cols()) * 5,
                    matchLoc.y + interval), new Scalar(0, 0, 255));
            //imwrite("C:\\Users\\Patryk\\Desktop\\template\\asdasd.jpg", source);
            //create new mat from rectangle area
            Log.d("question rectangle :", question.height  + " by " + question.width + ". Starting from:" + matchLoc.x + " " + matchLoc.y);
            Mat questionMat = new Mat(source, question);
            //save that mat as an image
            //imwrite("C:\\Users\\Patryk\\Desktop\\template\\q_sep\\question" + (i+1) + ".jpg", questionMat);
            for (int j = 0; j < 4; j++) {
                //Mat questionTemplate = Imgcodecs.imread(filePath + "\\templates_bigger\\" + (j + 1) + ".jpg", IMREAD_GRAYSCALE);
                Mat questionTemplate = new Mat();
                Utils.bitmapToMat((Bitmap) answersArray.get(j), questionTemplate);
                Imgproc.cvtColor(questionTemplate, questionTemplate, COLOR_BGR2GRAY);
                //match first question area with possible answer jpegs (empty A, B, C and D images)
                Mat outputFirstQ = new Mat();
                Imgproc.matchTemplate(questionMat, questionTemplate, outputFirstQ, TM_CCOEFF_NORMED);
                Core.MinMaxLocResult minMaxQuestion = Core.minMaxLoc(outputFirstQ);
                //Log.d("Question: ",(i + 1) + " checked answer: [" + maxValMatches[j] + "] MAXVAL: " + minMaxQuestion.maxVal);
                eachQuestionMaxVals[j] = minMaxQuestion.maxVal;
                //System.out.println("Matchlock.x " + matchLoc.x + " Matchlock.y " + matchLoc.y);
                Point matchQ = minMaxQuestion.maxLoc;
                //draw and rectangle in place where template matches with source image
                Imgproc.rectangle(questionMat, matchQ, new Point(matchQ.x + (questionTemplate.cols()),
                        matchQ.y + questionTemplate.rows()), new Scalar(255, 0, 0));
            }
            int index = 0;
            double min = eachQuestionMaxVals[index];
            for (int j = 0; j < 4; j++) {
                if (eachQuestionMaxVals[j] < min) {
                    min = eachQuestionMaxVals[j];
                    index = j;
                }
            }
            //System.out.println("Smallest: " + index);
            finalAnswers[i] = maxValMatches[index];

            System.out.println("--------------------------------------------------------------------------------");
            matchLoc.y += interval;
            //Imgcodecs.imwrite(filePath + "\\graded\\question" + i + ".jpg", questionMat);
        }

        StringBuilder stringBuilder = new StringBuilder();
        String answers;
        for (int i = 0; i < 20; i++) {
//            System.out.print(finalAnswers[i] + " ");
            stringBuilder.append(finalAnswers[i]);
        }

        //count points
        int pointsCounter = 0;
        for (int i = 0; i < finalAnswers.length; i++) {
            if(finalAnswers[i] == tempKey[i]) {
                pointsCounter+=1;
            } else {
                Log.d("MISTAKES", "You got mistaken in question: " + (i+1) + ". You answered " + finalAnswers[i] + " where it should be " + tempKey[i]);
            }
        }

        Log.d("ANSWERS:", String.valueOf(stringBuilder) + ". You scored :" + pointsCounter + " points");
        Blueprint bp = new Blueprint(finalAnswers);
        Blueprint bp2 = new Blueprint(finalAnswers);
        //System.out.println(bp.getId());
        char[] asd = bp.getAnswers();
//        for (int i = 0; i < asd.length; i++) {
//            System.out.println(asd[i]);
//        }
        try (Writer writer = new FileWriter("Output.json")) {
            Gson gson = new GsonBuilder().create();
            gson.toJson(bp, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
