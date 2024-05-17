package com.ferry.tulen.datasources.models;

import com.google.firebase.Timestamp;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Order {
    private  String idUser;
    private  String idWorkman;

    private   String typeWork;
    private   String descWork;

    private  String fileStorage;
    private  Date startDate;
    private  Date endDate;

    private   String geohash;

    private  Long lat;

    private  Long lng;

    public Order(String idUser, String idWorkman, String typeWork, String descWork, String fileStorage, Date startDate, Date endDate, String geohash, Long lat, Long lng) {
        this.idUser = idUser;
        this.idWorkman = idWorkman;
        this.typeWork = typeWork;
        this.descWork = descWork;
        this.fileStorage = fileStorage;
        this.startDate = startDate;
        this.endDate = endDate;
        this.geohash = geohash;
        this.lat = lat;
        this.lng = lng;
    }


    public String getIdUser() {
        return idUser;
    }

    public String getIdWorkman() {
        return idWorkman;
    }

    public String getTypeWork() {
        return typeWork;
    }

    public String getDescWork() {
        return descWork;
    }

    public String getFileStorage() {
        return fileStorage;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getGeohash() {
        return geohash;
    }

    public Long getLat() {
        return lat;
    }

    public Long getLng() {
        return lng;
    }


    public Map<String, Object> createMap() {

        Timestamp startTimeStamp = new Timestamp(startDate.getTime() / 1000, 0);
        Timestamp endDateTimeStamp = new Timestamp(endDate.getTime() / 1000, 0);


        Map<String, Object> map = new HashMap<>();
        map.put("idUser", idUser);
        map.put("idWorkman", idWorkman);
        map.put("typeWork", typeWork);
        map.put("descWork", descWork);
        map.put("fileStorage", fileStorage);
        map.put("startDate", startTimeStamp);
        map.put("endDate", endDateTimeStamp);
        map.put("geohash", geohash);
        map.put("lat", lat);
        map.put("lng", lng);

        return map;
    }

    public static Order toObject(Map<String, Object> map) {


        if (map == null) {
            return null;
        }

//        Timestamp startTimeStamp = new Timestamp(startDate.getTime() / 1000, 0);
//        Timestamp endDateTimeStamp = new Timestamp(endDate.getTime() / 1000, 0);


        String idUser = (String) map.get("idUser");
        String idWorkman = (String) map.get("idWorkman");
        String typeWork = (String) map.get("typeWork");
        String descWork = (String) map.get("descWork");
        String fileStorage = (String) map.get("fileStorage");
        Timestamp startDate = (Timestamp) map.get("startDate");
        Timestamp endDate = (Timestamp) map.get("endDate");
        String geohash = (String) map.get("geohash");
        Long lat = (Long) map.get("lat");
        Long lng = (Long) map.get("lng");

        return new Order(
                idUser,idWorkman,typeWork,descWork,fileStorage,startDate.toDate(),endDate.toDate(),geohash,lat,lng
        );
    }



    //    Date date = new Date();
//
//// Convert the Date object to a Firestore Timestamp
//Timestamp timestamp = new Timestamp(date.getTime() / 1000, 0);



}