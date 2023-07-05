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

public class AddPatientActivity extends AppCompatActivity {

    private EditText nameEditText;
    private EditText addressEditText;
    private EditText phoneEditText;
    private Spinner provincespinner;
    private Spinner medicalStatusspinner;
    private Button pictureButton;

    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_patient);

        nameEditText = findViewById(R.id.nameEditText);
        addressEditText = findViewById(R.id.addressEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        provincespinner = findViewById(R.id.provinceSpinner);
        medicalStatusspinner = findViewById(R.id.medicalStatusSpinner);
        pictureButton = findViewById(R.id.pictureButton);

        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        storageReference = FirebaseStorage.getInstance().getReference();

        pictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        });

        Button submitBtn = findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEditText.getText().toString();
                String address = addressEditText.getText().toString();
                String phoneNumber = phoneEditText.getText().toString();
                String province = provincespinner.getSelectedItem().toString();
                String medicalStatus = medicalStatusspinner.getSelectedItem().toString();

                if (name.isEmpty() || address.isEmpty() || phoneNumber.isEmpty()
                        || province.equals("Select Province") || medicalStatus.equals("Select Health Condition")) {
                    Toast.makeText(AddPatientActivity.this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (selectedImageUri == null) {
                    Toast.makeText(AddPatientActivity.this, "Please capture an image", Toast.LENGTH_SHORT).show();
                    return;
                }

                StorageReference imageRef = storageReference.child("images/" + UUID.randomUUID().toString());
                imageRef.putFile(selectedImageUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String imageUrl = uri.toString();

                                        String userId = databaseReference.push().getKey();
                                        UserHelper user = new UserHelper(name, address, phoneNumber, province, medicalStatus, imageUrl);

                                        databaseReference.child(userId).setValue(user)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(AddPatientActivity.this, "Form submitted successfully", Toast.LENGTH_SHORT).show();

                                                        nameEditText.setText("");
                                                        addressEditText.setText("");
                                                        phoneEditText.setText("");
                                                        provincespinner.setSelection(0);
                                                        medicalStatusspinner.setSelection(0);
                                                        selectedImageUri = null;


                                                        Intent intent = new Intent(AddPatientActivity.this, ReturnUserKey.class);
                                                        intent.putExtra("userId", userId); // Pass the user ID as an extra
                                                        startActivity(intent);

                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(AddPatientActivity.this, "Failed to submit form", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }
                                });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AddPatientActivity.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bitmap capturedImage = (Bitmap) data.getExtras().get("data");
            selectedImageUri = getImageUri(capturedImage);

            Toast.makeText(this, "Image selected successfully", Toast.LENGTH_SHORT).show();
        }
    }

    private Uri getImageUri(Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Image", null);
        return Uri.parse(path);
    }
}
