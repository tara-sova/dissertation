package com.example.polina.adminapp;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.gson.Gson;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RunnerActivity extends AppCompatActivity {

    private Lecture lectureFeature = null;
    private LectureEdition lectureEditionFeature = null;
    private AttendedClients attendedClientsFeature = null;


    @Target(value= ElementType.METHOD)
    @Retention(value= RetentionPolicy.RUNTIME)
    public @interface FeatureSetter {
        String str();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_runner);

        ArrayList<String> featureList = loadConfig("path");
        checkConfigurationCorrectness(featureList);
        startConfiguredApp(featureList);
    }

    private ArrayList<String> loadConfig(String path) {
        ArrayList<String> featureList = new ArrayList<String>();
        featureList.add("Lecture");
//        featureList.add("LectureEdition");
//        featureList.add("AttendedClients");
        featureList.add("BeAttented");
        return featureList;
    }

    private void checkConfigurationCorrectness(ArrayList<String> featureList) {
        if (!featureList.contains("Lecture")) {
            if (!featureList.contains("LectureEdition")) {
                throw new IllegalArgumentException("Lecture edition is not supported without Lecture selection");
            }
            if (!featureList.contains("AttendedClients")) {
                throw new IllegalArgumentException("Attended clients view is not supported without Lecture selection");
            }
        }
    }

    private void startConfiguredApp(ArrayList<String> featureList) {
        Intent intent = new Intent(RunnerActivity.this, MainActivity.class);
        intent.putExtra("lectureFeatureSelected", featureList.contains("Lecture"));
        intent.putExtra("lectureEditionFeatureSelected", featureList.contains("LectureEdition"));
        intent.putExtra("attendedClientsFeatureSelected", featureList.contains("AttendedClients"));
        intent.putExtra("beAttendedFeatureSelected", featureList.contains("BeAttented"));
        startActivity(intent);
    }
}
