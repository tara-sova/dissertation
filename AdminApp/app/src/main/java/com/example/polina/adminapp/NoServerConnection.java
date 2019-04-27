package com.example.polina.adminapp;

import java.util.ArrayList;

public class NoServerConnection {

    private static ArrayList<Lecture> lectureList = null;
    private static int counter = 0;

    public NoServerConnection() {
        lectureList = new ArrayList<>();
    }

    public Lecture addLecture(Lecture lecture) {
        lecture.setId(counter);
        lectureList.add(lecture);
        ++counter;
        return lecture;
    }

    public void updateLecture(Lecture lecture, long id) {
        Lecture lectureForRemove = null;
        for (Lecture lect : lectureList) {
            if (lect.getId() == id) {
                lectureForRemove = lect;
                break;
            }
        }
        lectureList.remove(lectureForRemove);
        lectureList.add(lecture);
    }

    public Lecture getLectureById(long id) {
        Lecture lecture = null;
        for (Lecture lect : lectureList) {
            if (lect.getId() == id) {
                lecture = lect;
                break;
            }
        }
        return lecture;
    }
}
