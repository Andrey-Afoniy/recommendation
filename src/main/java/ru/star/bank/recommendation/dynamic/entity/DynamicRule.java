package ru.star.bank.recommendation.dynamic.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "dynamic_rule")
public class DynamicRule {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "product_id", nullable = false, unique = true)
    private String productId;

    @Column(name = "product_text", nullable = false, length = 2000)
    private String productText;

    @OneToMany(mappedBy = "rule", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<RuleQuery> queries = new ArrayList<>();

    public DynamicRule() {
    }

    public DynamicRule(String productName, String productId, String productText) {
        this.productName = productName;
        this.productId = productId;
        this.productText = productText;
    }

    public void addQuery(RuleQuery query) {
        queries.add(query);
        query.setRule(this);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

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

    public List<RuleQuery> getQueries() {
        return queries;
    }

    public void setQueries(List<RuleQuery> queries) {
        this.queries = queries;
    }
}