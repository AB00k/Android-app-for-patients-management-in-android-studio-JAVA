package com.example.androidhospital;

public class UserUpdate {
    private String name;
    private String address;
    private String phoneNumber;
    private String province;
    private String medicalStatus;
    private String imageUri;

    private String pdfUri;

    public UserUpdate() {
        // Default constructor required for Firebase
    }

    public UserUpdate(String name, String address, String phoneNumber, String province, String medicalStatus, String imageUri, String pdfUri) {
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.province = province;
        this.medicalStatus = medicalStatus;
        this.imageUri = imageUri;
        this.pdfUri = pdfUri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getMedicalStatus() {
        return medicalStatus;
    }

    public void setMedicalStatus(String medicalStatus) {
        this.medicalStatus = medicalStatus;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getPdfUri() {
        return pdfUri;
    }

    public void setPdfUri(String pdfUri) {
        this.pdfUri = pdfUri;
    }
}

