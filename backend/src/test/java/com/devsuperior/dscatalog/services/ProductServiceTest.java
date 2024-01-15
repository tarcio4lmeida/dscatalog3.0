package com.devsuperior.dscatalog.services;

import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.factory.Factory;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourcesNotFoundException;
import com.devsuperior.dscatalog.dto.ProductDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class) // nao carrega o contexto
public class ProductServiceTest {
    @InjectMocks
    private ProductService service;
    @Mock
    private ProductRepository repository;

    @Mock
    private CategoryRepository categoryRepository;
    private long existingId;
    private long noExistingId;
    private long dependentId;
    private PageImpl<Product> page;
    private Product product;
    private ProductDTO productDTO;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        noExistingId = 1000L;
        dependentId = 4L;
        product = Factory.createProduct();
        productDTO = Factory.createProductDTO();
        page = new PageImpl<>(List.of(product));
        Category category = Factory.createCategory();

        when(categoryRepository.getOne(2L)).thenReturn(category);
        when(repository.findById(existingId)).thenReturn(Optional.ofNullable(product));
        when(repository.findById(noExistingId)).thenReturn(Optional.empty());
        when(repository.getOne(existingId)).thenReturn(product);
        when(repository.save(any())).thenReturn(product);
        when(repository.findAll((Pageable) any())).thenReturn(page);

        doNothing().when(repository).deleteById(existingId);

        doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(noExistingId);
        doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);
        doThrow(ResourcesNotFoundException.class).when(repository).getOne(noExistingId);
        doThrow(EntityNotFoundException.class).when(categoryRepository).getOne(noExistingId);
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