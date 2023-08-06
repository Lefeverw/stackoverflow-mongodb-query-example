package com.wouterlefever.stackoverflow;

import com.wouterlefever.stackoverflow.mongo.entity.ClientData;
import com.wouterlefever.stackoverflow.mongo.entity.SignedDocument;
import com.wouterlefever.stackoverflow.mongo.entity.SignedDocumentUnwind;
import com.wouterlefever.stackoverflow.repository.ClientDataRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.UnwindOperation;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.List;

@SpringBootApplication
public class Application implements CommandLineRunner {
    private final ClientDataRepository clientDataRepository;
    private final MongoTemplate mongoTemplate;

    public Application(ClientDataRepository clientDataRepository, MongoTemplate mongoTemplate) {
        this.clientDataRepository = clientDataRepository;
        this.mongoTemplate = mongoTemplate;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) {
        List<SignedDocument> signedDocuments = List.of(
                new SignedDocument("client1", "document1", "title1", true),
                new SignedDocument("client1", "document2", "title2", true));
        ClientData clientData = new ClientData("1", "john.doe@gmail.com", signedDocuments);
        clientDataRepository.save(clientData);

        String requestedClientId = "client1";
        String requestedDocumentId = "document1";
        List<SignedDocument> signedDocumentByQueryAndFiltering = filterRequestedDocument(clientDataRepository.findDocument(requestedClientId, requestedDocumentId), requestedClientId, requestedDocumentId);
        List<SignedDocument> signedDocumentByMethodAndFiltering = filterRequestedDocument(clientDataRepository.findByDocuments_ClientIdAndDocuments_DocumentId(requestedClientId, requestedDocumentId), requestedClientId, requestedDocumentId);
        List<SignedDocument> signedDocumentByUnwinding = getSignedDocumentByUnwinding(requestedClientId, requestedDocumentId);
    }

    private List<SignedDocument> getSignedDocumentByUnwinding(String requestedClientId, String requestedDocumentId) {
        UnwindOperation unwind = Aggregation.unwind("documents");
        MatchOperation match = Aggregation.match(Criteria.where("documents.clientId").is(requestedClientId).and("documents.documentId").is(requestedDocumentId));
        Aggregation aggregation = Aggregation.newAggregation(unwind, match);
        AggregationResults<SignedDocumentUnwind> results = mongoTemplate.aggregate(aggregation, "client_data",
                SignedDocumentUnwind.class);
        return results.getMappedResults().stream().map(SignedDocumentUnwind::getDocuments).toList();
    }

    private List<SignedDocument> filterRequestedDocument(ClientData clientDataRepository, String requestedClientId, String requestedDocumentId) {
        return clientDataRepository
                .getDocuments()
                .stream()
                .filter(signedDocument -> signedDocument.getClientId().equals(requestedClientId) && signedDocument.getDocumentId().equals(requestedDocumentId)).toList();
    }
}
