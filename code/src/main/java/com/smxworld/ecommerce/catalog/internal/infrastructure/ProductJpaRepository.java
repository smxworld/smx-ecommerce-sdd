package com.smxworld.ecommerce.catalog.internal.infrastructure;

import com.smxworld.ecommerce.catalog.internal.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductJpaRepository extends JpaRepository<Product, UUID> {}
