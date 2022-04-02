package com.test.testtec2.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;

import com.test.testtec2.model.IdentityDocument;
import com.test.testtec2.service.IdentityDocumentService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/identitydocuments")
public class IdentityDocumentController {

    private final static Logger logger = LoggerFactory.getLogger(IdentityDocumentController.class);

    @Autowired
    private IdentityDocumentService identityDocumentService;
    
    @CrossOrigin
    @GetMapping
    public ResponseEntity<List<IdentityDocument>> getAll(){

        HttpStatus status =  null ;
        List<IdentityDocument> identityDocumentList = new ArrayList<>();

        try {

            identityDocumentList = identityDocumentService.findAll();

            if(identityDocumentList.size()>0){
                status =  HttpStatus.OK;
            }else{
                status = HttpStatus.NOT_FOUND;
            }
            
        } catch (Exception e) {

            logger.error("Fallo el getAll en GET: " + e.getMessage());
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<List<IdentityDocument>>(identityDocumentList, status);
        
    }


    @PostMapping
    public ResponseEntity<IdentityDocument> create(@Valid @RequestBody IdentityDocument identityDocument){

        HttpStatus status =  null ;
        IdentityDocument identityDocumentTemp = new IdentityDocument();

        try {

            identityDocumentTemp = identityDocumentService.createIdentityDocument(identityDocument);
            status = HttpStatus.CREATED;

        } catch (Exception e) {

            logger.error("Fallo el create en POST: " + e.getMessage());
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            identityDocumentTemp = new IdentityDocument();
            identityDocumentTemp.setId(" INTERNAL_SERVER_ERROR !!");
        }

        return ResponseEntity.status(status).body(identityDocumentTemp);
    }



    @PutMapping("/{id}")
    public ResponseEntity<IdentityDocument> update(@Valid  @RequestBody IdentityDocument identityDocument, @PathVariable("id") String id){
        
        HttpStatus status =  null ;
        IdentityDocument identityDocumentTemp = new IdentityDocument();

        try {

            identityDocumentTemp =  identityDocumentService.findById(id);

            if(identityDocumentTemp !=null){

                identityDocument.setId(id);
                identityDocumentTemp = identityDocumentService.updateIdentityDocument(identityDocument);
                status= HttpStatus.OK;

            }else{

                status = HttpStatus.NOT_FOUND;
                identityDocumentTemp = new IdentityDocument();
                identityDocumentTemp.setId(" NOT FOUND !!");

            }

      /*  }catch (ConstraintViolationException cve) {
            logger.error("Fallo el update en Documento : IdentityDocument ");
                 for (ConstraintViolation constraintViolation : cve.getConstraintViolations()) {
                    logger.error("En Atributo  '" + constraintViolation.getPropertyPath() + "':" + constraintViolation.getMessage());
                 }*/
        }catch (Exception e) {

            logger.error("Fallo el update en PUT: " + e.getMessage());
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            identityDocumentTemp = new IdentityDocument();
            identityDocumentTemp.setId(" INTERNAL_SERVER_ERROR !!");
        }
        
        return ResponseEntity.status(status).body(identityDocumentTemp);
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<IdentityDocument> delete( @PathVariable("id") String id){

        HttpStatus status =  null ;
        IdentityDocument identityDocumentTemp = new IdentityDocument();

        try {

            identityDocumentTemp =  identityDocumentService.findById(id);

            if(identityDocumentTemp !=null){

                identityDocumentService.deleteIdentityDocument(id);
                status= HttpStatus.OK;
                identityDocumentTemp.setId(identityDocumentTemp.getId()+ " ELIMINADO !!");

            }else{

                status = HttpStatus.NOT_FOUND;
                identityDocumentTemp = new IdentityDocument();
                identityDocumentTemp.setId(" NOT FOUND !!");

            }

        } catch (Exception e) {

            logger.error("Fallo el delete en DELETE: " + e.getMessage());
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            identityDocumentTemp = new IdentityDocument();
            identityDocumentTemp.setId(" INTERNAL_SERVER_ERROR !!");
        }

        return ResponseEntity.status(status).body(identityDocumentTemp);
    }


}
