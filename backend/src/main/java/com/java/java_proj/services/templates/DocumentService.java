package com.java.java_proj.services.templates;

import com.java.java_proj.dto.request.forcreate.CRequestDocument;
import com.java.java_proj.dto.request.forupdate.URequestDocument;
import com.java.java_proj.dto.response.fordetail.DResponseDocument;
import com.java.java_proj.dto.response.forlist.LResponseDocument;
import org.springframework.data.domain.Page;

public interface DocumentService {

    Page<LResponseDocument> getAllDocument(String name, Integer page, Integer size, String orderBy, String orderDirection);

    DResponseDocument getOneDocument(Integer documentId, String documentVersion);

    void createDocument(CRequestDocument requestDocument);

    void updateDocument(URequestDocument requestDocument);

    void deleteFile(Integer id);

}
