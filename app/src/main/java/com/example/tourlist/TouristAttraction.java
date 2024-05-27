package com.example.tourlist;

public class TouristAttraction {
    private String name;
    private String address;

    public TouristAttraction(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }
}
