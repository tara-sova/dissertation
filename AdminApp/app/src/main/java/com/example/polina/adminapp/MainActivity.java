package com.example.polina.adminapp;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;

import com.google.gson.Gson;

import org.springframework.http.ResponseEntity;

public class MainActivity extends ListActivity implements AdapterView.OnItemLongClickListener {

    public ArrayAdapter<String> mAdapter;
    private ArrayList<String> titlesForListActivity = new ArrayList<>();
    private ArrayList<Lecture> allLectures = new ArrayList<>();
    private ServerConnection server = new ServerConnection();
    FloatingActionButton addButton;

    private Boolean lectureEditionFeature = null;
    private Boolean attendedClientsFeature = null;
    private Boolean beAttendedFeature = null;

    @RunnerActivity.FeatureSetter(str = "LectureEdition")
    public void setLectureEdition(Boolean lectureEdition)
    {
        lectureEditionFeature = lectureEdition;
    }

    @RunnerActivity.FeatureSetter(str = "BeAttended")
    public void setAttendedClients(Boolean attendedClients)
    {
        attendedClientsFeature = attendedClients;
    }

    private String currentClient = null;
    @RunnerActivity.FeatureSetter(str = "AttendedClients")
    public void setBeAttended(Boolean beAttended)
    {
        beAttendedFeature = beAttended;
    }


    private String makeTitleForListActivity(Lecture lecture) {
        return "Лектор: " + lecture.lecturerName + "\n" + "Тема: " + lecture.theme + "\n" + "Время: " + lecture.timeStart + " - " + lecture.timeEnd;
    }

    private void restoreFeatures() {
        Boolean lectureEditionFeatureSelected = FeatureInstances.lectureFeature.isFeatureActivated();
        Boolean attendedClientsFeatureSelected = FeatureInstances.attendedClientsFeature.isFeatureActivated();
        Boolean beAttendedFeatureSelected = FeatureInstances.clientFeature.isFeatureActivated();

        setLectureEdition(lectureEditionFeatureSelected);
        setAttendedClients(attendedClientsFeatureSelected);
        setBeAttended(beAttendedFeatureSelected);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        restoreFeatures();

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

        ResponseEntity<String> postResponse1 = server.postLecture(lecture1);
        ResponseEntity<String> postResponse2 = server.postLecture(lecture2);
        allLectures.add(server.getLecture(postResponse1));
        allLectures.add(server.getLecture(postResponse2));
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

//        if (attendedClientsFeature == null)
//            return;

        if (!attendedClientsFeature)
            return;

        Gson gson = new Gson();
        Lecture lecture = server.getLectureByPosition((long) position);
        String lectureAsAString = gson.toJson(lecture);

        Intent intent = new Intent(MainActivity.this, AttendedClients.class);
        intent.putExtra("lectureAsAString", lectureAsAString);

        startActivity(intent);
    }


    private static final int EDIT_ITEM = 1;
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

        Gson gson = new Gson();
        Lecture lecture = server.getLectureByPosition((long) position);
        String lectureAsAString = gson.toJson(lecture);

        if (lectureEditionFeature) {
            Toast.makeText(getApplicationContext(), "Редактировать", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(MainActivity.this, LectureEdition.class);
            intent.putExtra("lectureAsAString", lectureAsAString);
            intent.putExtra("position", position);

            startActivityForResult(intent, EDIT_ITEM);

            return true;
        }
        if (beAttendedFeature) {
            Toast.makeText(getApplicationContext(), "Редактировать", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(MainActivity.this, ClientForm.class);
            intent.putExtra("lectureAsAString", lectureAsAString);
            intent.putExtra("position", position);
            intent.putExtra("currentClient", currentClient);

            startActivityForResult(intent, EDIT_ITEM);

            return true;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (resultCode != Activity.RESULT_OK)
        {
            return;
        }

        Gson gson = new Gson();
        String lectureAsAString = intent.getStringExtra("editedLectureAsAString");
        Lecture targetLecture = gson.fromJson(lectureAsAString, Lecture.class);
        int lecturePosition = intent.getExtras().getInt("position");
        if (beAttendedFeature) {
            currentClient = intent.getStringExtra("currentClient");
        }

        if (requestCode == EDIT_ITEM) {
            titlesForListActivity.set(lecturePosition, makeTitleForListActivity(targetLecture));
            allLectures.set(lecturePosition, targetLecture);
            server.putLecture(targetLecture, lecturePosition);

        } else if (requestCode == ADD_ITEM) {
            titlesForListActivity.add(makeTitleForListActivity(targetLecture));
            ResponseEntity<String> postResponse = server.postLecture(targetLecture);
            allLectures.add(server.getLecture(postResponse));
        }

        mAdapter.notifyDataSetChanged();
    }

    private static final int ADD_ITEM = 2;
    private View.OnClickListener buttonListener = new View.OnClickListener() {
        public void onClick(View v) {
            if (!lectureEditionFeature)
                return;

            Intent intent = new Intent(MainActivity.this, LectureEdition.class);

            if (v.getId() == R.id.button7)
            {
                Gson gson = new Gson();
                Lecture lecture = new Lecture("", "", "", "", 0, "", 0);
                String lectureAsAString = gson.toJson(lecture);

                intent.putExtra("lectureAsAString", lectureAsAString);
                intent.putExtra("position", -1);

                startActivityForResult(intent, ADD_ITEM);
            }
        }
    };

}







