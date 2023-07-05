package com.example.androidhospital;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ReturnUserKey extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.returnuserkey);

        String userId = getIntent().getStringExtra("userId");

        TextView userIdTextView = findViewById(R.id.userIdTextView);
        userIdTextView.setText("User ID: " + userId);

        // Print out the user ID
        Log.d("NewActivity", "User ID: " + userId);
    }
}
