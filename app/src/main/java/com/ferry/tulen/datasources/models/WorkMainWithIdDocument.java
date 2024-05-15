package com.ferry.tulen.datasources.models;

public class WorkMainWithIdDocument {

    private String idDocument;
    private  WorkMan workMan;

    public WorkMainWithIdDocument(String idDocument, WorkMan workMan) {
        this.idDocument = idDocument;
        this.workMan = workMan;
    }


    public String getIdDocument() {
        return idDocument;
    }

    public WorkMan getWorkMan() {
        return workMan;
    }
}
