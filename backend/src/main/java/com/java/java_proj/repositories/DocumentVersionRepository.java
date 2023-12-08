package com.java.java_proj.repositories;

import com.java.java_proj.entities.Document;
import com.java.java_proj.entities.DocumentVersion;
import com.java.java_proj.entities.enums.DocumentVersionCompositeKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentVersionRepository extends JpaRepository<DocumentVersion, DocumentVersionCompositeKey> {

   DocumentVersion findTopByDocumentOrderByCreatedDateDesc(Document document);

   Optional<DocumentVersion> findByDocumentAndVersionContaining(Document document, String version);

   @Query("SELECT d.version FROM DocumentVersion d WHERE d.document = :document")
   List<String> findVersionList(Document document);
}
