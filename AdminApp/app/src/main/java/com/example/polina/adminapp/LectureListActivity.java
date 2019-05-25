package com.example.polina.adminapp;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Optional;

import com.example.polina.adminapp.ServerConnectionInstallation.ServerSetting;
import com.google.gson.Gson;

public class LectureListActivity extends ListActivity implements AdapterView.OnItemLongClickListener {

    public ArrayAdapter<String> mAdapter;
    private ArrayList<String> titlesForListActivity = new ArrayList<>();
    private ArrayList<Lecture> allLectures = new ArrayList<>();
    FloatingActionButton addButton;

    private Boolean lectureEditionFeature = null;
    private Boolean attendedClientsFeature = null;
    private Boolean beAttendedFeature = null;
    private Boolean serverFeature = null;

    private String currentClient = null;

    private String makeTitleForListActivity(Lecture lecture) {
        return "Лектор: " + lecture.lecturerName + "\n" + "Тема: " + lecture.theme + "\n" + "Время: " + lecture.timeStart + " - " + lecture.timeEnd;
    }

    private void restoreFeatures() {
        lectureEditionFeature = FeatureInstances.lectureFeature.isFeatureActivated();
        attendedClientsFeature = FeatureInstances.attendedClientsFeature.isFeatureActivated();
        beAttendedFeature = FeatureInstances.clientFeature.isFeatureActivated();
        serverFeature = FeatureInstances.serverFeature.isFeatureActivated();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        restoreFeatures();
        ServerSetting.init(serverFeature);

        Lecture lecture1 = new Lecture("Юрий Литвинов", "REAL.NET", "Введение в metaCase системы и демонстрация работы REAL.NET"
                                        , "08:30", 830, "09:30", 930);
        titlesForListActivity.add(makeTitleForListActivity(lecture1));

        Lecture lecture2 = new Lecture("Артемий Безгузиков", "Бизнес-тренинг \"Мир айти и как его найти\"", "Артемий расскажет вам, как стать успешным"
                                         , "10:30", 1030, "11:30", 1130);
        titlesForListActivity.add(makeTitleForListActivity(lecture2));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addButton = findViewById(R.id.button7);
        addButton.setOnClickListener(buttonListener);

        mAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, titlesForListActivity);

        setListAdapter(mAdapter);
        getListView().setOnItemLongClickListener(this);

        allLectures.add(ServerSetting.postThanGetLecture(lecture1));
        allLectures.add(ServerSetting.postThanGetLecture(lecture2));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        if (!attendedClientsFeature)
            return;
        Intent intent = intentSending(AttendedClientsActivity.class, position, -100500);
        startActivity(intent);

//        Gson gson = new Gson();
//        Intent intent = new Intent(LectureListActivity.this, AttendedClientsActivity.class);
//
//
//        for (Field field : AttendedClientsActivity.class.getDeclaredFields()) {
//            if (field.getAnnotationsByType(AnnotationList.InComingArg.class).length > 0) {
//                String neededFieldName = field.getName();
//                Class neededFieldDefaultType = field.getType();
//                for (AnnotationList.InComingArg ann : field.getAnnotationsByType(AnnotationList.InComingArg.class)){
//                    Class neededFieldType = ann.convertedClass();
//                    if (neededFieldType.getName().equals(Lecture.class.getName())) {
//                        Lecture lecture = ServerSetting.getLectureByPosition((long) position);
//                        String lectureAsAString = gson.toJson(lecture);
//                        intent.putExtra(neededFieldName, neededFieldDefaultType);
//                    }
//                }
//            }
//        }
//        startActivity(intent);

//        AttendedClientsActivity.class.getDeclaredFields()[0].getAnnotationsByType(AnnotationList.IncomingArg.class)

//        Gson gson = new Gson();
//        Lecture lecture = ServerSetting.getLectureByPosition((long) position);
//        String lectureAsAString = gson.toJson(lecture);
//
//        Intent intent = new Intent(LectureListActivity.this, AttendedClientsActivity.class);
//        intent.putExtra("lectureAsAString", lectureAsAString);
//
//        startActivity(intent);
    }

    private static final int EDIT_ITEM = 1;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//
