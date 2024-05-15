package com.ferry.tulen.datasources.models;

public class UserWithIdDocument {
    private String idDocument;
    private  User user;

    public UserWithIdDocument(String idDocument, User user) {
        this.idDocument = idDocument;
        this.user = user;
    }


    public String getIdDocument() {
        return idDocument;
    }

    public User getUser() {
        return user;
    }
}
