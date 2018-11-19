package com.example.polina.adminapp;

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

    public Lecture(long id, String lecturerName, String theme, String abstractContent
                   , String timeStart, int intTimeStart, String timeEnd, int intTimeEnd) {
        this(lecturerName, theme, abstractContent, timeStart, intTimeStart, timeEnd, intTimeEnd);
        this.id = id;
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
    }

    public Lecture() {}
}