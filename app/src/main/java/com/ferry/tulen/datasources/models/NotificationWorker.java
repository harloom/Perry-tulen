package com.ferry.tulen.datasources.models;

import com.google.firebase.Timestamp;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class NotificationWorker {

    private String formIdUser;
    private String idWorker;
    private  String message;
    private Date dateCreated;

    public NotificationWorker(String formIdUser, String idWorker, String message, Date dateCreated) {
        this.formIdUser = formIdUser;
        this.idWorker = idWorker;
        this.message = message;
        this.dateCreated = dateCreated;
    }

    public String getFormIdUser() {
        return formIdUser;
    }

    public void setFormIdUser(String formIdUser) {
        this.formIdUser = formIdUser;
    }

    public String getIdWorker() {
        return idWorker;
    }

    public void setIdWorker(String idWorker) {
        this.idWorker = idWorker;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }
    public Map<String, Object> createMap() {
        Timestamp dateCreatedTimeStamp = new Timestamp(dateCreated.getTime() / 1000, 0);
        Map<String, Object> map = new HashMap<>();
        map.put("formIdUser", formIdUser);
        map.put("idWorker", idWorker);
        map.put("message", message);
        map.put("dateCreated", dateCreatedTimeStamp);

        return map;
    }

    public static NotificationWorker toObject(Map<String, Object> map) {

        if (map == null) {
            return null;
        }


        String formIdUser = (String) map.get("formIdUser");
        String idWorker = (String) map.get("idWorker");
        String message = (String) map.get("message");

        Timestamp endDate = (Timestamp) map.get("dateCreated");

        return new NotificationWorker(
                formIdUser,idWorker,message,endDate.toDate()
        );
    }

}
