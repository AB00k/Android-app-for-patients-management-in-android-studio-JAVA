package com.example.androidhospital;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

public class UpdateRecordActivity extends AppCompatActivity {

    private EditText Patientid;
    private EditText nameEditText;
    private EditText addressEditText;
    private EditText phoneEditText;
    private Spinner provincespinner;
    private Spinner medicalStatusspinner;
    private Button pictureButton;
    private Button addreport;

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_PDF_PICKER = 2;

    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private Uri selectedPictureUri;
    private Uri selectedPdfUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_record);

        Patientid = findViewById(R.id.Patientid);
        nameEditText = findViewById(R.id.nameEditText);
        addressEditText = findViewById(R.id.addressEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        provincespinner = findViewById(R.id.provinceSpinner);
        medicalStatusspinner = findViewById(R.id.medicalStatusSpinner);
        pictureButton = findViewById(R.id.pictureButton);
        addreport = findViewById(R.id.addreportbutton);

        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        storageReference = FirebaseStorage.getInstance().getReference();

        pictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        });

        addreport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.setType("application/pdf");
                startActivityForResult(intent, REQUEST_PDF_PICKER);
            }
        });

        Button submitBtn = findViewById(R.id.updateBtn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String patientId = Patientid.getText().toString();
                String name = nameEditText.getText().toString();
                String address = addressEditText.getText().toString();
                String phoneNumber = phoneEditText.getText().toString();
                String province = provincespinner.getSelectedItem().toString();
                String medicalStatus = medicalStatusspinner.getSelectedItem().toString();

                if (patientId.isEmpty() || name.isEmpty() || address.isEmpty() || phoneNumber.isEmpty()
                        || province.equals("Select Province") || medicalStatus.equals("Select Health Condition")) {
                    Toast.makeText(UpdateRecordActivity.this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Update the data in the database using the patientId
                DatabaseReference userRef = databaseReference.child(patientId);
                userRef.child("name").setValue(name);
                userRef.child("address").setValue(address);
                userRef.child("phoneNumber").setValue(phoneNumber);
                userRef.child("province").setValue(province);
                userRef.child("medicalStatus").setValue(medicalStatus);

                // Upload the image and get the URL
                if (selectedPictureUri != null) {
                    StorageReference imageRef = storageReference.child("images/" + UUID.randomUUID().toString());
                    imageRef.putFile(selectedPictureUri)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            String imageUrl = uri.toString();

                                            // Update the image URL in the database
                                            userRef.child("imageUri").setValue(imageUrl);
                                        }
                                    });
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(UpdateRecordActivity.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                                }
                            });
                }

                // Upload the PDF file and get the URL
                if (selectedPdfUri != null) {
                    StorageReference pdfRef = storageReference.child("pdfs/" + UUID.randomUUID().toString());
                    pdfRef.putFile(selectedPdfUri)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    pdfRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            String pdfUrl = uri.toString();

                                            // Update the PDF URL in the database
                                            userRef.child("pdfUri").setValue(pdfUrl);
                                        }
                                    });
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(UpdateRecordActivity.this, "Failed to upload PDF file", Toast.LENGTH_SHORT).show();
                                }
                            });
                }

                // Display a success message
                Toast.makeText(UpdateRecordActivity.this, "Form submitted successfully", Toast.LENGTH_SHORT).show();

                // Reset the form fields
                Patientid.setText("");
                nameEditText.setText("");
                addressEditText.setText("");
                phoneEditText.setText("");
                provincespinner.setSelection(0);
                medicalStatusspinner.setSelection(0);
                selectedPictureUri = null;
                selectedPdfUri = null;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bitmap capturedImage = (Bitmap) data.getExtras().get("data");
            selectedPictureUri = getImageUri(capturedImage);

            Toast.makeText(this, "Image selected successfully", Toast.LENGTH_SHORT).show();
        } else if (requestCode == REQUEST_PDF_PICKER && resultCode == RESULT_OK) {
            selectedPdfUri = data.getData();

            Toast.makeText(this, "PDF selected successfully", Toast.LENGTH_SHORT).show();
        }
    }

    private Uri getImageUri(Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Image", null);
        return Uri.parse(path);
    }
}
