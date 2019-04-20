package com.example.polina.adminapp;

import com.google.gson.Gson;

import java.util.ArrayList;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class Lecture {
    private long id;
    public String lecturerName;
    public String theme;
    public String abstractContent;

    public String timeStart;
    public int intTimeStart;
    public String timeEnd;
    public int intTimeEnd;

    Gson gson = new Gson();
    ArrayList<String> attentedClientsList = new ArrayList<>();

    String attentedClients;

    public Lecture(long id, String lecturerName, String theme, String abstractContent
                   , String timeStart, int intTimeStart, String timeEnd, int intTimeEnd) {
        this(lecturerName, theme, abstractContent, timeStart, intTimeStart, timeEnd, intTimeEnd);
        this.id = id;
        this.attentedClients = gson.toJson(attentedClientsList);
    }

    public Lecture(String lecturerName, String theme, String abstractContent
            , String timeStart, int intTimeStart, String timeEnd, int intTimeEnd) {
        this.lecturerName = lecturerName;
        this.theme = theme;
        this.abstractContent = abstractContent;

        this.timeStart = timeStart;
        this.intTimeStart = intTimeStart;
        this.timeEnd = timeEnd;
        this.intTimeEnd = intTimeEnd;
        this.attentedClients = gson.toJson(attentedClientsList);
    }

    public Lecture(String lecturerName, String theme, String abstractContent
            , String timeStart, int intTimeStart, String timeEnd, int intTimeEnd
                   , String attentedClients) {
        this.lecturerName = lecturerName;
        this.theme = theme;
        this.abstractContent = abstractContent;

        this.timeStart = timeStart;
        this.intTimeStart = intTimeStart;
        this.timeEnd = timeEnd;
        this.intTimeEnd = intTimeEnd;
        this.attentedClients = attentedClients;
    }

    public Lecture() {}
}