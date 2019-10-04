package com.example.eventbasedtracking.Model;

public class EventLocations {

    public int id;
    public String locationName;
    public String locationETA;
    public String locationETD;

    EventLocations(int id, String locationName, String locationETA, String locationETD) {
        this.id = id;
        this.locationName = locationName;
        this.locationETA = locationETA;
        this.locationETD = locationETD;
    }
}
