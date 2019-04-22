package com.example.polina.adminapp;

import android.os.StrictMode;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

public class ServerConnection {

    private static String URL = "http://10.0.2.2:8080";
    private static String lecturesURL = URL + "/lectures";
    private static long offset = -1;

    public ServerConnection() {
        setThreadPolicy();
        setOffset();
    }

//    public ServerConnection(long offsetForSet) {
//        setThreadPolicy();
//        setOffset(offsetForSet);
//    }

    private void setThreadPolicy() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    private void setOffset() {
        ResponseEntity<String> getResponse = getAllLectures();
        String idAtribute = "{\"id\":";
        Integer startSymbol = getResponse.getBody().lastIndexOf(idAtribute) + idAtribute.length();
        offset = Long.valueOf(getResponse.getBody().substring(startSymbol, getResponse.getBody().length() - 1).split(",")[0]);
    }

    public void setOffset(long offsetForSet) {
        offset = offsetForSet;
    }

    public long getOffset() {
        return offset;
    }

    public ResponseEntity<String> getAllLectures() {
        ResponseEntity<String> getResponse = (new RestTemplate()).getForEntity(lecturesURL, String.class);
        return getResponse;
    }

    public ResponseEntity<String> getLecture(long id) {
        ResponseEntity<String> getResponse = (new RestTemplate()).getForEntity(lecturesURL + "/" + Long.toString(id), String.class);
        return getResponse;
    }

    public ResponseEntity<String> postLecture(Lecture lectForPost) {
        RestTemplate rest = new RestTemplate();
        HttpEntity<Lecture> entity = new HttpEntity<Lecture>(lectForPost);
        return rest.postForEntity(lecturesURL, entity, String.class);
    }

    public Lecture getLecture(ResponseEntity<String> response) {
        ObjectMapper mapper = new ObjectMapper();
        Lecture obj = null;
        try {
            String res = response.getBody();
            if (res.contains(",\"_links")) {
                res = res.split(",\"_links")[0] + "}";
            }
            obj = mapper.readValue(res, Lecture.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return obj;
    }

    public Lecture getLectureByPosition(long position) {
        ResponseEntity<String> getResponse = getLecture(position + offset + 1);
        return getLecture(getResponse);
    }

    public void putLecture(Lecture lect, long position) {
        HttpEntity<Lecture> requestEntity = new HttpEntity<Lecture>(lect);
        HttpEntity<Lecture> response = (new RestTemplate()).exchange(lecturesURL + "/" + Long.toString(offset + position + 1), HttpMethod.PUT, requestEntity, Lecture.class);
        return;
    }
}
