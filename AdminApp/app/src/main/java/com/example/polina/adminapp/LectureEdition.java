package com.example.polina.adminapp;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;



import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.HttpURLConnection;
//import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.web.client.RestTemplate;

public class LectureEdition extends AppCompatActivity {

    private String lecturerName;
    private String theme;
    private String abstractContent;
    private String timeStart;
    private int intTimeStart;
    private String timeEnd;
    private int intTimeEnd;

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

        lecturerName = getIntent().getExtras().getString("lecturerName");
        theme = getIntent().getExtras().getString("theme");
        abstractContent = getIntent().getExtras().getString("abstractContent");
        lecturePosition = getIntent().getExtras().getInt("position");

        timeStart = getIntent().getExtras().getString("timeStart");
        intTimeStart = getIntent().getExtras().getInt("intTimeStart");
        timeEnd = getIntent().getExtras().getString("timeEnd");
        intTimeEnd = getIntent().getExtras().getInt("intTimeEnd");


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
            lecturerEdit.setText(lecturerName);
            themeEdit.setText(theme);
            abstractEdit.setText(abstractContent);
            timeStartEdit.setText(timeStart);
            timeEndEdit.setText(timeEnd);
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
            Intent intent = new Intent(LectureEdition.this, MainActivity.class);

            if (v.getId() == R.id.button)
            {
                intent.putExtra("lecturerName", lecturerEdit.getText().toString());
                intent.putExtra("theme", themeEdit.getText().toString());
                intent.putExtra("abstractContent", abstractEdit.getText().toString());
                intent.putExtra("position", lecturePosition);

                String timeStart = normalizeTime(timeStartEdit.getText().toString());
                int intTimeStart = Integer.parseInt(timeStart.replace(":", ""));

                String timeEnd = normalizeTime(timeEndEdit.getText().toString());
                int intTimeEnd = Integer.parseInt(timeEnd.replace(":", ""));

                intent.putExtra("timeStart", timeStart);
                intent.putExtra("intTimeStart", intTimeStart);

                intent.putExtra("timeEnd", timeEnd);
                intent.putExtra("intTimeEnd", intTimeEnd);

                setResult(Activity.RESULT_OK, intent);
                finish();

                String lecturerName = lecturerEdit.getText().toString();
                String theme = themeEdit.getText().toString();
                String abstractContent = abstractEdit.getText().toString();

//                Lecture l = new Lecture(lecturerName, theme, abstractContent);
//
//// Create a new RestTemplate instance
//                RestTemplate restTemplate = new RestTemplate();
//
//// Make the HTTP POST request, marshaling the request to JSON, and the response to a String
//                String response = restTemplate.postForObject("http://localhost:8080/lectures", l, String.class);
            }
        }
    };



    public static void MyGETRequest() throws IOException {
        URL urlForGetRequest = new URL("http://localhost:8080/greeting");
        String readLine = null;
        HttpURLConnection conection = (HttpURLConnection) urlForGetRequest.openConnection();
        conection.setRequestMethod("GET");
        conection.setRequestProperty("id", "1"); // set userId its a sample here
        int responseCode = conection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(conection.getInputStream()));
            StringBuffer response = new StringBuffer();
            while ((readLine = in .readLine()) != null) {
                response.append(readLine);
            } in .close();
            // print result
            System.out.println("JSON String Result " + response.toString());
            //GetAndPost.POSTRequest(response.toString());
        } else {
            System.out.println("GET NOT WORKED");
        }
    }

}
