package com.example.tourlist.Tourist_Detail_Activity.Detail_files;

public class TouristPlace {
    private String placeName;
    private double latitude;
    private double longitude;
    private String address;
    private String description;
    private String phone;

    private String photoUrl;

    public TouristPlace(String placeName, double latitude, double longitude, String address, String description, String phone) {
        this.placeName = placeName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.description = description;
        this.phone = phone;
    }

    //long lang 의문의 오류 떠서 이렇게 생성자
    public TouristPlace(String placeName, String address, String description, String phone) {
        this.placeName = placeName;
        this.address = address;
        this.description = description;
        this.phone = phone;
    }

    public String getPlaceName() {
        return placeName;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getAddress() {
        return address;
    }

    public String getDescription() {
        return description;
    }

    public String getPhone() {
        return phone;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}