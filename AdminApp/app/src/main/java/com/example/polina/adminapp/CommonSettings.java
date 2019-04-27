package com.example.polina.adminapp;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class CommonSettings {

    private static ServerConnection server = null;
    private static NoServerConnection noServerConnection = null;

    public static void init() {
        if (FeatureInstances.serverFeature.isFeatureActivated()) {
            server = new ServerConnection();
        }
        else {
            noServerConnection = new NoServerConnection();
        }
    }

    private static void checkStatusCode(HttpStatus code) {
        if (code != HttpStatus.OK) {
            try {
                throw new Exception("Problem");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static ResponseEntity<String> postLecture(Lecture lecture) {
        ResponseEntity<String> postResponse = null;
        if (server != null) {
            postResponse = server.postLecture(lecture);
            checkStatusCode((postResponse.getStatusCode()));
        }
        else {
            noServerConnection.addLecture(lecture);
        }
        return postResponse;
    }

    public static Lecture postThanGetLecture(Lecture lecture) {
        Lecture lect = null;
        if (server != null) {
            ResponseEntity<String> postResponse = server.postLecture(lecture);
            checkStatusCode((postResponse.getStatusCode()));
            lect = server.getLecture(postResponse);
        }
        else {
            lect = noServerConnection.addLecture(lecture);
        }
        return lect;
    }

    public static void putLecture(Lecture lecture, long position) {
        if (server != null) {
            server.putLecture(lecture, position);
        }
        else {
            noServerConnection.updateLecture(lecture, position);
        }
    }

    public static Lecture getLectureByPosition(long position) {
        Lecture lecture  = null;
        if (server != null) {
            lecture = server.getLectureByPosition(position);
        }
        else {
            lecture = noServerConnection.getLectureById(position);
        }
        return lecture;
    }
}