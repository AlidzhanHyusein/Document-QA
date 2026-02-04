package com.docqa.docqa.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionHistoryResponse {

    private Long id;
    private Long documentId;
    private String documentFilename;
    private String question;
    private String answer;
    private LocalDateTime askedAt;
    private Long responseTimeMs;
}
