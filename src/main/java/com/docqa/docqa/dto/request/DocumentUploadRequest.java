package com.docqa.docqa.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.aspectj.weaver.ast.Not;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentUploadRequest {

    @NotBlank(message = "\"Filename cannot be blank\"")
    private String filename;

    @NotBlank(message = "\"File type cannot be blank\"")
    @Pattern(regexp = "PDF|TXT",message = "File type must be PDF or TXT")
    private String fileType;

    @NotNull(message = "File size cannot be null")
    private Long fileSizeBytes;

    @NotBlank(message = "Content cannot be blank")
    private String content;
}
