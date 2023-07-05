package com.example.androidhospital;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

import android.os.Environment;


public class ShowReport extends AppCompatActivity {
    private ImageView imageView;
    private TextView nameTextView;
    private TextView addressTextView;
    private TextView provinceTextView;
    private TextView phoneNumberTextView;
    private TextView medicalStatusTextView;
    private TextView reportTextView;
    private Button downloadReportButton;

    private String name;
    private String address;
    private String province;
    private String phoneNumber;
    private String medicalStatus;
    private String imageUri;
    private String pdfUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_report_activity);

        imageView = findViewById(R.id.imageView);
        nameTextView = findViewById(R.id.nameTextView);
        addressTextView = findViewById(R.id.addressTextView);
        provinceTextView = findViewById(R.id.provinceTextView);
        phoneNumberTextView = findViewById(R.id.phoneNumberTextView);
        medicalStatusTextView = findViewById(R.id.medicalStatusTextView);
        reportTextView = findViewById(R.id.reportTextView);
        downloadReportButton = findViewById(R.id.downloadReportButton);

        // Retrieve the data passed from the previous activity
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            name = extras.getString("name");
            address = extras.getString("address");
            province = extras.getString("province");
            phoneNumber = extras.getString("phoneNumber");
            medicalStatus = extras.getString("medicalStatus");
            imageUri = extras.getString("imageUri");
            pdfUri = extras.getString("pdfUri");
        }

        // Load and display the user image from the imageUri
        Picasso.get().load(imageUri).into(imageView);

        // Display user details
        nameTextView.setText(name);
        addressTextView.setText(address);
        provinceTextView.setText(province);
        phoneNumberTextView.setText(phoneNumber);
        medicalStatusTextView.setText(medicalStatus);


        // Check if the report is available or not
        if (pdfUri == null || pdfUri.isEmpty()) {
            reportTextView.setText("Report not generated");
            downloadReportButton.setVisibility(View.GONE);
        } else {
            reportTextView.setVisibility(View.GONE);
            downloadReportButton.setVisibility(View.VISIBLE);

            // Download report PDF when the button is clicked
            downloadReportButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    downloadReport(pdfUri);
                }
            });
        }
    }

    private void downloadReport(String pdfUri) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(pdfUri);

        // Show loading toast message
        Toast.makeText(ShowReport.this, "Downloading report...", Toast.LENGTH_SHORT).show();

        try {
            File localFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "report.pdf");

            storageReference.getFile(localFile)
                    .addOnSuccessListener(taskSnapshot -> {
                        // Dismiss the loading toast message
                        Toast.makeText(ShowReport.this, "Report downloaded successfully", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        // Dismiss the loading toast message
                        Toast.makeText(ShowReport.this, "Failed to download report: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } catch (Exception e) {
            e.printStackTrace();
            // Dismiss the loading toast message
            Toast.makeText(ShowReport.this, "Failed to download report: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}
