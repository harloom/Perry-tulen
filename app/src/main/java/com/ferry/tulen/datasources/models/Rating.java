package com.ferry.tulen.datasources.models;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Rating {
    private  final String idUser;
    private  final String idWorkman;
    private  final int rating;

    private final String comment;


    public Rating(String idUser, String idWorkman, int rating, String comment) {
        this.idUser = idUser;
        this.idWorkman = idWorkman;
        this.rating = rating;
        this.comment = comment;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("idUser", idUser);
        map.put("idWorkman", idWorkman);
        map.put("rating", rating);
        map.put("comment", comment);
        return map;
    }

    public static Rating fromMap(Map<String, Object> map) {
        if (map == null) {
            return null;
        }
        String idUser = (String) map.get("id");
        String idWorkman = (String) map.get("fullName");

        int rating = Optional.ofNullable((Integer) map.getOrDefault("rating", null))
                .orElse(0);
        String comment = (String) map.get("comment");

        return  new Rating(
                idUser,idWorkman,rating,comment
        );

    }

}
