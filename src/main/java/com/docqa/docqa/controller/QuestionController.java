package com.docqa.docqa.controller;

import com.docqa.docqa.dto.request.QuestionRequest;
import com.docqa.docqa.dto.response.QuestionHistoryResponse;
import com.docqa.docqa.dto.response.QuestionResponse;
import com.docqa.docqa.service.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/questions")
@Tag(name = "Question & Answer", description = "AI-powered document Q&A")
public class QuestionController {

    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @PostMapping("/ask")
    @Operation(summary = "Ask a question about a document", description = "Get AI-generated answers based on document content")
    public ResponseEntity<QuestionResponse> askedQuestion(@Valid @RequestBody QuestionRequest request){
        return ResponseEntity.ok(questionService.askQuestion(request));
    }

    @Operation(summary = "Get all document history", description = "Get all document history")
    @GetMapping("/history")
    public ResponseEntity<List<QuestionHistoryResponse>> historyResponses(){
        return ResponseEntity.ok(questionService.getAllHistory());
    }

    @Operation(summary = "Get the document by id", description = "Can get the document by id")
    @GetMapping("/history/{documentId}")
    public ResponseEntity<List<QuestionHistoryResponse>> getDocumentById(@PathVariable Long documentId){

        return ResponseEntity.ok(questionService.getHistoryByDocumentId(documentId));
    }
}
