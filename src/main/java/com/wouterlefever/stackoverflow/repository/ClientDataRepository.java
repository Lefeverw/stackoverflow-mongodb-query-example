package com.wouterlefever.stackoverflow.repository;

import com.wouterlefever.stackoverflow.mongo.entity.ClientData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface ClientDataRepository extends MongoRepository<ClientData, String> {

    @Query(value = "{ $and: [{'documents.clientId' :  ?0},{'documents.documentId' :  ?1}]}", fields = "{ 'documents' : 1}")
    ClientData findDocument(String clientId, String documentId);

    ClientData findByDocuments_ClientIdAndDocuments_DocumentId(String clientId, String documentId);
}