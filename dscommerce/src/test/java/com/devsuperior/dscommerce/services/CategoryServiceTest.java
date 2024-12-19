package com.devsuperior.dscommerce.services;

import com.devsuperior.dscommerce.dto.CategoryDTO;
import com.devsuperior.dscommerce.entities.Category;
import com.devsuperior.dscommerce.repositories.CategoryRepository;
import com.devsuperior.dscommerce.tests.CategoryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(SpringExtension.class)
public class CategoryServiceTest {


  @InjectMocks
  private CategoryService categoryService;

  @Mock
  private CategoryRepository categoryRepository;

  private Category category;
  private List<Category> list;

  @BeforeEach
  void setUp() throws Exception {
    category = CategoryFactory.createCategory();
    list = new ArrayList<>();
    list.add(category);

    Mockito.when(categoryRepository.findAll()).thenReturn(list);
  }

  @Test
  void findAllShouldReturnListCategoryDTO() {

    List<CategoryDTO> result = categoryService.findAll();

    Assertions.assertEquals(1, result.size());
    Assertions.assertEquals(result.get(0).getId(), category.getId());
    Assertions.assertEquals(result.get(0).getName(), category.getName());
  }
}
