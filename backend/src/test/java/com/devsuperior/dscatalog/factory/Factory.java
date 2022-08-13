package com.devsuperior.dscatalog.factory;

import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dto.ProductDTO;

import java.time.Instant;

public class Factory {

    public static Product createProduct() {
        Product product = new Product(1L, "Phone", "Good Phone", 800.0, "https://img.com/img.png", Instant.parse("2020-10-20T03:00:00Z"));
        product.getCategories().add(createCategory());

        return product;
    }

    public static ProductDTO createProductDTO() {
        return new ProductDTO(createProduct(), createProduct().getCategories());
    }

    public static Category createCategory() {
        return new Category(2L, "Eletrônicos");
    }
}
