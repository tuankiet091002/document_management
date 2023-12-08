package com.java.java_proj;

import com.java.java_proj.dto.request.forcreate.CRequestDocument;
import com.java.java_proj.dto.request.forupdate.URequestDocument;
import com.java.java_proj.dto.response.fordetail.DResponseDocument;
import com.java.java_proj.dto.response.forlist.LResponseDocument;
import com.java.java_proj.entities.Document;
import com.java.java_proj.entities.DocumentVersion;
import com.java.java_proj.repositories.DocumentRepository;
import com.java.java_proj.repositories.DocumentVersionRepository;
import com.java.java_proj.services.DocumentServiceImpl;
import com.java.java_proj.services.FirebaseFileService;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

//@RunWith(SpringRunner.class)
//@SpringBootTest
//@TestPropertySource(locations = "classpath:application_test.properties")
@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
public class DocumentServiceTest {

    @InjectMocks
    DocumentServiceImpl documentService;
    @Mock
    FirebaseFileService fileService;
    @Mock
    DocumentVersionRepository documentVersionRepository;
    @Mock
    DocumentRepository documentRepository;
    @Spy
    ModelMapper modelMapper;

    @Before
    public void setFileService() throws IOException {

        Mockito.when(fileService.getImageUrl("1234-filename"))
                .thenReturn("http://file.url");

        Mockito.when(fileService.save(any(MultipartFile.class)))
                .thenReturn("1234-filename");

    }

    @Before
    public void setDocumentVersionRepository() {
        Document document = new Document();
        document.setId(1);

        List<DocumentVersion> documentVersions = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            DocumentVersion documentVersion = new DocumentVersion();
            documentVersion.setDocument(document);
            documentVersion.setVersion(i + 1 + ".0");

            documentVersions.add(documentVersion);
        }

        Mockito.when(documentVersionRepository.findVersionList(any(Document.class)))
                .thenReturn(documentVersions.stream().map(DocumentVersion::getVersion).collect(Collectors.toList()));

        Mockito.when(documentVersionRepository.findByDocumentAndVersionContaining(any(Document.class), eq("1.0")))
                .thenReturn(Optional.of(documentVersions.get(0)));

        Mockito.when(documentVersionRepository.findTopByDocumentOrderByCreatedDateDesc(any(Document.class)))
                .thenReturn(documentVersions.get(2));
    }

    @Before
    public void setDocumentRepository() {

        Pageable pageable = PageRequest.of(0, 10);
        List<Document> documents = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Document document = new Document();
            document.setId(1);
            document.setName("Testing doc");

            documents.add(document);
        }

        Mockito.when(documentRepository.findDocumentBy(eq(""), any(Pageable.class)))
                .thenReturn(new PageImpl<>(documents, pageable, documents.size()));

        Mockito.when(documentRepository.findById(1))
                .thenReturn(Optional.of(documents.get(0)));
    }

    @Test
    public void getAllDocumentTest() {

        Page<LResponseDocument> documentPage = documentService.getAllDocument("", 0, 10, "id", "DESC");

        Assertions.assertEquals(documentPage.getSize(), 10);
        Assertions.assertEquals(documentPage.getTotalPages(), 1);
        Assertions.assertEquals(documentPage.getContent().size(), 3);
        Assertions.assertEquals(documentPage.getContent().get(0).getId(), 1);

        Mockito.verify(documentRepository).findDocumentBy(any(String.class), any(Pageable.class));
    }

    @Test
    public void getOneDocumentTest() {

        DResponseDocument document = documentService.getOneDocument(1, "latest");

        Assertions.assertEquals(document.getId(), 1);
        Assertions.assertEquals(document.getDocumentVersion().getVersion(), "3.0");

        Mockito.verify(documentVersionRepository).findTopByDocumentOrderByCreatedDateDesc(any(Document.class));
        Mockito.verify(documentVersionRepository).findVersionList(any(Document.class));
    }

    @Test
    public void createDocumentTest() throws IOException {
        CRequestDocument requestDocument = new CRequestDocument();
        requestDocument.setName("Testing doc");
        requestDocument.setFile(new MockMultipartFile("filename", "filename.data",
                "text/plain", "some other type".getBytes()));

        Assertions.assertDoesNotThrow(() -> documentService.createDocument(requestDocument));

        Mockito.verify(fileService).save(any(MultipartFile.class));
        Mockito.verify(fileService).getImageUrl(eq("1234-filename"));
        Mockito.verify(documentRepository).save(any(Document.class));
    }

    @Test
    public void updateDocumentTest() throws IOException {
        URequestDocument requestDocument = new URequestDocument();
        requestDocument.setId(1);
        requestDocument.setVersion(4);
        requestDocument.setFile(new MockMultipartFile("filename", "filename",
                "text/plain", "some other type".getBytes()));

        Assertions.assertDoesNotThrow(() -> documentService.updateDocument(requestDocument));

        Mockito.verify(documentRepository).findById(1);
        Mockito.verify(fileService).save(any(MultipartFile.class));
        Mockito.verify(fileService).getImageUrl("1234-filename");
        Mockito.verify(documentRepository).save(any(Document.class));
    }

    @Test
    public void deleteFileTest() throws IOException {

        Assertions.assertDoesNotThrow(() -> documentService.deleteFile(1));

        Mockito.verify(documentRepository).findById(1);
        Mockito.verify(documentRepository).deleteById(1);
    }
}
