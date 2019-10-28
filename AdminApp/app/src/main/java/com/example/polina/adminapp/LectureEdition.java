package com.example.polina.adminapp;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;



import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;


@AnnotationList.AbstractFeature(abstractFatureName = "EventManipulations")
@AnnotationList.Feature(featureName = "LectureEdition")
public class LectureEdition extends AppCompatActivity {

    @AnnotationList.OnLongItemClickTO(featureNameFrom = "LectureListActivity")

    @AnnotationList.InComingArg(convertedClass = Integer.class)
    int position;

    @AnnotationList.InComingArg(convertedClass = Lecture.class)
    String lectureAsAString;

    @AnnotationList.InComingArg(convertedClass = Boolean.class)
    Boolean removeOpportunity;

    @AnnotationList.InComingArg(convertedClass = String.class)
    String selectedItemAsAString;

    @AnnotationList.OutComingArg(convertedClass = Boolean.class)
    Boolean wasItemRemoved = false;

    EditText lecturerEdit;
    EditText themeEdit;
    EditText abstractEdit;
    EditText timeStartEdit;
    EditText timeEndEdit;

    Button button;
    Button removeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecture_edition);

        Gson gson = new Gson();
        Intent intent = getIntent();
        lectureAsAString = intent.getStringExtra("lectureAsAString");
        Lecture lecture = gson.fromJson(lectureAsAString, Lecture.class);
        position = getIntent().getExtras().getInt("position");

        removeOpportunity = intent.getBooleanExtra("removeOpportunity", false);
        selectedItemAsAString = intent.getStringExtra("selectedItemAsAString");

        lecturerEdit = findViewById(R.id.editText9);
        themeEdit = findViewById(R.id.editText10);
        abstractEdit = findViewById(R.id.editText11);
        timeStartEdit = findViewById(R.id.editText);
        timeEndEdit = findViewById(R.id.editText5);


        button = findViewById(R.id.button);
        button.setOnClickListener(buttonListener);


        removeButton = findViewById(R.id.removeButton);
        removeButton.setOnClickListener(removeButtonListener);
        removeButton.setText("Удалить");

        if (!removeOpportunity) {
            removeButton.setVisibility(View.GONE);
        }

        // Case of addition
        if (position == -1) {
            button.setText("Добавить");

            lecturerEdit.setText("Лектор");
            themeEdit.setText("Тема");
            abstractEdit.setText("Описание");
            timeStartEdit.setText("Начало");
            timeEndEdit.setText("Конец");

        } else {
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

                lectureAsAString = gson.toJson(editedLecture);

                Intent intent = new Intent(LectureEdition.this, LectureListActivity.class);
                intent.putExtra("lectureAsAString", lectureAsAString);
                intent.putExtra("position", position);

                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        }
    };

    private View.OnClickListener removeButtonListener = new View.OnClickListener() {
        public void onClick(View v) {
            if (v.getId() == R.id.removeButton)
            {
                wasItemRemoved = true;
                Intent intent = new Intent(LectureEdition.this, LectureListActivity.class);
                intent.putExtra("selectedItemAsAString", selectedItemAsAString);
                intent.putExtra("selectedItemAsAString", selectedItemAsAString);
                intent.putExtra("wasItemRemoved", true);

                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        }
    };

}
