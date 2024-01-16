package com.devsuperior.dscatalog.repositories;

import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.factory.Factory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest // -> Carrega somente os componentes relacionados ao Spring Data JPA.
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository repository;

    private long existingId;
    private long noExistingId;
    private long countTotalProducts;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        noExistingId = 1000L;
        countTotalProducts = 25L;
    }

    @Test
    public void saveShouldPersistObjectWhenIdsNull() {
        Product product = Factory.createProduct();
        product.setId(null);

        product = repository.save(product);
        assertNotNull(product.getId());
        assertEquals(countTotalProducts+1, product.getId());
    }

    @Test
    public void deleteShouldDeleteObjectWhenIdsExists() {
        repository.deleteById(existingId);

        Optional<Product> result = repository.findById(existingId);
        assertFalse(result.isPresent());
    }

    @Test
    public void deleteShouldThrowEmptyResultDataAccessExceptionWhenIdDoesNotExist() {
        assertThrows(EmptyResultDataAccessException.class, () -> {
            repository.deleteById(noExistingId);
        });
    }

    @Test
    public void findByIdShouldRetrieveObjectWhenIdsExists() {
        Optional<Product> result = repository.findById(existingId);
        assertTrue(result.isPresent());
    }

    @Test
    public void findByIdShouldNotRetrieveObjectWhenIdDoesntExists() {

        Optional<Product> result = repository.findById(noExistingId);
        assertFalse(result.isPresent());
    }
}