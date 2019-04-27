package com.example.polina.adminapp;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;



import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.google.gson.Gson;

import java.util.ArrayList;
//import org.springframework.boot.test.web.client.TestRestTemplate;


public class ClientFormActivity extends AppCompatActivity {


    private EditText nameEdit;
    private CheckBox checkBox;
    private Boolean checkBoxStateOnCreateState;
    private Button button;

    private int lecturePosition;

    private Lecture lecture = null;
    private String currentClient = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attented_client_form);

        Gson gson = new Gson();
        Intent intent = getIntent();
        String lectureAsAString = intent.getStringExtra("lectureAsAString");
        lecture = gson.fromJson(lectureAsAString, Lecture.class);
        lecturePosition = getIntent().getExtras().getInt("position");
        currentClient = getIntent().getStringExtra("currentClient");

        nameEdit = findViewById(R.id.clientName);
        checkBox = findViewById(R.id.checkBox);
        button = findViewById(R.id.confirmButton);
        button.setOnClickListener(buttonListener);

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
                ArrayList<String> attentedClientsList = lecture.attentedClients;
                Boolean checkBoxStateNow = checkBox.isChecked();
                if (!checkBoxStateNow.equals(checkBoxStateOnCreateState)) {
                    if (checkBoxStateNow) {
                        attentedClientsList.add(currentClient);
                        lecture.updateAttendedClientsList(attentedClientsList);
                    }
                    else {
                        attentedClientsList.remove(currentClient);
                        lecture.updateAttendedClientsList(attentedClientsList);
                    }
                }

                String editedLectureAsAString = gson.toJson(lecture);

                Intent intent = new Intent(ClientFormActivity.this, LectureListActivity.class);
                intent.putExtra("currentClient", currentClient);
                intent.putExtra("editedLectureAsAString", editedLectureAsAString);
                intent.putExtra("position", lecturePosition);

                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        }
    };

}
