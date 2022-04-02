package com.test.testtec2.repository;

import com.test.testtec2.model.IdentityDocument;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IdentityDocumentRepository extends MongoRepository<IdentityDocument,String> {
    
}
