package com.docqa.docqa.repository;

import com.docqa.docqa.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DocumentRepository extends JpaRepository<Document,Long> {
    Optional<Document> findByFilename(String filename);
    boolean existsByFilename(String filename);
}
