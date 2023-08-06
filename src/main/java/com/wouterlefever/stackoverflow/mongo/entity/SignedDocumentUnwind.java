package com.wouterlefever.stackoverflow.mongo.entity;

public class SignedDocumentUnwind {
    private final SignedDocument documents;

    public SignedDocumentUnwind(SignedDocument documents) {
        this.documents = documents;
    }

    public SignedDocument getDocuments() {
        return documents;
    }
}
