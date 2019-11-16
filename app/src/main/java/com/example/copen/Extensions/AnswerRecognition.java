package com.example.copen.Extensions;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import static org.opencv.imgcodecs.Imgcodecs.IMREAD_GRAYSCALE;
import static org.opencv.imgcodecs.Imgcodecs.imwrite;
import static org.opencv.imgproc.Imgproc.TM_CCOEFF_NORMED;

public class AnswerRecognition {
    //System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    Mat source=null;
    Mat template=null;
    String filePath="C:\\Users\\Patryk\\Desktop\\template\\";
    char[] maxValMatches = new char[]{'A', 'B', 'C', 'D'};
    char[] finalAnswers = new char[20];
    double[] eachQuestionMaxVals = new double[4];
    //Load image file
    source= Imgcodecs.imread(filePath+"corrected.jpg", IMREAD_GRAYSCALE);
    template=Imgcodecs.imread(filePath+"questions.jpg", IMREAD_GRAYSCALE);
        System.out.println(template.height()/20);
    int interval = template.height()/20;
    Mat outputImage=new Mat();
    //Matching question header to whole image
        Imgproc.matchTemplate(source, template, outputImage, TM_CCOEFF_NORMED);
    Core.MinMaxLocResult mmr = Core.minMaxLoc(outputImage);
    //create point with coordinates from where to draw rectangle
    Point matchLoc=mmr.maxLoc;
        System.out.println(matchLoc);

    //drawing rectangle around question header matched template
        Imgproc.rectangle(source, matchLoc, new Point(matchLoc.x + interval,
                                                      matchLoc.y + template.height()), new Scalar(0,0,255));
    //rectangle with first question answers area inside
        for (int i = 0; i < 20; i++) {
        Rect question = new Rect((int)matchLoc.x, (int)matchLoc.y, (int)(source.width()-template.width()), interval);
        Imgproc.rectangle(source, matchLoc, new Point(matchLoc.x + (template.cols())*5,
                matchLoc.y + interval), new Scalar(0,0,255));
        imwrite("C:\\Users\\Patryk\\Desktop\\template\\asdasd.jpg", source);
        //create new mat from rectangle area
        Mat questionMat = new Mat(source, question);
        //save that mat as an image
        imwrite("C:\\Users\\Patryk\\Desktop\\template\\q_sep\\question" + (i+1) + ".jpg", questionMat);
        for (int j = 0; j < 4; j++) {
            Mat questionTemplate = Imgcodecs.imread(filePath+"\\templates_bigger\\" + (j+1) + ".jpg", IMREAD_GRAYSCALE);
            //match first question area with possible answer jpegs (empty A, B, C and D images)
            Mat outputFirstQ = new Mat();
            Imgproc.matchTemplate(questionMat, questionTemplate, outputFirstQ, TM_CCOEFF_NORMED);
            Core.MinMaxLocResult minMaxQuestion = Core.minMaxLoc(outputFirstQ);
            System.out.println("Question: " + (i+1) + " checked answer: [" + maxValMatches[j] + "] MAXVAL: " + minMaxQuestion.maxVal);
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
            if(eachQuestionMaxVals[j] < min) {
                min = eachQuestionMaxVals[j];
                index = j;
            }
        }
        //System.out.println("Smallest: " + index);
        finalAnswers[i] = maxValMatches[index];

        System.out.println("--------------------------------------------------------------------------------");
        matchLoc.y += interval;
        Imgcodecs.imwrite(filePath+"\\graded\\question" + i + ".jpg", questionMat);
    }
        for (int i = 0; i < 20; i++) {
        System.out.print(finalAnswers[i] + " ");

    }
    Blueprint bp = new Blueprint(finalAnswers);
    Blueprint bp2 = new Blueprint(finalAnswers);
    //System.out.println(bp.getId());
    char[] asd = bp.getAnswers();
        for (int i = 0; i < asd.length; i++) {
        //System.out.println(asd[i]);
    }
}
