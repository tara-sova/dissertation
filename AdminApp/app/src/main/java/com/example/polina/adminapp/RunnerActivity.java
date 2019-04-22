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

        loadFeaturesByReflection(featureList);

        startConfiguredApp(featureList);
    }

    private ArrayList<String> loadConfig(String path) {
        ArrayList<String> featureList = new ArrayList<String>();
//        featureList.add("LectureEdition");
        featureList.add("AttendedClients");
        featureList.add("BeAttended");
        return featureList;
    }

    private void checkConfigurationCorrectness(ArrayList<String> featureList) {
        if (featureList.contains("LectureEdition") && featureList.add("BeAttended")) {
            throw new IllegalArgumentException("Two features for one action button (AttendedClients and BeAttended)");
        }
    }

    private void loadFeaturesByReflection(ArrayList<String> featureList) {
        FeatureInstances.init(featureList);
    }

    private void startConfiguredApp(ArrayList<String> featureList) {
        Intent intent = new Intent(RunnerActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
