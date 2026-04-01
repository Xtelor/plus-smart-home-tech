package ru.yandex.practicum.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.yandex.practicum.model.Product;
import ru.yandex.practicum.enums.ProductCategory;

import java.util.List;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    @Query("""
       select p
       from Product p
       where p.productCategory = :category
       """)
    Page<Product> findByProductCategory(ProductCategory category, Pageable pageable);

    List<Product> findAllByProductCategory(ProductCategory category);
}
