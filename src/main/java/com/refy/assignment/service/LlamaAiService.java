package com.refy.assignment.service;

import com.refy.assignment.pojo.Criteria;
import com.refy.assignment.pojo.LlamaResponse;
import com.refy.assignment.pojo.Models;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface LlamaAiService {
    LlamaResponse generateMessage(String prompt);

    Models getAvailableModels();

    void changeDefaultLLM(String model);

    LlamaResponse getSummaryInWords(int wordCount);

    void readPDFs(List<MultipartFile> files) throws IOException;

    LlamaResponse setCriteria(Criteria criteria);

    LlamaResponse resetCriteria(Criteria criteria);

    List<String> getCriteria();

    void removeDocuments();
}