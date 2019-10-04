package com.example.eventbasedtracking.Retrofit;

import com.example.eventbasedtracking.Model.UpcomingEvent;

import java.util.List;

import io.reactivex.Observable;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface API {
    @POST("register")
    @FormUrlEncoded
    Observable<String> registerUser(@Field("email") String email,
                                    @Field("name") String name,
                                    @Field("password") String password);

    @POST("login")
    @FormUrlEncoded
    Observable<String> loginUser(@Field("email") String email,
                                    @Field("password") String password);

    @POST("myUpcomingEvents")
    @FormUrlEncoded
    Observable<List<UpcomingEvent>> fetchUpcomingEvents(@Field("userId") String userId);

    @POST("createEvent")
    @FormUrlEncoded
    Observable<String> createEvent(@Field("eventName") String eventName,
                                   @Field("createdBy") String createdBy,
                                   @Field("eventStartDate") String eventStartDate,
                                   @Field("eventStartTime") String eventStartTime,
                                   @Field("eventEndDate") String eventEndDate,
                                   @Field("eventEndTime") String eventEndTime,
                                   @Field("locationCount") int locationCount);

    @POST("joinEvent")
    @FormUrlEncoded
    Observable<String> joinEvent(@Field("eventCode") String eventCode,
                                   @Field("userId") String userId,
                                   @Field("userName") String userName);

    @POST("createEvent")
    @FormUrlEncoded
    Observable<String> createEvent(@Field("eventName") String eventName,
                                   @Field("createdBy") String createdBy,
                                   @Field("eventStartDate") String eventStartDate,
                                   @Field("eventStartTime") String eventStartTime,
                                   @Field("eventEndDate") String eventEndDate,
                                   @Field("eventEndTime") String eventEndTime,
                                   @Field("locationCount") int locationCount,
                                   @Field("eventLocation1") String eventLocation1,
                                   @Field("eventLocationETA1") String eventLocationETA1,
                                   @Field("eventLocationETD1") String eventLocationETD1);

    @POST("createEvent")
    @FormUrlEncoded
    Observable<String> createEvent(@Field("eventName") String eventName,
                                   @Field("createdBy") String createdBy,
                                   @Field("eventStartDate") String eventStartDate,
                                   @Field("eventStartTime") String eventStartTime,
                                   @Field("eventEndDate") String eventEndDate,
                                   @Field("eventEndTime") String eventEndTime,
                                   @Field("locationCount") int locationCount,
                                   @Field("eventLocation1") String eventLocation1,
                                   @Field("eventLocationETA1") String eventLocationETA1,
                                   @Field("eventLocationETD1") String eventLocationETD1,
                                   @Field("eventLocation2") String eventLocation2,
                                   @Field("eventLocationETA2") String eventLocationETA2,
                                   @Field("eventLocationETD2") String eventLocationETD2);

    @POST("createEvent")
    @FormUrlEncoded
    Observable<String> createEvent(@Field("eventName") String eventName,
                                   @Field("createdBy") String createdBy,
                                   @Field("eventStartDate") String eventStartDate,
                                   @Field("eventStartTime") String eventStartTime,
                                   @Field("eventEndDate") String eventEndDate,
                                   @Field("eventEndTime") String eventEndTime,
                                   @Field("locationCount") int locationCount,
                                   @Field("eventLocation1") String eventLocation1,
                                   @Field("eventLocationETA1") String eventLocationETA1,
                                   @Field("eventLocationETD1") String eventLocationETD1,
                                   @Field("eventLocation2") String eventLocation2,
                                   @Field("eventLocationETA2") String eventLocationETA2,
                                   @Field("eventLocationETD2") String eventLocationETD2,
                                   @Field("eventLocation2") String eventLocation3,
                                   @Field("eventLocationETA2") String eventLocationETA3,
                                   @Field("eventLocationETD2") String eventLocationETD3);

    @POST("createEvent")
    @FormUrlEncoded
    Observable<String> createEvent(@Field("eventName") String eventName,
                                   @Field("createdBy") String createdBy,
                                   @Field("eventStartDate") String eventStartDate,
                                   @Field("eventStartTime") String eventStartTime,
                                   @Field("eventEndDate") String eventEndDate,
                                   @Field("eventEndTime") String eventEndTime,
                                   @Field("locationCount") int locationCount,
                                   @Field("eventLocation1") String eventLocation1,
                                   @Field("eventLocationETA1") String eventLocationETA1,
                                   @Field("eventLocationETD1") String eventLocationETD1,
                                   @Field("eventLocation2") String eventLocation2,
                                   @Field("eventLocationETA2") String eventLocationETA2,
                                   @Field("eventLocationETD2") String eventLocationETD2,
                                   @Field("eventLocation2") String eventLocation3,
                                   @Field("eventLocationETA2") String eventLocationETA3,
                                   @Field("eventLocationETD2") String eventLocationETD3,
                                   @Field("eventLocation2") String eventLocation4,
                                   @Field("eventLocationETA2") String eventLocationETA4,
                                   @Field("eventLocationETD2") String eventLocationETD4);

    @POST("createEvent")
    @FormUrlEncoded
    Observable<String> createEvent(@Field("eventName") String eventName,
                                   @Field("createdBy") String createdBy,
                                   @Field("eventStartDate") String eventStartDate,
                                   @Field("eventStartTime") String eventStartTime,
                                   @Field("eventEndDate") String eventEndDate,
                                   @Field("eventEndTime") String eventEndTime,
                                   @Field("locationCount") int locationCount,
                                   @Field("eventLocation1") String eventLocation1,
                                   @Field("eventLocationETA1") String eventLocationETA1,
                                   @Field("eventLocationETD1") String eventLocationETD1,
                                   @Field("eventLocation2") String eventLocation2,
                                   @Field("eventLocationETA2") String eventLocationETA2,
                                   @Field("eventLocationETD2") String eventLocationETD2,
                                   @Field("eventLocation3") String eventLocation3,
                                   @Field("eventLocationETA3") String eventLocationETA3,
                                   @Field("eventLocationETD3") String eventLocationETD3,
                                   @Field("eventLocation4") String eventLocation4,
                                   @Field("eventLocationETA4") String eventLocationETA4,
                                   @Field("eventLocationETD4") String eventLocationETD4,
                                   @Field("eventLocation5") String eventLocation5,
                                   @Field("eventLocationETA5") String eventLocationETA5,
                                   @Field("eventLocationETD5") String eventLocationETD5);
}
