package com.example.tourlist;

public class TouristAttraction {

    private String name;
    private String address;
    private String description;
    private String phone;
    private String photoUrl;

    // 기존 생성자
    public TouristAttraction(String name, String address, String description, String phone, String photoUrl) {
        this.name = name;
        this.address = address;
        this.description = description;
        this.phone = phone;
        this.photoUrl = photoUrl;
    }

    // 두 개의 인자를 받는 생성자 추가
    public TouristAttraction(String name, String address) {
        this(name, address, "", "", "");
    }

    // Getter methods
    public String getName() {
        return name;
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
