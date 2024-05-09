package com.ferry.tulen.datasources.models;

import java.util.HashMap;
import java.util.Map;

public class User {

    private final String id;

    private final String email;

    private final String fullName;
    private final String address;

    private final String phoneNumber;

    private final String job;

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
                id,fullName,email,address,phoneNumber,job
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