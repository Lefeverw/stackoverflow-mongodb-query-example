package com.wouterlefever.stackoverflow.mongo.entity;

public class SignedDocument {

    private final String clientId;
    private final String documentId;
    private final String title;
    private final boolean signed;

    public SignedDocument(String clientId, String documentId, String title, boolean signed) {
        this.clientId = clientId;
        this.documentId = documentId;
        this.title = title;
        this.signed = signed;
    }

    public String getClientId() {
        return clientId;
    }

    public String getDocumentId() {
        return documentId;
    }
}
