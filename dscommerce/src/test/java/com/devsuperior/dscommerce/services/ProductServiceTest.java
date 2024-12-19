package com.devsuperior.dscommerce.services;

import com.devsuperior.dscommerce.dto.ProductDTO;
import com.devsuperior.dscommerce.dto.ProductMinDTO;
import com.devsuperior.dscommerce.entities.Product;
import com.devsuperior.dscommerce.repositories.ProductRepository;
import com.devsuperior.dscommerce.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dscommerce.tests.ProductFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
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

  private Long existingProductId, nonExistingProductId;
  private String productName;
  private Product product;

  private PageImpl<Product> page;

  @BeforeEach
  void setup() throws Exception {

    existingProductId = 1L;
    nonExistingProductId = 2L;
    productName = "PlayStation 5";
    product = ProductFactory.createProduct(productName);
    page = new PageImpl<>(List.of(product));

    Mockito.when(productRepository.findById(existingProductId)).thenReturn(Optional.of(product));
    Mockito.when(productRepository.findById(nonExistingProductId)).thenReturn(Optional.empty());
    Mockito.when(productRepository.searchByName(any(), (Pageable)any())).thenReturn(page);


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
    Assertions.assertEquals(result.getSize(), 1);
    Assertions.assertEquals(result.iterator().next().getName(), productName);

  }
}

