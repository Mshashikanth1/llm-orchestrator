package com.refy.assignment.controllers;

import com.refy.assignment.exception.LLMOrchestrationServiceException;
import com.refy.assignment.pojo.Criteria;
import com.refy.assignment.pojo.LlamaResponse;
import com.refy.assignment.pojo.Models;
import com.refy.assignment.service.LlamaAiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api/v1/ai")
public class LlamaRestController {

    private final LlamaAiService llamaAiService;

    @Autowired
    public LlamaRestController(LlamaAiService llamaAiService) {
        this.llamaAiService = llamaAiService;
    }

    @GetMapping("/list-models")
    public ResponseEntity<Models> getAvailableLLMs(){
        return ResponseEntity.status(HttpStatus.OK)
                .body(llamaAiService.getAvailableModels());
    }

    @PostMapping("/select/{llm}")
    public ResponseEntity<ResponseEntity<Models>> selectLLM(@PathVariable String llm){
        try {
            llamaAiService.changeDefaultLLM(llm);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(getAvailableLLMs());
        }catch (Exception ex){
            throw new LLMOrchestrationServiceException("Failed to change the LLM");
        }
    }

    @PostMapping("/upload")
    ResponseEntity<LlamaResponse> uploadFiles(@RequestParam("files") List<MultipartFile> files) {
        try {
            llamaAiService.readPDFs(files);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(LlamaResponse.builder().message("files uploaded successfully").build());
        }catch (Exception ex){
            throw new LLMOrchestrationServiceException("Failed to read the files");
        }

    }

    @PostMapping("/removeFiles")
    ResponseEntity<LlamaResponse> deleteAllFilesFromContext() {
        try {
            llamaAiService.removeDocuments();
            return ResponseEntity.status(HttpStatus.OK)
                    .body(LlamaResponse.builder().message("files removed successfully").build());
        }catch (Exception ex){
            throw new LLMOrchestrationServiceException("Failed to remove the files");
        }

    }

    @GetMapping("/summary/{wordCount}")
    ResponseEntity<LlamaResponse> getSummary(@PathVariable int wordCount){
        return ResponseEntity.status(HttpStatus.OK)
                .body(llamaAiService.getSummaryInWords(wordCount));
    }


    @GetMapping("/get-criteria")
    ResponseEntity<List<String>> getCriteria(){
        return ResponseEntity.status(HttpStatus.OK)
                .body(llamaAiService.getCriteria());
    }


    @PostMapping("/set-criteria")
    ResponseEntity<LlamaResponse> setCriteria(@RequestBody Criteria criteria){

         return ResponseEntity.status(HttpStatus.OK)
                 .body(llamaAiService.setCriteria(criteria));
    }

    @PostMapping("/reset-criteria")
    ResponseEntity<LlamaResponse> resetCriteria(@RequestBody Criteria criteria){
        return ResponseEntity.status(HttpStatus.OK)
                .body(llamaAiService.resetCriteria(criteria));
    }


    @GetMapping("/generate")
    public ResponseEntity<LlamaResponse> generate(
            @RequestParam(value = "promptMessage", defaultValue = "Why is the sky blue?")
            String promptMessage) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(llamaAiService.generateMessage(promptMessage));
    }
}