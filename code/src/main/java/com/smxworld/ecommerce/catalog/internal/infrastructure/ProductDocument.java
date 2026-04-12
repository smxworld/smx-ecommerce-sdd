package com.smxworld.ecommerce.catalog.internal.infrastructure;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.math.BigDecimal;

/**
 * Documento Elasticsearch per l'indice "products".
 * Non contiene lo stock: la disponibilità viene letta da WarehouseApi on-demand.
 */
@Document(indexName = "products", createIndex = false)
@Setting(shards = 1, replicas = 0)
public class ProductDocument {

    @Id
    private String id;

    @MultiField(
        mainField  = @Field(type = FieldType.Text,    analyzer = "standard"),
        otherFields = @InnerField(suffix = "keyword", type = FieldType.Keyword)
    )
    private String name;

    @Field(type = FieldType.Text, analyzer = "standard")
    private String description;

    @Field(type = FieldType.Double)
    private BigDecimal price;

    @Field(type = FieldType.Keyword)
    private String category;

    @Field(type = FieldType.Double)
    private double averageRating;

    @Field(type = FieldType.Double)
    private double searchScore;

    public ProductDocument() {}

    public ProductDocument(String id, String name, String description,
                           BigDecimal price, String category,
                           double averageRating, double searchScore) {
        this.id            = id;
        this.name          = name;
        this.description   = description;
        this.price         = price;
        this.category      = category;
        this.averageRating = averageRating;
        this.searchScore   = searchScore;
    }

    public String getId()              { return id; }
    public void   setId(String id)     { this.id = id; }

    public String getName()            { return name; }
    public void   setName(String name) { this.name = name; }

    public String getDescription()                    { return description; }
    public void   setDescription(String description)  { this.description = description; }

    public BigDecimal getPrice()              { return price; }
    public void       setPrice(BigDecimal p)  { this.price = p; }

    public String getCategory()               { return category; }
    public void   setCategory(String c)       { this.category = c; }

    public double getAverageRating()              { return averageRating; }
    public void   setAverageRating(double rating) { this.averageRating = rating; }

    public double getSearchScore()             { return searchScore; }
    public void   setSearchScore(double score) { this.searchScore = score; }
}
