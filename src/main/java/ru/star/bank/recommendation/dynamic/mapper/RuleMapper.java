package ru.star.bank.recommendation.dynamic.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import ru.star.bank.recommendation.dynamic.dto.RuleRequest;
import ru.star.bank.recommendation.dynamic.dto.RuleResponse;
import ru.star.bank.recommendation.dynamic.entity.DynamicRule;
import ru.star.bank.recommendation.dynamic.entity.RuleQuery;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RuleMapper {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public DynamicRule toEntity(RuleRequest request) {
        DynamicRule rule = new DynamicRule(request.getProductName(), request.getProductId(), request.getProductText());
        for (RuleRequest.QueryDto q : request.getRule()) {
            RuleQuery query = new RuleQuery();
            query.setQueryType(q.getQuery());
            try {
                query.setArguments(objectMapper.writeValueAsString(q.getArguments()));
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Failed to serialize arguments", e);
            }
            query.setNegate(q.isNegate());
            rule.addQuery(query);
        }
        return rule;
    }

    public RuleResponse toResponse(DynamicRule rule) {
        RuleResponse response = new RuleResponse();
        response.setId(rule.getId());
        response.setProductName(rule.getProductName());
        response.setProductId(rule.getProductId());
        response.setProductText(rule.getProductText());
        List<RuleResponse.QueryDto> queryDtos = rule.getQueries().stream()
                .map(q -> {
                    RuleResponse.QueryDto dto = new RuleResponse.QueryDto();
                    dto.setQuery(q.getQueryType());
                    try {
                        List<String> args = objectMapper.readValue(q.getArguments(), new TypeReference<List<String>>() {});
                        dto.setArguments(args);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException("Failed to deserialize arguments", e);
                    }
                    dto.setNegate(q.isNegate());
                    return dto;
                })
                .collect(Collectors.toList());
        response.setRule(queryDtos);
        return response;
    }
}