package com.smxworld.ecommerce.catalog.internal.repository;

import com.smxworld.ecommerce.catalog.internal.model.Product;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductJpaRepository extends JpaRepository<Product, UUID> {}
