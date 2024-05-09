package com.ferry.tulen.datasources.models;

import java.util.HashMap;
import java.util.Map;

public class GuardUser {
    private final String idUser;

    private final String password;



    public GuardUser(String idUser, String password) {
        this.idUser = idUser;
        this.password = password;
    }

    public String getIdUser() {
        return idUser;
    }

    public String getPassword() {
        return password;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("idUser", idUser);
        map.put("password", password);
        return map;
    }

    public static GuardUser fromMap(Map<String, Object> map) {
        if (map == null) {
            return null;
        }
        String idUser = (String) map.get("id");
        String password = (String) map.get("password");
        return new GuardUser(
                idUser,password
        );

    }
}

