package com.example.polina.adminapp;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import com.example.polina.adminapp.Lecture;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class MainActivity extends ListActivity implements AdapterView.OnItemLongClickListener {
    public ArrayAdapter<String> mAdapter;

    private ArrayList<String> titlesForListActivity = new ArrayList<>();
    private ArrayList<Lecture> allLectures = new ArrayList<>();
    private ServerConnection server = new ServerConnection();

    FloatingActionButton addButton;


    private String makeTitleForListActivity(Lecture lecture) {
        return "Лектор: " + lecture.lecturerName + "\n" + "Тема: " + lecture.theme + "\n" + "Время: " + lecture.timeStart + " - " + lecture.timeEnd;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        Lecture lecture1 = new Lecture("Юрий Литвинов111", "REAL.NET", "Как Qreal, только на .NET-e"
                                        , "08:30", 830, "09:30", 930);
        titlesForListActivity.add(makeTitleForListActivity(lecture1));

        Lecture lecture2 = new Lecture("Артемий Безгузиков", "Как стать успешным, если тебе почти 23 и ты живешь с мамой", "Никак"
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

//        ResponseEntity<String> get_response = (new RestTemplate()).getForEntity("http://localhost:8080/lectures" + "/1", String.class);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
//        Toast.makeText(getApplicationContext(),
//                "Вы выбрали " + (position + 1) + " элемент", Toast.LENGTH_SHORT).show();

//        Toast.makeText(getApplicationContext(),
//                "Вы выбрали " + l.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(MainActivity.this, AttendedClients.class);

//        Lecture lecture = allLectures.get(position);
//        intent.putExtra("lecturerName", lecture.lecturerName);
//        intent.putExtra("theme", "\n\n"+ lecture.theme);
//        intent.putExtra("abstractContent", lecture.abstractContent);

        startActivity(intent);

//        Intent intent = new Intent(MainActivity.this, LectureInfoActivity.class);
//        startActivity(intent);
    }


    private static final int EDIT_ITEM = 1;
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        //String selectedItem = parent.getItemAtPosition(position).toString();

        Toast.makeText(getApplicationContext(), "Редактировать", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(MainActivity.this, LectureEdition.class);

//        Lecture lecture = allLectures.get(position);

        Lecture lecture = server.getLectureByPosition((long)position);
        intent.putExtra("lecturerName", lecture.lecturerName);
        intent.putExtra("theme", lecture.theme);
        intent.putExtra("abstractContent", lecture.abstractContent);
        intent.putExtra("position", position);
        intent.putExtra("timeStart", lecture.timeStart);
        intent.putExtra("intTimeStart", lecture.intTimeStart);
        intent.putExtra("timeEnd", lecture.timeEnd);
        intent.putExtra("intTimeEnd", lecture.intTimeEnd);

        startActivityForResult(intent, EDIT_ITEM);

//        mAdapter.remove(selectedItem);
//        mAdapter.notifyDataSetChanged();
//
//        Toast.makeText(getApplicationContext(),
//                selectedItem + " удалён.",
//                Toast.LENGTH_SHORT).show();

        return true;
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK)
        {
            return;
        }

        String lecturerName = data.getStringExtra("lecturerName");
        String theme = data.getStringExtra("theme");
        String abstractContent = data.getStringExtra("abstractContent");
        int lecturePosition = data.getIntExtra("position", 0);
        String timeStart = data.getStringExtra("timeStart");
        int intTimeStart = data.getIntExtra("intTimeStart", 0);
        String timeEnd = data.getStringExtra("timeEnd");
        int intTimeEnd = data.getIntExtra("intTimeEnd", 0);


        Lecture targetLecture = new Lecture(lecturerName, theme, abstractContent, timeStart, intTimeStart, timeEnd, intTimeEnd);

        if (requestCode == EDIT_ITEM) {
            titlesForListActivity.set(lecturePosition, makeTitleForListActivity(targetLecture));
            allLectures.set(lecturePosition, targetLecture);

        } else if (requestCode == ADD_ITEM) {
            titlesForListActivity.add(makeTitleForListActivity(targetLecture));
            allLectures.add(targetLecture);
        }

        Collections.sort(allLectures, new Comparator<Lecture>() {
            @Override
            public int compare(Lecture l1, Lecture l2) {
                // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                return l1.intTimeStart < l2.intTimeStart ? -1 : (l1.intTimeStart > l2.intTimeStart) ? 1 : 0;
            }
        });

        for (int i = 0; i < allLectures.size(); ++i) {
            Lecture lecture = allLectures.get(i);
            String title = makeTitleForListActivity(lecture);
            titlesForListActivity.set(i, title);
        }

        mAdapter.notifyDataSetChanged();
    }

    private static final int ADD_ITEM = 2;
    private View.OnClickListener buttonListener = new View.OnClickListener() {
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, LectureEdition.class);

            if (v.getId() == R.id.button7)
            {
                intent.putExtra("lecturerName", "");
                intent.putExtra("theme", "");
                intent.putExtra("abstractContent", "");
                intent.putExtra("position", -1);
                intent.putExtra("timeStart", "");
                intent.putExtra("intTimeStart", 0);
                intent.putExtra("timeEnd", "");
                intent.putExtra("intTimeEnd", 0);

                startActivityForResult(intent, ADD_ITEM);
            }
        }
    };

}







