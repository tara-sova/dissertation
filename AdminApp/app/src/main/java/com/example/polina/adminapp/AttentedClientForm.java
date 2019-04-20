package com.example.polina.adminapp;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;



import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.HttpURLConnection;
import java.util.ArrayList;
//import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.web.client.RestTemplate;

public class AttentedClientForm extends AppCompatActivity {


    private EditText nameEdit;
    private CheckBox checkBox;
    private Boolean checkBoxStateOnCreateState;
    private Button button;

    private int lecturePosition;
    private ServerConnection server;

    private Lecture lecture = null;
    public void setLecture(Lecture lecture)
    {
        this.lecture = lecture;
    }

    private String currentClient = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attented_client_form);

        Intent intent = getIntent();
        Long serverOffset = intent.getLongExtra("serverOffset", -1);
        lecturePosition = getIntent().getExtras().getInt("position");
        currentClient = getIntent().getStringExtra("currentClient");

        ServerConnection server = new ServerConnection(serverOffset);

        nameEdit = findViewById(R.id.clientName);
        checkBox = findViewById(R.id.checkBox);
        button = findViewById(R.id.confirmButton);
        button.setOnClickListener(buttonListener);

        lecture = server.getLectureByPosition((long) lecturePosition);

        if (currentClient != null) {
            nameEdit.setText(currentClient);
            if (lecture.attentedClients.contains(currentClient)) {
                checkBox.setChecked(true);
                checkBoxStateOnCreateState = checkBox.isChecked();
            }
            return;
        }
        nameEdit.setText("Ваше имя");
        checkBox.setChecked(false);
        checkBoxStateOnCreateState = checkBox.isChecked();
    }

    private View.OnClickListener buttonListener = new View.OnClickListener() {
        public void onClick(View v) {
            if (v.getId() == R.id.confirmButton)
            {
                currentClient = nameEdit.getText().toString();

                Gson gson = new Gson();

                ArrayList<String> attentedClientsList = gson.fromJson(lecture.attentedClients, ArrayList.class);

                Boolean checkBoxStateNow = checkBox.isChecked();
                if (!checkBoxStateNow.equals(checkBoxStateOnCreateState)) {
                    if (checkBoxStateNow) {
                        attentedClientsList.add(currentClient);
                        String attentedClientsAsAString = gson.toJson(attentedClientsList);
                        lecture.attentedClients = attentedClientsAsAString;
                    }
                    else {
                        attentedClientsList.remove(currentClient);
                        String attentedClientsAsAString = gson.toJson(attentedClientsList);
                        lecture.attentedClients = attentedClientsAsAString;
                    }
                }

                String editedLectureAsAString = gson.toJson(lecture);


                Intent intent = new Intent(AttentedClientForm.this, MainActivity.class);
                intent.putExtra("currentClient", currentClient);
                intent.putExtra("editedLectureAsAString", editedLectureAsAString);
                intent.putExtra("position", lecturePosition);

                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        }
    };

}
