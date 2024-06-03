package com.example.tourlist.Course;

public class TouristCourse {

    private String name;
    private String address;
    private double latitude;
    private double longitude;
    private String description;
    private String phone;
    private String photoUrl;

    // 기존 생성자
    public TouristCourse(String name, double latitude, double longitude, String address, String description, String phone, String photoUrl) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.description = description;
        this.phone = phone;
        this.photoUrl = photoUrl;
    }

    // 두 개의 인자를 받는 생성자 추가
    public TouristCourse(String name, String address) {
        this(name,-1,-1, address, "", "", "");
    }

    //parser2
    public TouristCourse(String name) {
        this(name,-1,-1,"", "", "", "");
    }



    //프레그 3에 맞춰 생성자.
    public TouristCourse(String name, double latitude, double longitude, String address, String description, String phone) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.description = description;
        this.phone = phone;

    }




    // Getter methods
    public String getName() {
        return name;
    }

    public double getlatitude() {
        return latitude;
    }
    public double getlongitude() {
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
}
