package com.example.androidhospital;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button addPatientButton;
    private Button updateRecordButton;
    private Button checkReportButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addPatientButton = findViewById(R.id.addPatientButton);
        updateRecordButton = findViewById(R.id.updateRecordButton);
        checkReportButton = findViewById(R.id.checkReportButton);

        addPatientButton.setOnClickListener(this);
        updateRecordButton.setOnClickListener(this);
        checkReportButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;

        switch (v.getId()) {
            case R.id.addPatientButton:
                intent = new Intent(MainActivity.this, AddPatientActivity.class);
                startActivity(intent);
                break;

            case R.id.updateRecordButton:
                intent = new Intent(MainActivity.this, UpdateRecordActivity.class);
                startActivity(intent);
                break;

            case R.id.checkReportButton:
                intent = new Intent(MainActivity.this, CheckReportActivity.class);
                startActivity(intent);
                break;
        }
    }
}