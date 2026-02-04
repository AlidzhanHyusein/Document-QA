package com.docqa.docqa.dto.response;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DocumentUploadResponse {

    private Long documentId;
    private String filename;
    private String fileType;
    private Long fileSizeBytes;
    private LocalDateTime uploadedAt;
    private String message;
}
