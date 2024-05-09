package com.ferry.tulen.datasources.models;

import java.util.HashMap;
import java.util.Map;

public class GuardWorkMan {
    final String idUser;

    final String password;

    public GuardWorkMan(String idUser, String password) {
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

    public static GuardWorkMan fromMap(Map<String, Object> map) {
        if (map == null) {
            return null;
        }
        String idUser = (String) map.get("id");
        String password = (String) map.get("password");
        return new GuardWorkMan(
                idUser,password
        );

    }
}
