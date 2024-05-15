package com.ferry.tulen.datasources.models;

import java.util.HashMap;
import java.util.Map;

public class WorkMan {

    private  String id;

    private  String email;

    private  String fullName;
    private  String address;

    private  String phoneNumber;

    private  String job;

    public void setId(String id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return fullName;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getJob() {
        return job;
    }

    public WorkMan(String id, String email, String fullName, String address, String phoneNumber, String job) {
        this.id = id;
        this.email = email;
        this.fullName = fullName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.job = job;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("fullName", fullName);
        map.put("email", email);
        map.put("address", address);
        map.put("phoneNumber", phoneNumber);
        map.put("job", job);
        return map;
    }

    public Map<String, Object> toMapUpdate() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("fullName", fullName);
        map.put("address", address);
        map.put("phoneNumber", phoneNumber);
        map.put("job", job);
        return map;
    }

    public static WorkMan fromMap(Map<String, Object> map) {
        if (map == null) {
            return null;
        }
        String id = (String) map.get("id");
        String fullName = (String) map.get("fullName");
        String email = (String) map.get("email");
        String address = (String) map.get("address");
        String phoneNumber = (String) map.get("phoneNumber");
        String job = (String) map.get("job");
        return  new WorkMan(
                id,fullName,email,address,phoneNumber,job
        );

    }
}
