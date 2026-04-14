package com.smxworld.ecommerce.catalog.internal.application;

import com.smxworld.ecommerce.catalog.internal.domain.Product;
import com.smxworld.ecommerce.catalog.internal.infrastructure.ProductDocument;
import com.smxworld.ecommerce.catalog.internal.infrastructure.ProductElasticsearchRepository;
import com.smxworld.ecommerce.catalog.internal.infrastructure.ProductJpaRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CatalogIndexInitializerTest {

    @Test
    void shouldCreateIndexAndPopulateDocumentsWhenIndexIsMissing() {
        ProductJpaRepository productRepo = mock(ProductJpaRepository.class);
        ProductElasticsearchRepository esRepo = mock(ProductElasticsearchRepository.class);
        ElasticsearchOperations esOps = mock(ElasticsearchOperations.class);
        IndexOperations indexOps = mock(IndexOperations.class);
        Document mapping = Document.create();

        Product smartphone = product(
                UUID.fromString("aaaaaaaa-0001-0000-0000-000000000001"),
                "Smartphone XPro",
                "Flagship smartphone",
                "699.00",
                "Elettronica",
                4.5,
                0.9);

        when(esOps.indexOps(ProductDocument.class)).thenReturn(indexOps);
        when(indexOps.exists()).thenReturn(false);
        when(indexOps.createMapping()).thenReturn(mapping);
        when(productRepo.findAll()).thenReturn(List.of(smartphone));
        when(esRepo.saveAll(any(Iterable.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CatalogIndexInitializer initializer = new CatalogIndexInitializer(productRepo, esRepo, esOps);
        initializer.initializeIndex();

        verify(indexOps).create();
        verify(indexOps).putMapping(mapping);

        @SuppressWarnings("unchecked")
        ArgumentCaptor<Iterable<ProductDocument>> documentsCaptor = ArgumentCaptor.forClass((Class) Iterable.class);
        verify(esRepo).saveAll(documentsCaptor.capture());

        List<ProductDocument> documents = StreamSupport.stream(documentsCaptor.getValue().spliterator(), false).toList();
        assertThat(documents)
                .singleElement()
                .satisfies(document -> {
                    assertThat(document.getId()).isEqualTo("aaaaaaaa-0001-0000-0000-000000000001");
                    assertThat(document.getName()).isEqualTo("Smartphone XPro");
                    assertThat(document.getCategory()).isEqualTo("Elettronica");
                    assertThat(document.getAverageRating()).isEqualTo(4.5);
                    assertThat(document.getSearchScore()).isEqualTo(0.9);
                });
    }

    @Test
    void shouldReuseExistingIndexWhenAlreadyPresent() {
        ProductJpaRepository productRepo = mock(ProductJpaRepository.class);
        ProductElasticsearchRepository esRepo = mock(ProductElasticsearchRepository.class);
        ElasticsearchOperations esOps = mock(ElasticsearchOperations.class);
        IndexOperations indexOps = mock(IndexOperations.class);

        when(esOps.indexOps(ProductDocument.class)).thenReturn(indexOps);
        when(indexOps.exists()).thenReturn(true);
        when(productRepo.findAll()).thenReturn(List.of(product(
                UUID.fromString("aaaaaaaa-0002-0000-0000-000000000002"),
                "Laptop UltraSlim",
                "Lightweight laptop",
                "1299.00",
                "Elettronica",
                4.2,
                0.8)));
        when(esRepo.saveAll(any(Iterable.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CatalogIndexInitializer initializer = new CatalogIndexInitializer(productRepo, esRepo, esOps);
        initializer.initializeIndex();

        verify(indexOps, never()).create();
        verify(indexOps, never()).putMapping(any(Document.class));
        verify(esRepo).saveAll(any(Iterable.class));
    }

    private static Product product(UUID id,
                                   String name,
                                   String description,
                                   String price,
                                   String category,
                                   double averageRating,
                                   double searchScore) {
        Product product = new Product(name, description, new BigDecimal(price), category);
        ReflectionTestUtils.setField(product, "id", id);
        ReflectionTestUtils.setField(product, "averageRating", averageRating);
        ReflectionTestUtils.setField(product, "searchScore", searchScore);
        return product;
    }
}
