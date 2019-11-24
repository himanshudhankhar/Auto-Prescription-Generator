package com.docs.telemedicine;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

public class ShowPrecription extends AppCompatActivity {
        private TextView disease1;
        private TextView disease2;
        private TextView symptoms;
        private TextView medicines;
        private TextView comments;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_precription);

        disease1=findViewById(R.id.diseasesField1);
        disease2=findViewById(R.id.diseaseField2);
        symptoms = findViewById(R.id.symptomsField);
        medicines = findViewById(R.id.medicinesField);
        comments=findViewById(R.id.commentsField);

        Intent intent= getIntent();
       String diseasesList = intent.getExtras().getString("disease");
       String symptomsList = intent.getExtras().getString("symptoms");
       String dosageList = intent.getExtras().getString("dosage");

       String[] disList=diseasesList.split(",");
       disease1.setText(disList[0]);
       disease2.setText(disList[1]);
       symptoms.setText(symptomsList);
       medicines.setText(dosageList);
       comments.setText("None");
    }
}
