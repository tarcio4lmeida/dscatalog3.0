package com.devsuperior.dscatalog.services;

import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.factory.Factory;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.exceptions.ResourcesNotFoundException;
import com.devsuperior.dto.ProductDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest //carrrega o contexto
@Transactional // da rollback em cada test
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
    public void findAllPagedShouldReturnPage0Size10() {
        PageRequest pageRequest = PageRequest.of(0,10);
        Page<ProductDTO> result = service.findAllPaged(pageRequest);

        assertFalse(result.isEmpty());
        assertNotNull(result);
        assertEquals(0, result.getNumber());
        assertEquals(10, result.getSize());
        assertEquals(countTotalProducts, result.getTotalElements());
    }

    @Test
    public void findAllPagedShouldReturnEmptyPageWhenPageDoesNotExist() {
        PageRequest pageRequest = PageRequest.of(50,10);
        Page<ProductDTO> result = service.findAllPaged(pageRequest);

        assertTrue(result.isEmpty());
    }

    @Test
    public void findAllPagedShouldReturnSortedPageWhenSortByName() {
        PageRequest pageRequest = PageRequest.of(0,10, Sort.by("name"));
        Page<ProductDTO> result = service.findAllPaged(pageRequest);

        assertFalse(result.isEmpty());
        assertEquals("Macbook Pro", result.getContent().get(0).getName());
        assertEquals("PC Gamer", result.getContent().get(1).getName());
        assertEquals("PC Gamer Alfa", result.getContent().get(2).getName());
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