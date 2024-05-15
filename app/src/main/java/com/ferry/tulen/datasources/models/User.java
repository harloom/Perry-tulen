package com.ferry.tulen.datasources.models;

import java.util.HashMap;
import java.util.Map;

public class User {

    private  String id;

    private  String email;

    private  String fullName;
    private  String address;

    private  String phoneNumber;

    private  String job;

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }




    public User(String id, String email, String fullName, String address, String phoneNumber, String job) {
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

    public Map<String, Object> toUpdateMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("fullName", fullName);
        map.put("address", address);
        map.put("phoneNumber", phoneNumber);
        map.put("job", job);
        return map;
    }


    public static User fromMap(Map<String, Object> map) {
        if (map == null) {
            return null;
        }
        String id = (String) map.get("id");
        String fullName = (String) map.get("fullName");
        String email = (String) map.get("email");
        String address = (String) map.get("address");
        String phoneNumber = (String) map.get("phoneNumber");
        String job = (String) map.get("job");
        return  new User(
                id,email,fullName,address,phoneNumber,job
        );

    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", fullName='" + fullName + '\'' +
                ", address='" + address + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", job='" + job + '\'' +
                '}';
    }
}

/// example
//User user = new User("John Doe", "john.doe@example.com", 30);
//
//// Add user to Firestore
//db.collection("users").add(user.toMap());