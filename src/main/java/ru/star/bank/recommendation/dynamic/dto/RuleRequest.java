package ru.star.bank.recommendation.dynamic.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class RuleRequest {
    @NotBlank
    private String productName;
    @NotBlank
    private String productId;
    @NotBlank
    private String productText;
    @NotNull
    private List<QueryDto> rule;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductText() {
        return productText;
    }

    public void setProductText(String productText) {
        this.productText = productText;
    }

    public List<QueryDto> getRule() {
        return rule;
    }

    public void setRule(List<QueryDto> rule) {
        this.rule = rule;
    }

    public static class QueryDto {
        @NotBlank
        private String query;
        @NotNull
        private List<String> arguments;
        private boolean negate;

        public String getQuery() {
            return query;
        }

        public void setQuery(String query) {
            this.query = query;
        }

        public List<String> getArguments() {
            return arguments;
        }

        public void setArguments(List<String> arguments) {
            this.arguments = arguments;
        }

        public boolean isNegate() {
            return negate;
        }

        public void setNegate(boolean negate) {
            this.negate = negate;
        }
    }
}