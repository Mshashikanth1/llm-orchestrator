package com.refy.assignment.service.impl;

import com.refy.assignment.beans.CriteriaProvider;
import com.refy.assignment.beans.DefaultModelProvider;
import com.refy.assignment.beans.DocumentsProvider;
import com.refy.assignment.pojo.Criteria;
import com.refy.assignment.pojo.LlamaResponse;
import com.refy.assignment.pojo.Models;
import com.refy.assignment.service.LlamaAiService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;



import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Service
public class LlamaAiServiceImpl implements LlamaAiService {

    private final ChatClient chatClient;
    private final RestTemplate restTemplate;
    private final DefaultModelProvider defaultModelProvider;
    private final DocumentsProvider documentsProvider;
    private final CriteriaProvider criteriaProvider;


    @Autowired
    public LlamaAiServiceImpl(ChatClient chatClient, RestTemplate restTemplate,DefaultModelProvider defaultModelProvider,
                              DocumentsProvider documentsProvider,
                              CriteriaProvider criteriaProvider) {
        this.chatClient = chatClient;
        this.restTemplate=restTemplate;
        this.defaultModelProvider = defaultModelProvider;
        this.documentsProvider = documentsProvider;
        this.criteriaProvider = criteriaProvider;
    }

    @Override
    public LlamaResponse generateMessage(String promptMessage) {

       final Prompt prompt = new Prompt(
                promptMessage,
                OllamaOptions.create()
                        .withModel(defaultModelProvider.getDefaultModel())
                        .withTemperature(0.4F)
        );

       final ChatResponse response = chatClient.call(prompt);

        return  LlamaResponse.builder()
                .message(response.getResult().getOutput().getContent())
                .build();
    }

    @Override
    public Models getAvailableModels(){
        Models models=  Objects.requireNonNull(restTemplate.getForObject("http://localhost:11434/api/tags", Models.class));
        models.setActiveLLM(defaultModelProvider.getDefaultModel());
        return models;
    }

    @Override
    public  void changeDefaultLLM(String model){
        defaultModelProvider.setDefaultModel(model);
    }

    @Override
    public LlamaResponse getSummaryInWords(int wordCount) {
        final Prompt prompt = new Prompt(
                String.join(" ",
                          "you are a document researcher read the documents attached ",
                                    "give me the summary this text  :{}  ",
                                    documentsProvider.getDocuments().toString(),
                                    " but follow these criteria: {} ",
                                    "give me the summary in  ", String.valueOf(wordCount)," words ",
                                    criteriaProvider.getCriteria().toString()
                        ),
                OllamaOptions.create()
                        .withModel(defaultModelProvider.getDefaultModel())
                        .withTemperature(0.4F)
        );

        final ChatResponse response = chatClient.call(prompt);

        return  LlamaResponse.builder()
                .message(response.getResult().getOutput().getContent())
                .build();
    }

    @Override
    public void readPDFs(List<MultipartFile> files) throws IOException {

        for (MultipartFile file : files) {
            String content = readPDF(file);
            documentsProvider.addDocument(content);
        }
    }

    @Override
    public LlamaResponse setCriteria(Criteria cri) {
        criteriaProvider.setCriteria(cri.getCriteria());
        return LlamaResponse.builder().message("criteria updated successfully").build();
    }

    @Override
    public LlamaResponse resetCriteria(Criteria cri) {
        criteriaProvider.clearCriteria();
        return LlamaResponse.builder().message("criteria updated successfully").build();
    }

    @Override
    public List<String> getCriteria() {
        return criteriaProvider.getCriteria();
    }

    @Override
    public void removeDocuments() {
        documentsProvider.clearDocuments();
    }

    private String readPDF(MultipartFile file) throws IOException {
        try (PDDocument document = PDDocument.load(file.getInputStream())) {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            return pdfStripper.getText(document);
        }
    }
}