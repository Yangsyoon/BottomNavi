package com.example.tourlist.A_Place;



public class PlaceDataHolder {
    private static PlaceDataHolder instance;
    private Place place;

    private PlaceDataHolder() {}

    public static PlaceDataHolder getInstance() {
        if (instance == null) {
            instance = new PlaceDataHolder();
        }
        return instance;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }
}
