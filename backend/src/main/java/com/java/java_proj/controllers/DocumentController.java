package com.java.java_proj.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.java_proj.dto.request.forcreate.CRequestDocument;
import com.java.java_proj.dto.request.forupdate.URequestDocument;
import com.java.java_proj.dto.response.fordetail.DResponseDocument;
import com.java.java_proj.dto.response.forlist.LResponseDocument;
import com.java.java_proj.exceptions.HttpException;
import com.java.java_proj.services.templates.DocumentService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Null;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/document")
@Api(tags = "Document")
public class DocumentController {

    @Autowired
    DocumentService documentService;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    Validator validator;


    @GetMapping()
    public ResponseEntity<Page<LResponseDocument>> getAllDocument(@RequestParam(value = "name", defaultValue = "") String name,
                                                                  @RequestParam(value = "orderBy", defaultValue = "id") String orderBy,
                                                                  @RequestParam(value = "pageNo", defaultValue = "0") Integer page,
                                                                  @RequestParam(value = "pageSize", defaultValue = "10") Integer size,
                                                                  @RequestParam(value = "orderDirection", defaultValue = "DESC") String orderDirection) {

        List<String> allowedFields = Arrays.asList("id", "name");
        if (!allowedFields.contains(orderBy)) {
            throw new HttpException(HttpStatus.BAD_REQUEST, "Order by column " + orderBy + " is illegal!");
        }

        List<String> allowedSort = Arrays.asList("ASC", "DESC");
        if (!allowedSort.contains(orderDirection)) {
            throw new HttpException(HttpStatus.BAD_REQUEST, "Sort Direction " + orderDirection + " is illegal!");
        }

        Page<LResponseDocument> documentPage = documentService.getAllDocument(name, page, size, orderBy, orderDirection);

        return new ResponseEntity<>(documentPage, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/{documentId}/{documentVersion}")
    public ResponseEntity<DResponseDocument> getOneDocument(@PathVariable Integer documentId,
                                                            @PathVariable String documentVersion) {

        DResponseDocument documents = documentService.getOneDocument(documentId, documentVersion);

        return new ResponseEntity<>(documents, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Null> createDocument(@RequestPart String content,
                                               @RequestPart(required = false) MultipartFile file) throws JsonProcessingException {

        CRequestDocument requestDocument = objectMapper.readValue(content, CRequestDocument.class);
        if (file == null || file.isEmpty()) {
            throw new HttpException(HttpStatus.BAD_REQUEST, "File is required");
        }
        requestDocument.setFile(file);

        // get validation error
        DataBinder binder = new DataBinder(requestDocument);
        binder.setValidator(validator);
        binder.validate();
        BindingResult bindingResult = binder.getBindingResult();
        if (bindingResult.hasErrors()) {
            throw new HttpException(HttpStatus.BAD_REQUEST, bindingResult);
        }


        documentService.createDocument(requestDocument);

        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @PutMapping("")
    public ResponseEntity<Null> updateDocument(@RequestPart String content,
                                               @RequestPart(required = false) MultipartFile file) throws JsonProcessingException {

        URequestDocument requestDocument = objectMapper.readValue(content, URequestDocument.class);
        if (file == null || file.isEmpty()) {
            throw new HttpException(HttpStatus.BAD_REQUEST, "File is required");
        }

        requestDocument.setFile(file);
        // get validation error
        DataBinder binder = new DataBinder(requestDocument);
        binder.setValidator(validator);
        binder.validate();
        BindingResult bindingResult = binder.getBindingResult();
        if (bindingResult.hasErrors()) {
            throw new HttpException(HttpStatus.BAD_REQUEST, bindingResult);
        }

        documentService.updateDocument(requestDocument);

        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @DeleteMapping("/{documentId}")
    public ResponseEntity<Null> deleteDocument(@PathVariable Integer documentId) {

        documentService.deleteFile(documentId);

        return new ResponseEntity<>(null, HttpStatus.OK);
    }

}
