package com.docqa.docqa.service.impl;

import com.docqa.docqa.config.OpenAIConfig;
import com.docqa.docqa.dto.openai.MessageDto;
import com.docqa.docqa.dto.openai.OpenAIRequest;
import com.docqa.docqa.dto.openai.OpenAIResponse;
import com.docqa.docqa.exception.FileProcessingException;
import com.docqa.docqa.service.AIService;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Service
public class AIServiceImpl implements AIService {

    private final OpenAIConfig config;
    private final RestTemplate restTemplate;

    public AIServiceImpl(OpenAIConfig openAIConfig){
        this.config = openAIConfig;
        this.restTemplate = new RestTemplate();
    }

    @Override
    public String generateAnswer(String documentContent, String question) {
        String prompt = """
            Based on the following document:
            %s

            Answer the following question:
            %s
    """.formatted(documentContent, question);

        MessageDto systemMessage = MessageDto.builder()
                .role("system")
                .content("You are a helpful assistant that answers questions based strictly on the provided document content.")
                .build();

        MessageDto userMessage = MessageDto.builder()
                .role("user")
                .content(prompt)
                .build();

        OpenAIRequest request = OpenAIRequest.builder()
                .model(config.getModel())
                .messages(Arrays.asList(systemMessage, userMessage))
                .temperature(0.7)
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + config.getApiKey());

        HttpEntity<OpenAIRequest> entity = new HttpEntity<>(request, headers);

        try {
                ResponseEntity<OpenAIResponse> response = restTemplate.postForEntity(
                        config.getApiUrl(),
                        entity,
                        OpenAIResponse.class
            );

            OpenAIResponse openAIResponse = response.getBody();

            if (openAIResponse == null ||
                    openAIResponse.getChoices() == null ||
                    openAIResponse.getChoices().isEmpty()) {
                throw new FileProcessingException("Invalid response from AI service");
            }

            String answer = openAIResponse.getChoices().get(0).getMessage().getContent();

            return answer;

        } catch (Exception e) {
            throw new FileProcessingException("Failed to get AI response: " + e.getMessage());
        }

    }

}
