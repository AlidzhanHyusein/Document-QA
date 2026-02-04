package com.docqa.docqa.repository;

import com.docqa.docqa.entity.QuestionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface QuestionHistoryRepository extends JpaRepository<QuestionHistory,Long> {

    List<QuestionHistory> findByDocument_Id(Long documentId);

    List<QuestionHistory> findByQuestion(String question);

    List<QuestionHistory> findByAskedAtAfter(LocalDateTime date);

    List<QuestionHistory> findByResponseTimeMsLessThan(Long maxTime);
}
