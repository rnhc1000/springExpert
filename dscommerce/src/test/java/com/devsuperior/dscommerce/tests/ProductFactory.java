package com.devsuperior.dscommerce.tests;

import com.devsuperior.dscommerce.entities.Category;
import com.devsuperior.dscommerce.entities.Product;

public class ProductFactory {

  public static Product createProduct() {

    Category category = CategoryFactory.createCategory();
    Product product = new Product(1L, "Console Playstation", "ipsis lorem",
        5000.00, "https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/3-big.jpg");
    product.getCategories().add(category);

    return product;
  }

  public static Product createProduct(String name) {

    Product product = createProduct();
    product.setName(name);

    return product;
  }

  }
