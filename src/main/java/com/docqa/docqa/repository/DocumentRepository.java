package com.docqa.docqa.repository;

import com.docqa.docqa.entity.Document;
import com.docqa.docqa.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface DocumentRepository extends JpaRepository<Document,Long> {
    Optional<Document> findByFilename(String filename);
    boolean existsByFilename(String filename);

    boolean existsByFilenameAndUser(String filename, User user);

    List<Document> findByUser(User user);
}
