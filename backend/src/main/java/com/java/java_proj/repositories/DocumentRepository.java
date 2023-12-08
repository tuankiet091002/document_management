package com.java.java_proj.repositories;

import com.java.java_proj.dto.response.forlist.LResponseDocument;
import com.java.java_proj.entities.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.print.Doc;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Integer> {

    @Query(value = "SELECT d FROM Document d JOIN FETCH d.documentVersions WHERE d.name LIKE CONCAT('%',:name, '%')"
    ,countQuery = "SELECT count(*) FROM Document d WHERE d.name LIKE CONCAT('%',:name, '%')")
    Page<Document> findDocumentBy(String name, Pageable pageable);

}
