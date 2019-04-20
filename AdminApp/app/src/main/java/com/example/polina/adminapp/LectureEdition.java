package com.example.polina.adminapp;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;



import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.HttpURLConnection;
//import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.web.client.RestTemplate;

public class LectureEdition extends AppCompatActivity {

    EditText lecturerEdit;
    EditText themeEdit;
    EditText abstractEdit;
    EditText timeStartEdit;
    EditText timeEndEdit;

    Button button;

    int lecturePosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecture_edition);

        Gson gson = new Gson();
        Intent intent = getIntent();
        String lectureAsAString = intent.getStringExtra("lectureAsAString");
        Lecture lecture = gson.fromJson(lectureAsAString, Lecture.class);
        lecturePosition = getIntent().getExtras().getInt("position");

        lecturerEdit = findViewById(R.id.editText9);
        themeEdit = findViewById(R.id.editText10);
        abstractEdit = findViewById(R.id.editText11);
        timeStartEdit = findViewById(R.id.editText);
        timeEndEdit = findViewById(R.id.editText5);


        button = findViewById(R.id.button);
        button.setOnClickListener(buttonListener);

        // Case of addition
        if (lecturePosition == -1) {
            button.setText("Добавить");

            lecturerEdit.setText("Лектор");
            themeEdit.setText("Тема");
            abstractEdit.setText("Описание");
            timeStartEdit.setText("Начало");
            timeEndEdit.setText("Конец");

        } else {
//            lecturerEdit.setText(lecturerName);
//            themeEdit.setText(theme);
//            abstractEdit.setText(abstractContent);
//            timeStartEdit.setText(timeStart);
//            timeEndEdit.setText(timeEnd);
            lecturerEdit.setText(lecture.lecturerName);
            themeEdit.setText(lecture.theme);
            abstractEdit.setText(lecture.abstractContent);
            timeStartEdit.setText(lecture.timeStart);
            timeEndEdit.setText(lecture.timeEnd);
        }
    }

    private String normalizeTime(String time) {
        while (time.length() < 5) {
            time = "0" + time;
        }
        return  time;
    }

    private View.OnClickListener buttonListener = new View.OnClickListener() {
        public void onClick(View v) {
            if (v.getId() == R.id.button)
            {

                Gson gson = new Gson();

                String timeStart = normalizeTime(timeStartEdit.getText().toString());
                int intTimeStart = Integer.parseInt(timeStart.replace(":", ""));

                String timeEnd = normalizeTime(timeEndEdit.getText().toString());
                int intTimeEnd = Integer.parseInt(timeEnd.replace(":", ""));


                Lecture editedLecture = new Lecture(lecturerEdit.getText().toString(),
                                                    themeEdit.getText().toString(),
                                                    abstractEdit.getText().toString(),
                                                    timeStart, intTimeStart,
                                                    timeEnd, intTimeEnd);

                String editedLectureAsAString = gson.toJson(editedLecture);


                Intent intent = new Intent(LectureEdition.this, MainActivity.class);
                intent.putExtra("editedLectureAsAString", editedLectureAsAString);
                intent.putExtra("position", lecturePosition);

                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        }
    };

}
