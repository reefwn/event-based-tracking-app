package com.example.eventbasedtracking.Model;

import org.json.JSONException;

import java.util.List;

import io.reactivex.annotations.Nullable;

public class UpcomingEvent {

    public String _id;
    public String createdBy;
    public long eventCode;
    public String eventName;
    public List<com.example.eventbasedtracking.Model.Members> members;
    public EventStart eventStart;
    public EventEnd eventEnd;
    public List<EventLocations> eventLocations;

    public UpcomingEvent(String _id, String createdBy, int eventCode, String eventName, @Nullable List<Members> members, EventStart eventStart, EventEnd eventEnd, @Nullable List<EventLocations> eventLocations) throws JSONException {
        this._id = _id;
        this.createdBy = createdBy;
        this.eventCode = eventCode;
        this.eventName = eventName;
        this.members = members;
        this.eventStart = eventStart;
        this.eventEnd = eventEnd;
        this.eventLocations = eventLocations;
    }
}

