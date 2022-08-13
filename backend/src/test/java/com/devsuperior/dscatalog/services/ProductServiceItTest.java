package com.devsuperior.dscatalog.services;

import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.factory.Factory;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourcesNotFoundException;
import com.devsuperior.dto.ProductDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest //carrrega o contexto
public class ProductServiceItTest {
    @Autowired
    private ProductService service;

    @Autowired
    private ProductRepository repository;

    private long existingId;
    private long noExistingId;
    private long dependentId;

    private long countTotalProducts;
    private PageImpl<Product> page;
    private Product product;
    private ProductDTO productDTO;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        noExistingId = 1000L;
        dependentId = 4L;
        countTotalProducts = 25L;
        product = Factory.createProduct();
        productDTO = Factory.createProductDTO();
        page = new PageImpl<>(List.of(product));
        Category category = Factory.createCategory();

    }

    @Test
    public void deleteShouldDoNothingWhenIdExists() {
        assertDoesNotThrow(() -> {
            service.delete(existingId);
        });
        assertEquals(countTotalProducts-1, repository.count());
    }

    @Test
    public void deleteShouldThrowResourcesNotFoundExceptionWhenIdEDoesntxists() {
        assertThrows(ResourcesNotFoundException.class, () -> {
            service.delete(noExistingId);
        });

    }

    @Test
    public void findAllPagedShouldReturnPage() {
        Page<ProductDTO> result = service.findAllPaged((Pageable) any());

        assertNotNull(result);

        verify(repository, times(1)).findAll((Pageable) any());
    }

    @Test
    public void findByIdShouldReturnAProductDTOWhenIdExists() {
        ProductDTO productDTO = service.findbyId(existingId);

        assertNotNull(productDTO);

        verify(repository, times(1)).findById(existingId);
    }

    @Test
    public void findByIdShouldReturnResourcesNotFoundExceptionWhenIdDoesntExists() {
        assertThrows(ResourcesNotFoundException.class, () -> {
            service.findbyId(noExistingId);
        });


        verify(repository, times(1)).findById(noExistingId);
    }

    @Test
    public void updateShouldReturnAProductDTOWhenIdExists() {
        productDTO = service.update(existingId, productDTO);

        assertNotNull(productDTO);

        verify(repository, times(1)).save(product);
    }

    @Test
    public void updateShouldReturnREntityNotFoundExceptionWhenIdDoesntExists() {
        assertThrows(ResourcesNotFoundException.class, () -> {
           service.update(noExistingId, productDTO);
        });

        verify(repository, times(1)).getOne(noExistingId);
    }
}