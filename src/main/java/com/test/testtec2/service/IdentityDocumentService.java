package com.test.testtec2.service;

import java.util.List;
import java.util.Optional;

import com.test.testtec2.model.IdentityDocument;
import com.test.testtec2.repository.IdentityDocumentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IdentityDocumentService {
    
    @Autowired
    private IdentityDocumentRepository identityDocumentRepository;

    public List<IdentityDocument> findAll() {
        return identityDocumentRepository.findAll();
    }

    public IdentityDocument findById(String id) {

        IdentityDocument identityDocumenttemp = null;

        Optional<IdentityDocument> result = identityDocumentRepository.findById(id);

        if(result.isPresent()){
            identityDocumenttemp =  result.get();
        }
        
        return identityDocumenttemp;

         
    }


    public IdentityDocument createIdentityDocument(IdentityDocument identityDocument){

        return identityDocumentRepository.save(identityDocument);
    }

    public IdentityDocument updateIdentityDocument(IdentityDocument identityDocument){


        return identityDocumentRepository.save(identityDocument);
    }


    public void deleteIdentityDocument(String id){

         identityDocumentRepository.deleteById(id);
    }


}
