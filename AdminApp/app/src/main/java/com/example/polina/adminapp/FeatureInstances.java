package com.example.polina.adminapp;

import java.util.ArrayList;

public class FeatureInstances {

    private static final String lectureFeatureConfigStr  = "LectureEdition";
    static public LectureFeature lectureFeature = new LectureFeature();

    private static final String beAttendedFeatureConfigStr  = "BeAttended";
    static public ClientFeature clientFeature = new ClientFeature();

    private static final String attendedClientsFeatureConfigStr  = "AttendedClients";
    static public AttendedClientsFeature attendedClientsFeature = new AttendedClientsFeature();

    static void init(ArrayList<String> featureInstances) {
        for (String fI : featureInstances) {
//            if (fI.equals(lectureFeatureConfigStr)) {
//                lectureFeature = new LectureFeature(true);
//            }
//            if (fI.equals(lectureFeatureConfigStr)) {
//                lectureFeature = new LectureFeature(true);
//            }
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
            }
        }
    }
}
