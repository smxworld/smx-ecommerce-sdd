package com.smxworld.ecommerce.catalog;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.smxworld.ecommerce.catalog.internal.model.Product;
import com.smxworld.ecommerce.catalog.internal.model.ProductDocument;
import com.smxworld.ecommerce.catalog.internal.repository.ProductElasticsearchRepository;
import com.smxworld.ecommerce.review.ReviewCreatedEvent;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.StringQuery;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.modulith.test.PublishedEvents;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

@ApplicationModuleTest
@ActiveProfiles("test")
class CatalogModuleTest {

    /**
     * Configurazione di test che fornisce: - SyncTaskExecutor: rende @Async (usato da @ApplicationModuleListener)
     * sincrono e deterministico nei test, senza dover ricorrere a sleep o CountDownLatch. - Mock ES via Mockito.mock()
     * (proxy JDK) invece di @MockBean: su Java 25 byte-buddy fallisce nello strumentare l'intera gerarchia di
     * ElasticsearchOperations. Le auto-configurazioni ES sono escluse in application-test.yml.
     */
    @TestConfiguration
    static class TestConfig {

        @Bean
        @Primary
        Executor taskExecutor() {
            return new SyncTaskExecutor();
        }

        @Bean
        TransactionTemplate transactionTemplate(PlatformTransactionManager txManager) {
            return new TransactionTemplate(txManager);
        }

        @Bean
        IndexOperations productIndexOperations() {
            IndexOperations indexOperations = mock(IndexOperations.class);
            when(indexOperations.exists()).thenReturn(true);
            return indexOperations;
        }

        @Bean
        ElasticsearchOperations elasticsearchOperations(IndexOperations productIndexOperations) {
            ElasticsearchOperations operations = mock(ElasticsearchOperations.class);
            when(operations.indexOps(ProductDocument.class)).thenReturn(productIndexOperations);
            return operations;
        }

        @Bean
        ProductElasticsearchRepository productElasticsearchRepository() {
            var repo = mock(ProductElasticsearchRepository.class);
            // findById è chiamato da syncRatingToEs/syncScoreToEs:
            // ritornare Optional.empty() fa sì che ifPresent() sia no-op.
            when(repo.findById(any())).thenReturn(Optional.empty());
            when(repo.saveAll(any(Iterable.class))).thenAnswer(invocation -> invocation.getArgument(0));
            return repo;
        }
    }

    @Autowired
    CatalogApi catalogApi;

    @Autowired
    ApplicationEventPublisher publisher;

    @Autowired
    TransactionTemplate txTemplate;

    @Autowired
    ElasticsearchOperations elasticsearchOperations;

    @Autowired
    IndexOperations productIndexOperations;

    @Autowired
    ProductElasticsearchRepository productElasticsearchRepository;

    @PersistenceContext
    EntityManager em;

