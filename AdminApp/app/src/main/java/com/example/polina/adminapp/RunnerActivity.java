package com.example.polina.adminapp;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.polina.adminapp.ServerConnectionInstallation.ServerSetting;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;

public class RunnerActivity extends AppCompatActivity {

//    @Target(value= ElementType.METHOD)
//    @Retention(value= RetentionPolicy.RUNTIME)
//    public @interface FeatureSetter {
//        String str();
//    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_runner);

        ArrayList<String> featureList = loadConfig("path");
//        checkConfigurationCorrectness(featureList);

        loadFeaturesByReflection(featureList);

//        ServerSetting.init();

        startConfiguredApp(featureList);
    }

    private ArrayList<String> loadConfig(String path) {
        ArrayList<String> featureList = new ArrayList<String>();
        featureList.add("LectureEdition");
//        featureList.add("AttendedClientsActivity");
        featureList.add("BeAttended");
        featureList.add("Server");
        return featureList;
    }
//
//    private void checkConfigurationCorrectness(ArrayList<String> featureList) {
//        if (featureList.contains("LectureEdition") && featureList.add("BeAttended")) {
//            throw new IllegalArgumentException("Two features for one action button (AttendedClientsActivity and BeAttended)");
//        }
//    }

    private void loadFeaturesByReflection(ArrayList<String> featureList) {
        FeatureInstances.init(featureList);
    }

    private void startConfiguredApp(ArrayList<String> featureList) {
        Intent intent = new Intent(RunnerActivity.this, LectureListActivity.class);
        startActivity(intent);
    }
}
