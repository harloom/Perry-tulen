package com.ferry.tulen.datasources.models;

import com.google.firebase.Timestamp;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Order {

    private String id;
    private  String idUser;
    private  String idWorkman;
    private   String typeWork;
    private   String descWork;
    private  String categoryJob;

    private  String fileStorage;
    private  Date startDate;
    private  Date endDate;

    private   String geohash;

    private  Double lat;

    private  Double lng;

    private  long statusTransaction;

    public Order(String id,String idUser, String idWorkman, String typeWork, String descWork, String categoryJob, String fileStorage, Date startDate, Date endDate, String geohash, Double lat, Double lng,
                 long statusTransaction) {
        this.id = id;
        this.idUser = idUser;
        this.idWorkman = idWorkman;
        this.typeWork = typeWork;
        this.descWork = descWork;
        this.categoryJob = categoryJob;
        this.fileStorage = fileStorage;
        this.startDate = startDate;
        this.endDate = endDate;
        this.geohash = geohash;
        this.lat = lat;
        this.lng = lng;
        this.statusTransaction = statusTransaction;
    }

    public String getCategoryJob() {
        return categoryJob;
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

    public Double getLat() {
        return lat;
    }

    public Double getLng() {
        return lng;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getStatusTransaction() {
        return statusTransaction;
    }

    public void setStatusTransaction(long statusTransaction) {
        this.statusTransaction = statusTransaction;
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
        map.put("categoryJob",categoryJob);
        map.put("statusTransaction",statusTransaction);

        return map;
    }

    public static Order toObject(String id, Map<String, Object> map) {


        if (map == null) {
            return null;
        }

        String idUser = (String) map.get("idUser");
        String idWorkman = (String) map.get("idWorkman");
        String typeWork = (String) map.get("typeWork");
        String descWork = (String) map.get("descWork");
        String fileStorage = (String) map.get("fileStorage");
        Timestamp startDate = (Timestamp) map.get("startDate");
        Timestamp endDate = (Timestamp) map.get("endDate");
        String geohash = (String) map.get("geohash");
        Double lat = (Double) map.get("lat");
        Double lng = (Double) map.get("lng");
        String categoryJob = (String) map.get("categoryJob");
        Long  statusTransaction = (Long) map.get("statusTransaction");
        return new Order(
                id,
                idUser,idWorkman,typeWork,descWork,categoryJob,fileStorage,startDate.toDate(),endDate.toDate(),geohash,lat,lng,statusTransaction
        );
    }

    public Map<String, Object> toTransactionUpdate() {
        Map<String, Object> map = new HashMap<>();
        map.put("statusTransaction", statusTransaction);
        return map;
    }


    @Override
    public String toString() {
        return "Order{" +
                "id='" + id + '\'' +
                ", idUser='" + idUser + '\'' +
                ", idWorkman='" + idWorkman + '\'' +
                ", typeWork='" + typeWork + '\'' +
                ", descWork='" + descWork + '\'' +
                ", categoryJob='" + categoryJob + '\'' +
                ", fileStorage='" + fileStorage + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", geohash='" + geohash + '\'' +
                ", lat=" + lat +
                ", lng=" + lng +
                '}';
    }
}
