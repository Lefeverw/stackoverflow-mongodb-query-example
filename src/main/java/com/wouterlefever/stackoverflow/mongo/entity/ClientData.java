package com.wouterlefever.stackoverflow.mongo.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document("client_data")
public class ClientData {

    @Id
    private final String id;
    private final String email;
    private final List<SignedDocument> documents;


    public ClientData(String id, String email, List<SignedDocument> documents) {
        this.id = id;
        this.email = email;
        this.documents = documents;
    }

    public List<SignedDocument> getDocuments() {
        return documents;
    }
}
