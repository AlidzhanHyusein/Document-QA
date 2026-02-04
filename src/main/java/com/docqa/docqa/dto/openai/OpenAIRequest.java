package com.docqa.docqa.dto.openai;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class OpenAIRequest {
    private String model;
    private List<MessageDto> messages;
    private Double temperature;


}
