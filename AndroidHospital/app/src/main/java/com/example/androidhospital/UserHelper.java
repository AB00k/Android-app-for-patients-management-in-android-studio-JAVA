package com.example.androidhospital;

public class UserHelper {
    private String name;
    private String address;
    private String phoneNumber;
    private String province;
    private String medicalStatus;
    private String imageUri;

    public UserHelper() {
        // Default constructor required for Firebase
    }

    public UserHelper(String name, String address, String phoneNumber, String province, String medicalStatus, String imageUri) {
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.province = province;
        this.medicalStatus = medicalStatus;
        this.imageUri = imageUri;
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
}

