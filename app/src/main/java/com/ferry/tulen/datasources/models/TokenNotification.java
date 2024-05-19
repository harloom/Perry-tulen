package com.ferry.tulen.datasources.models;

import com.google.firebase.Timestamp;

import java.util.HashMap;
import java.util.Map;

public class TokenNotification {
    final String tokenFcm;

    public TokenNotification(String tokenFcm) {
        this.tokenFcm = tokenFcm;
    }

    public String getTokenFcm() {
        return tokenFcm;
    }


    public Map<String, Object> setTokenMap() {
       Map<String, Object> map = new HashMap<>();
        map.put("tokenFcm", tokenFcm);
        return map;
    }

}
