package com.devsuperior.dscatalog.repositories;

import com.devsuperior.dscatalog.entities.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest // -> Carrega somente os componentes relacionados ao Spring Data JPA.
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository repository;

    private long existingId;
    private long noExistingId;

    @BeforeEach
    void setUp() throws Exception{
         existingId = 1L;
         noExistingId = 1000L;
    }

    @Test
    public void deleteShouldDeleteObjectWhenIdsExists(){
        repository.deleteById(existingId);

        Optional<Product> result = repository.findById(existingId);
        assertFalse(result.isPresent());
    }

    @Test
    public void deleteShouldThrowEmptyResultDataAccessExceptionWhenIdDoesNotExist(){
        assertThrows(EmptyResultDataAccessException. class, () ->{
            repository.deleteById(noExistingId);
        });
    }
}