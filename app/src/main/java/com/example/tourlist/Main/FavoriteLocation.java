package com.example.tourlist.Main;

public class FavoriteLocation {
    public double latitude;
    public double longitude;
    private String place_name; // 장소 이름 추가

    private String key; // 추가된 필드

    public FavoriteLocation() {
        // Default constructor required for calls to DataSnapshot.getValue(FavoriteLocation.class)
    }

    public FavoriteLocation(String place_name, double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.place_name = place_name;
    }

        /*public FavoriteLocation(String place_name, double latitude, double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
            this.place_name = place_name;
        }*/


    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }


    public String getName() {
        return place_name;
    }

    public void setName(String name) {
        this.place_name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }


    @Override
    public String toString() {
        return place_name != null ? place_name : "Lat: " + latitude + ", Lng: " + longitude;
    }
}