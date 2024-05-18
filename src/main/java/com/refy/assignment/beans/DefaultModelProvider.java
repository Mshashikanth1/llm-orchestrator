package com.refy.assignment.beans;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Setter
@Getter
@Component
@SessionScope
public class DefaultModelProvider {
    private String defaultModel = "llama3";
}