    @BeforeEach
    void setup() {
        txTemplate.execute(s -> {
            em.createQuery("DELETE FROM Product").executeUpdate();
            return null;
        });

        // Reset stub per evitare interferenze tra test.
        // doReturn bypassa la verifica dei tipi generici a compile-time, necessaria
        // perché Mockito non riesce a inferire <T> di SearchHits<T> in presenza
        // di overload ambigui su ElasticsearchOperations.search().
        reset(elasticsearchOperations, productIndexOperations, productElasticsearchRepository);
        when(elasticsearchOperations.indexOps(ProductDocument.class)).thenReturn(productIndexOperations);
        when(productIndexOperations.exists()).thenReturn(true);
        when(productElasticsearchRepository.findById(any())).thenReturn(Optional.empty());
        when(productElasticsearchRepository.saveAll(any(Iterable.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        doReturn(emptySearchHits())
                .when(elasticsearchOperations)
                .search(any(CriteriaQuery.class), eq(ProductDocument.class));
        doReturn(emptySearchHits())
                .when(elasticsearchOperations)
                .search(any(StringQuery.class), eq(ProductDocument.class));
    }

    // ─── getProduct ───────────────────────────────────────────────────────────

    @Test
    void shouldThrowWhenProductNotFound() {
        assertThatThrownBy(() -> catalogApi.getProduct(UUID.randomUUID()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Product not found");
    }

    @Test
    void shouldReturnProductDetailsWhenProductExists() {
        UUID productId = saveProduct("Laptop Pro", "High-end laptop", "1299.99", "Electronics");

        ProductDetails result = catalogApi.getProduct(productId);

        assertThat(result.id()).isEqualTo(productId);
        assertThat(result.name()).isEqualTo("Laptop Pro");
        assertThat(result.category()).isEqualTo("Electronics");
        assertThat(result.price()).isEqualByComparingTo("1299.99");
        assertThat(result.averageRating()).isZero();
        assertThat(result.searchScore()).isZero();
    }

    // ─── search ───────────────────────────────────────────────────────────────

    @Test
    void shouldPublishSearchPerformedEventWhenSearchIsExecuted(PublishedEvents events) {
        catalogApi.search(new SearchQuery("laptop", null, null, null, 0, 10));

        assertThat(events.ofType(SearchPerformedEvent.class))
                .hasSize(1)
                .first()
                .extracting(SearchPerformedEvent::query)
                .isEqualTo("laptop");
    }

    @Test
    void shouldReturnEmptyResultsWhenNoProductsMatchSearchQuery(PublishedEvents events) {
        SearchResult result = catalogApi.search(new SearchQuery(null, null, null, null, 0, 10));

        assertThat(result.totalElements()).isZero();
        assertThat(result.items()).isEmpty();
        assertThat(events.ofType(SearchPerformedEvent.class)).hasSize(1);
    }

    @Test
    void shouldSearchWithMatchAllWhenNoFilterIsGiven() {
        catalogApi.search(new SearchQuery(null, null, null, null, 2, 5));

        StringQuery query = captureStringQuery();
        assertThat(query.getSource()).isEqualTo("{\"match_all\":{}}");
        assertThat(query.getPageable().getPageNumber()).isEqualTo(2);
        assertThat(query.getPageable().getPageSize()).isEqualTo(5);
    }

    @Test
    void shouldSearchTextOnNameOrDescription() {
        catalogApi.search(new SearchQuery("laptop", null, null, null, 0, 10));

        CriteriaQuery query = captureCriteriaQuery();
        assertThat(describe(query.getCriteria()))
                .isEqualTo("description[MATCHES=[laptop]]{}<name[MATCHES=[laptop]]{}<>>|or");
        assertThat(query.getPageable().getPageSize()).isEqualTo(10);
    }

    @Test
    void shouldCombineTextCategoryAndPriceRange() {
        catalogApi.search(
                new SearchQuery("laptop", "Electronics", new BigDecimal("100"), new BigDecimal("2000"), 1, 20));

        CriteriaQuery query = captureCriteriaQuery();
        assertThat(describe(query.getCriteria()))
                .isEqualTo("description[MATCHES=[laptop]]{}<name[MATCHES=[laptop]]{}<> "
                        + "category[EQUALS=[Electronics]]{}<> price[BETWEEN=[[100, 2000]]]{}<>>|or");
        assertThat(query.getPageable().getPageNumber()).isEqualTo(1);
        assertThat(query.getPageable().getPageSize()).isEqualTo(20);
    }

    @Test
    void shouldFilterByMinPriceOnly() {
        catalogApi.search(new SearchQuery(null, null, new BigDecimal("100"), null, 0, 10));

        assertThat(describe(captureCriteriaQuery().getCriteria())).isEqualTo("-[]{}<price[GREATER_EQUAL=[100]]{}<>>");
    }

    @Test
    void shouldFilterByMaxPriceOnly() {
        catalogApi.search(new SearchQuery(null, "Books", null, new BigDecimal("50"), 0, 10));

        assertThat(describe(captureCriteriaQuery().getCriteria()))
                .isEqualTo("-[]{}<category[EQUALS=[Books]]{}<> price[LESS_EQUAL=[50]]{}<>>");
    }

    /** Flattens a criteria tree into a readable string, since {@code Criteria.toString()} only shows its head. */
    private static String describe(Criteria criteria) {
        var criteriaField = criteria.getField();
        String field = criteriaField == null ? "-" : criteriaField.getName();
        String entries = criteria.getQueryCriteriaEntries().stream()
                .map(e -> e.getKey() + "=" + Arrays.deepToString(new Object[] {e.getValue()}))
                .collect(Collectors.joining(","));
        String subs = criteria.getSubCriteria().stream()
                .map(CatalogModuleTest::describe)
                .collect(Collectors.joining(" "));
        String chain = criteria.getCriteriaChain().stream()
                .filter(c -> !c.equals(criteria))
                .map(CatalogModuleTest::describe)
                .collect(Collectors.joining(" "));
        return field + "[" + entries + "]{" + subs + "}<" + chain + ">" + (criteria.isOr() ? "|or" : "");
    }

    private CriteriaQuery captureCriteriaQuery() {
        ArgumentCaptor<CriteriaQuery> captor = ArgumentCaptor.forClass(CriteriaQuery.class);
        verify(elasticsearchOperations).search(captor.capture(), eq(ProductDocument.class));
        return captor.getValue();
    }

    private StringQuery captureStringQuery() {
        ArgumentCaptor<StringQuery> captor = ArgumentCaptor.forClass(StringQuery.class);
        verify(elasticsearchOperations).search(captor.capture(), eq(ProductDocument.class));
        return captor.getValue();
    }

    // ─── updateProductScore ───────────────────────────────────────────────────

    @Test
    void shouldUpdateSearchScoreWhenUpdateProductScoreIsCalled() {
        UUID productId = saveProduct("Headphones", "Wireless headphones", "299.99", "Audio");

        catalogApi.updateProductScore(productId, 8.5);

        assertThat(catalogApi.getProduct(productId).searchScore()).isEqualTo(8.5);
    }

    // ─── ReviewCreatedEvent listener ─────────────────────────────────────────

    @Test
    void shouldUpdateAverageRatingWhenReviewCreatedEventReceived() {
        UUID productId = saveProduct("Camera", "DSLR camera", "699.99", "Photography");

        // @ApplicationModuleListener = @TransactionalEventListener(AFTER_COMMIT) + @Async.
        // L'evento va pubblicato dentro una transazione che committa (altrimenti il listener
        // non scatta). Con SyncTaskExecutor, @Async gira nel thread corrente → determinismo.
        txTemplate.execute(s -> {
            publisher.publishEvent(new ReviewCreatedEvent(productId, 5));
            return null;
        });

        assertThat(catalogApi.getProduct(productId).averageRating()).isEqualTo(5.0);
    }

    @Test
    void shouldComputeIncrementalAverageOverMultipleReviews() {
        UUID productId = saveProduct("Speaker", "Bluetooth speaker", "149.99", "Audio");

        // Prima recensione 4, seconda 2 → media mobile attesa = (4 + 2) / 2 = 3.0
        txTemplate.execute(s -> {
            publisher.publishEvent(new ReviewCreatedEvent(productId, 4));
            return null;
        });
        txTemplate.execute(s -> {
            publisher.publishEvent(new ReviewCreatedEvent(productId, 2));
            return null;
        });

        assertThat(catalogApi.getProduct(productId).averageRating()).isEqualTo(3.0);
    }

    // ─── Helpers ──────────────────────────────────────────────────────────────

    /** Persiste un prodotto in una transazione committata e ne ritorna l'id. */
    private UUID saveProduct(String name, String description, String price, String category) {
        return txTemplate.execute(s -> {
            var p = new Product(name, description, new BigDecimal(price), category);
            em.persist(p);
            em.flush();
            return p.getId();
        });
    }

    /** Ritorna un mock di SearchHits&lt;ProductDocument&gt; con zero risultati. */
    @SuppressWarnings("unchecked")
    private static SearchHits<ProductDocument> emptySearchHits() {
        SearchHits<ProductDocument> hits = mock(SearchHits.class);
        when(hits.getTotalHits()).thenReturn(0L);
        when(hits.stream()).thenReturn(List.<SearchHit<ProductDocument>>of().stream());
        return hits;
    }
}
