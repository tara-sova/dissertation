package com.example.polina.adminapp;

import com.example.polina.adminapp.Features.AttendedClientsFeature;
import com.example.polina.adminapp.Features.ClientFeature;
import com.example.polina.adminapp.Features.LectureFeature;
import com.example.polina.adminapp.Features.ServerFeature;

import java.util.ArrayList;

public class FeatureInstances {

    private static final String lectureFeatureConfigStr  = "LectureEdition";
    static public LectureFeature lectureFeature = new LectureFeature();

    private static final String beAttendedFeatureConfigStr  = "BeAttended";
    static public ClientFeature clientFeature = new ClientFeature();

    private static final String attendedClientsFeatureConfigStr  = "AttendedClientsActivity";
    static public AttendedClientsFeature attendedClientsFeature = new AttendedClientsFeature();

    private static final String serverFeatureConfigStr  = "Server";
    static public ServerFeature serverFeature = new ServerFeature();

    static void init(ArrayList<String> featureInstances) {
        for (String fI : featureInstances) {
            switch(fI)
            {
                case lectureFeatureConfigStr:
                    lectureFeature.activateFeature();
                    break;
                case beAttendedFeatureConfigStr:
                    clientFeature.activateFeature();
                    break;
                case attendedClientsFeatureConfigStr:
                    attendedClientsFeature.activateFeature();
                    break;
                case serverFeatureConfigStr:
                    serverFeature.activateFeature();
                    break;
            }
        }
    }
}
