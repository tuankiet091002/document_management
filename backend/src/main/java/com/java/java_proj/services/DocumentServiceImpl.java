package com.java.java_proj.services;

import com.java.java_proj.dto.request.forcreate.CRequestDocument;
import com.java.java_proj.dto.request.forupdate.URequestDocument;
import com.java.java_proj.dto.response.fordetail.DResponseDocument;
import com.java.java_proj.dto.response.fordetail.DResponseDocumentVersion;
import com.java.java_proj.dto.response.forlist.LResponseDocument;
import com.java.java_proj.entities.Document;
import com.java.java_proj.entities.DocumentVersion;
import com.java.java_proj.exceptions.HttpException;
import com.java.java_proj.repositories.DocumentRepository;
import com.java.java_proj.repositories.DocumentVersionRepository;
import com.java.java_proj.services.templates.DocumentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

@Service
public class DocumentServiceImpl implements DocumentService {

    @Autowired
    FirebaseFileService fileService;
    @Autowired
    DocumentRepository documentRepository;
    @Autowired
    DocumentVersionRepository documentVersionRepository;
    @Autowired
    ModelMapper modelMapper;

    @Override
    @Transactional
    public Page<LResponseDocument> getAllDocument(String name, Integer page, Integer size, String orderBy, String orderDirection) {

        // create pageable
        Pageable paging = orderDirection.equals("ASC")
                ? PageRequest.of(page, size, Sort.by(orderBy).ascending())
                : PageRequest.of(page, size, Sort.by(orderBy).descending());

        Page<Document> documentPage = documentRepository.findDocumentBy(name, paging);

        return documentPage.map(entity -> {
            // map to dto
            LResponseDocument document = modelMapper.map(entity, LResponseDocument.class);

            // fetch latest message and set
            DocumentVersion latestVersion = documentVersionRepository.findTopByDocumentOrderByCreatedDateDesc(entity);
            document.setLastVersion(latestVersion.getVersion());
            document.setLastModified(latestVersion.getCreatedDate());

            return document;
        });
    }

    @Override
    @Transactional
    public DResponseDocument getOneDocument(Integer id, String documentVersion) {

        // find entity
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "Document not found"));
        DResponseDocument responseDocument = modelMapper.map(document, DResponseDocument.class);

        DocumentVersion selectedVersion;
        if (Objects.equals(documentVersion, "latest")) {
            selectedVersion = documentVersionRepository.findTopByDocumentOrderByCreatedDateDesc(document);
        } else {
            selectedVersion = documentVersionRepository.findByDocumentAndVersionContaining(document, documentVersion)
                    .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "Can't find document with that version."));
        }

        // set version
        DResponseDocumentVersion selectedResponseVersion = modelMapper.map(selectedVersion, DResponseDocumentVersion.class);

        responseDocument.setDocumentVersion(selectedResponseVersion);
        // set version list
        responseDocument.setVersionList(documentVersionRepository.findVersionList(document));

        return responseDocument;
    }

    @Override
    public void createDocument(CRequestDocument requestDocument) {

        try {

            // save to cloud
            String generatedName = fileService.save(requestDocument.getFile());
            String imageUrl = fileService.getImageUrl(generatedName);

            // create document
            Document document = Document.builder()
                    .name(requestDocument.getName())
                    .description(requestDocument.getDescription())
                    .documentVersions(new ArrayList<>())
                    .createdDate(LocalDateTime.now())
                    .createdBy(null).build();

            // create document
            DocumentVersion firstVersion = DocumentVersion.builder()
                    .version("1.0")
                    .document(document)
                    .content(requestDocument.getContent())
                    .filename(requestDocument.getFile().getOriginalFilename())
                    .generatedName(generatedName)
                    .url(imageUrl)
                    .createdDate(LocalDateTime.now())
                    .createdBy(null).build();

            document.getDocumentVersions().add(firstVersion);

            // save entities
            documentRepository.save(document);

        } catch (IOException e) {
            throw new HttpException(HttpStatus.INTERNAL_SERVER_ERROR, "Can't save file");
        }
    }

    @Override
    @Transactional
    public void updateDocument(URequestDocument requestDocument) {
        try {

            Document document = documentRepository.findById(requestDocument.getId())
                    .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "Document not found."));

            // save to cloud
            String generatedName = fileService.save(requestDocument.getFile());
            String imageUrl = fileService.getImageUrl(generatedName);

            // get appropriate version
            DocumentVersion newestVer = documentVersionRepository.findTopByDocumentOrderByCreatedDateDesc(document);
            String version = "";
            if (requestDocument.getVersion() != null) {
                version = newestVer.updateMainVer(requestDocument.getVersion());
            } else {
                version = newestVer.nextPublish();
            }

            if (requestDocument.getDescription() != null)
                document.setDescription(requestDocument.getDescription());

            // create document
            DocumentVersion nextVersion = DocumentVersion.builder()
                    .document(document)
                    .version(version)
                    .content(requestDocument.getContent())
                    .filename(requestDocument.getFile().getOriginalFilename())
                    .generatedName(generatedName)
                    .url(imageUrl)
                    .createdDate(LocalDateTime.now()).build();
            document.getDocumentVersions().add(nextVersion);

            // save entity
            documentRepository.save(document);
        } catch (IOException e) {
            throw new HttpException(HttpStatus.INTERNAL_SERVER_ERROR, "Can't save file");
        }
    }

    @Override
    @Transactional
    public void deleteFile(Integer id) {

        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "Document not found."));

        document.getDocumentVersions().forEach(ver -> {
            // delete file on each version firebase
            try {
                fileService.delete(ver.getGeneratedName());
            } catch (IOException e) {
                System.out.println("Can't delete file");
            }
        });

        // delete entity
        documentRepository.deleteById(id);
    }
}