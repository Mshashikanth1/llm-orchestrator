package com.refy.assignment.beans;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Component
@SessionScope
public class CriteriaProvider {
    private List<String> criteria = new ArrayList<>();

    public void clearCriteria() {
        criteria.clear();
    }
}
