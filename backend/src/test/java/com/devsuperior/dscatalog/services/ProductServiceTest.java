package com.devsuperior.dscatalog.services;

import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.factory.Factory;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourcesNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatcher;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class ProductServiceTest {
    @InjectMocks
    private ProductService service;
    @Mock
    private ProductRepository repository;
    private long existingId;
    private long noExistingId;
    private long dependentId;
    private PageImpl<Product> page;
    private Product product;
    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        noExistingId = 1000L;
        dependentId = 4L;
        product = Factory.createProduct();
        page = new PageImpl<>(List.of(product));

        when(repository.findById(existingId)).thenReturn(Optional.ofNullable(product));
        when(repository.findById(noExistingId)).thenReturn(Optional.empty());
        when(repository.save(any())).thenReturn(product);
        when(repository.findAll((Pageable) any())).thenReturn(page);
        doNothing().when(repository).deleteById(existingId);
        doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(noExistingId);
        doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);
    }

    @Test
    public void deleteShouldDoNothingWhenIdExists() {
        assertDoesNotThrow(() -> {
            service.delete(existingId);
        });

        verify(repository, times(1)).deleteById(existingId);
    }

    @Test
    public void deleteShouldThrowResourcesNotFoundExceptionWhenIdEDoesntxists() {
        assertThrows(ResourcesNotFoundException.class, () -> {
            service.delete(noExistingId);
        });

        verify(repository, times(1)).deleteById(noExistingId);
    }

    @Test
    public void deleteShouldThrowDatabaseExceptionWhenIntegrityViolation() {
        assertThrows(DatabaseException.class, () -> {
            service.delete(dependentId);
        });

        verify(repository, times(1)).deleteById(dependentId);
    }
}