package com.docqa.docqa.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "question_history")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class QuestionHistory {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String question;

    @Column(columnDefinition = "TEXT")
    private String answer;

    @CreationTimestamp
    private LocalDateTime askedAt;

    private Long responseTimeMs;

    @ManyToOne
    @JoinColumn(name = "document_id",nullable = false)
    @JsonBackReference
    private Document document;
}
