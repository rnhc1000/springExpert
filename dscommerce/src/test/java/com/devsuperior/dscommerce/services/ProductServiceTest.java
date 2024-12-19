package com.devsuperior.dscommerce.services;

import com.devsuperior.dscommerce.dto.ProductDTO;
import com.devsuperior.dscommerce.dto.ProductMinDTO;
import com.devsuperior.dscommerce.entities.Product;
import com.devsuperior.dscommerce.repositories.ProductRepository;
import com.devsuperior.dscommerce.services.exceptions.DatabaseException;
import com.devsuperior.dscommerce.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dscommerce.tests.ProductFactory;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
public class ProductServiceTest {
  @InjectMocks
  private ProductService productService;

  @Mock
  private ProductRepository productRepository;

  private Long existingProductId, nonExistingProductId, dependentProductId;
  private String productName;
  private Product product;
  private ProductDTO productDTO;

  private PageImpl<Product> page;

  @BeforeEach
  void setup() throws Exception {

    existingProductId = 1L;
    nonExistingProductId = 2L;
    dependentProductId = 3L;
    productName = "PlayStation 5";
    product = ProductFactory.createProduct(productName);
    page = new PageImpl<>(List.of(product));
    productDTO = new ProductDTO((product));

    Mockito.when(productRepository.findById(existingProductId)).thenReturn(Optional.of(product));
    Mockito.when(productRepository.findById(nonExistingProductId)).thenReturn(Optional.empty());
    Mockito.when(productRepository.searchByName(any(), (Pageable)any())).thenReturn(page);
    Mockito.when(productRepository.save(any())).thenReturn(product);
    Mockito.when(productRepository.getReferenceById(existingProductId)).thenReturn((product));
    Mockito.when(productRepository.getReferenceById(nonExistingProductId)).thenThrow(EntityNotFoundException.class);

    Mockito.when(productRepository.existsById((nonExistingProductId))).thenReturn(false);
    Mockito.when(productRepository.existsById((existingProductId))).thenReturn(true);
    Mockito.when(productRepository.existsById((dependentProductId))).thenReturn(true);
    Mockito.doNothing().when(productRepository).deleteById(existingProductId);
    Mockito.doThrow(DataIntegrityViolationException.class).when(productRepository).deleteById(dependentProductId);
  }

  @Test
  void findByIdShouldReturnProductDTOWhenIdExists() {

    ProductDTO result = productService.findById(existingProductId);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(result.getId(), existingProductId);
    Assertions.assertEquals(result.getName(), product.getName());

  }

  @Test
  void findByIdShouldReturnResourceNotFoundExceptionWhenIdDoesNotExist() {

    Assertions.assertThrows(ResourceNotFoundException.class, () -> {
      productService.findById(nonExistingProductId);
    });

  }

  @Test
  void findAllShouldReturnPagedProductMinDTO() {

    Pageable pageable = PageRequest.of(0, 12);
    Page<ProductMinDTO> result = productService.findAll(productName, pageable);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(1, result.getSize());
    Assertions.assertEquals(result.iterator().next().getName(), productName);

  }

  @Test
  void insertShouldReturnProductDTO() {

    ProductDTO result = productService.insert(productDTO);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(result.getId(), product.getId());

  }

  @Test
  void updateShouldReturnProductDTOWhenProductIdIsValid() {

    ProductDTO result = productService.update(existingProductId, productDTO);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(result.getId(), product.getId());
    Assertions.assertEquals(result.getName(), productDTO.getName());

  }
  @Test
  void updateShouldThrowResourceNotFoundExceptionWhenProductIdNonExists() {

    Assertions.assertThrows(ResourceNotFoundException.class, () -> {
      productService.update(nonExistingProductId, productDTO);
    });

  }

  @Test
  void deleteShouldDoNothingWhenIdExists() {

    Assertions.assertDoesNotThrow(() -> {
      productService.delete(existingProductId);
    });

  }

  @Test
  void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {

    Assertions.assertThrows(ResourceNotFoundException.class, () -> {
      productService.delete(nonExistingProductId);
    });

  }

  @Test
  void deleteShouldThrowDatabaseExceptionWhenDependentId() {

    Assertions.assertThrows(DatabaseException.class, () -> {
      productService.delete(dependentProductId);
    });

  }
}

