package com.refy.assignment.beans;

import lombok.Getter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.util.ArrayList;
import java.util.List;

@Getter
@Component
@SessionScope
public class DocumentsProvider {
    private final List<String> documents = new ArrayList<>();

    public void addDocument(String document) {
        documents.add(document);
    }
    public void clearDocuments() {
        documents.clear();
    }
}