//        Gson gson = new Gson();
//        Lecture lecture = ServerSetting.getLectureByPosition((long) position);
//        String lectureAsAString = gson.toJson(lecture);
//
        if (lectureEditionFeature) {
            Toast.makeText(getApplicationContext(), "Редактировать", Toast.LENGTH_SHORT).show();
//
//            Intent intent = new Intent(LectureListActivity.this, LectureEdition.class);
//            intent.putExtra("lectureAsAString", lectureAsAString);
//            intent.putExtra("position", position);
            Intent intent = intentSending(LectureEdition.class, position, -100500);
            startActivityForResult(intent, EDIT_ITEM);

            return true;
        }
        if (beAttendedFeature) {
            Toast.makeText(getApplicationContext(), "Записаться / Отписаться", Toast.LENGTH_SHORT).show();
//
//            Intent intent = new Intent(LectureListActivity.this, ClientFormActivity.class);
//            intent.putExtra("lectureAsAString", lectureAsAString);
//            intent.putExtra("position", position);
//            intent.putExtra("currentClient", currentClient);
            Intent intent = intentSending(ClientFormActivity.class, position, -100500);
            startActivityForResult(intent, EDIT_ITEM);

            return true;
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        Pair<Lecture, Integer> res = intentGetting(ClientFormActivity.class, intent);
        Lecture targetLecture = res.first;
        Integer lecturePosition = res.second;
        if (resultCode != Activity.RESULT_OK)
        {
            return;
        }

        if (requestCode == EDIT_ITEM) {
            titlesForListActivity.set(lecturePosition, makeTitleForListActivity(targetLecture));
            allLectures.set(lecturePosition, targetLecture);
            ServerSetting.putLecture(targetLecture, lecturePosition);

        } else if (requestCode == ADD_ITEM) {
            titlesForListActivity.add(makeTitleForListActivity(targetLecture));
            allLectures.add(ServerSetting.postThanGetLecture(targetLecture));
        }

        mAdapter.notifyDataSetChanged();
    }

    private static final int ADD_ITEM = 2;
    private View.OnClickListener buttonListener = new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        public void onClick(View v) {
            if (!lectureEditionFeature)
                return;

            if (v.getId() == R.id.button7)
            {
                Intent intent = intentSending(LectureEdition.class, -1, ADD_ITEM);
                startActivityForResult(intent, ADD_ITEM);
            }
        }
    };


    @RequiresApi(api = Build.VERSION_CODES.N)
    private Intent intentSending(Class<?> classTo, int position, int requestCode) {
        Gson gson = new Gson();
        Intent intent = new Intent(LectureListActivity.this, classTo);
        for (Field field : classTo.getDeclaredFields()) {
            AnnotationList.InComingArg[] inComingArgs = field.getAnnotationsByType(AnnotationList.InComingArg.class);
            if (inComingArgs.length > 0) {
                String neededFieldName = field.getName();
                Class neededFieldDefaultType = field.getType();
                for (AnnotationList.InComingArg ann : inComingArgs) {
                    Class neededFieldType = ann.convertedClass();
                    if (neededFieldType.getName().equals(Lecture.class.getName())) {
                        if (position == -100500) {
                            try {
                                throw new Exception("Wrong args");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        Lecture lecture;
                        if (requestCode == ADD_ITEM) {
                            lecture = new Lecture("", "", "", "", 0, "", 0);
                        } else {
                            lecture = ServerSetting.getLectureByPosition((long) position);
                        }
                        String lectureAsAString = gson.toJson(lecture);
                        intent.putExtra(neededFieldName, lectureAsAString);
                    }
                    if (neededFieldType.getName().equals(Integer.class.getName())) {
                        intent.putExtra(neededFieldName, position);
                    }
                    if (neededFieldType.getName().equals(String.class.getName())) {
                        intent.putExtra(neededFieldName, position);
                    }
                }
            }
        }
        return intent;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private Pair<Lecture, Integer> intentGetting(Class<?> classTo, Intent intent) {
        Lecture targetLecture = null;
        int lecturePosition = -100500;
        Gson gson = new Gson();
        for (Field field : classTo.getDeclaredFields()) {
            AnnotationList.OutComingArg[] outComingArgs = field.getAnnotationsByType(AnnotationList.OutComingArg.class);
            if (outComingArgs.length > 0) {
                String neededFieldName = field.getName();
                Class neededFieldDefaultType = field.getType();
                for (AnnotationList.OutComingArg ann : outComingArgs) {
                    Class neededFieldType = ann.convertedClass();
                    if (neededFieldType.getName().equals(Lecture.class.getName())) {
                        String lectureAsAString = intent.getStringExtra(neededFieldName);
                        targetLecture = gson.fromJson(lectureAsAString, Lecture.class);
                    }
                    if (neededFieldType.getName().equals(Integer.class.getName())) {
                        lecturePosition = intent.getExtras().getInt(neededFieldName);
                    }
                    if (neededFieldType.getName().equals(String.class.getName())) {
                        String gettedValue = intent.getExtras().getString(neededFieldName);
                        try {
                            Class currentClass = this.getClass();
                            Field f1 = currentClass.getDeclaredField(neededFieldName);
                            f1.set(this, gettedValue);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return new Pair<>(targetLecture, lecturePosition);
    }



}







