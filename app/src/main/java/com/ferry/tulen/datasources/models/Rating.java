package com.ferry.tulen.datasources.models;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Rating {
    private  final String idUser;
    private  final String idWorkman;
    private  final double rating;

    private final String comment;

    private  final String idOrder;

    public String getIdUser() {
        return idUser;
    }

    public String getIdWorkman() {
        return idWorkman;
    }

    public double getRating() {
        return rating;
    }

    public String getIdOrder() {
        return idOrder;
    }

    public String getComment() {
        return comment;
    }

    public Rating(String idUser, String idWorkman, double rating, String comment, String idOrder) {
        this.idUser = idUser;
        this.idWorkman = idWorkman;
        this.rating = rating;
        this.comment = comment;
        this.idOrder = idOrder;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("idUser", idUser);
        map.put("idWorkman", idWorkman);
        map.put("rating", rating);
        map.put("comment", comment);
        map.put("idOrder", idOrder);
        return map;
    }

    public static Rating fromMap(Map<String, Object> map) {
        if (map == null) {
            return null;
        }

        String idOrder = (String) map.get("idOrder");
        String idUser = (String) map.get("idUser");
        String idWorkman = (String) map.get("idWorkman");


        Object rating = (Object) map.get("rating");

        Double ratingDouble = 0.0;

        if(rating instanceof Double){
            ratingDouble = (Double) rating;
        }else  if( rating instanceof  Long){
            ratingDouble = ((Long) rating).doubleValue();
        }

        String comment = (String) map.get("comment");


        return  new Rating(
                idUser,idWorkman,ratingDouble,comment,idOrder
        );

    }

}
