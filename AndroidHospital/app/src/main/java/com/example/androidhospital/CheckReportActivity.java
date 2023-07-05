package com.example.androidhospital;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CheckReportActivity extends AppCompatActivity {
    private EditText idEditText;
    private Button submitIdButton;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_patient_report);

        idEditText = findViewById(R.id.idEditText);
        submitIdButton = findViewById(R.id.submitIdButton);

        // Get reference to your Firebase database
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        submitIdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String id = idEditText.getText().toString().trim();

                // Check if ID is provided
                if (id.isEmpty()) {
                    Toast.makeText(CheckReportActivity.this, "Please enter an ID", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Retrieve data from the database based on the ID
                databaseReference.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // User data found
                            UserUpdate user = dataSnapshot.getValue(UserUpdate.class);

                            // Retrieve values from the UserUpdate object
                            String name = user.getName();
                            String address = user.getAddress();
                            String phoneNumber = user.getPhoneNumber();
                            String province = user.getProvince();
                            String medicalStatus = user.getMedicalStatus();
                            String imageUri = user.getImageUri();
                            String pdfUri = user.getPdfUri();

                            if (pdfUri == null) {
                                // If pdfUri is not available, show a toast message and pass "none" to the next activity
                                Toast.makeText(CheckReportActivity.this, "PDF not available", Toast.LENGTH_SHORT).show();
                                pdfUri = "none";
                            }

                            // Pass the values to the next activity
                            Intent intent = new Intent(CheckReportActivity.this, ShowReport.class);
                            intent.putExtra("name", name);
                            intent.putExtra("address", address);
                            intent.putExtra("phoneNumber", phoneNumber);
                            intent.putExtra("province", province);
                            intent.putExtra("medicalStatus", medicalStatus);
                            intent.putExtra("imageUri", imageUri);
                            intent.putExtra("pdfUri", pdfUri);
                            startActivity(intent);
                        } else {
                            // User data not found
                            Toast.makeText(CheckReportActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(CheckReportActivity.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
