package com.devsuperior.dscommerce.controllers;

import com.devsuperior.dscommerce.dto.ProductDTO;
import com.devsuperior.dscommerce.entities.Category;
import com.devsuperior.dscommerce.entities.Product;
import com.devsuperior.dscommerce.tests.TokenUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ProductControllerIT {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private TokenUtil tokenUtil;

  @Autowired
  private ObjectMapper objectMapper;

  private String productName;
  private String jsonBody;
  private String clientUsername, clientPassword, adminUsername, adminPassword;
  private String clientToken, adminToken, invalidToken;

  private Long existingProductId, nonExistingProductId, dependentProductId;

  private Product product;
  private ProductDTO productDTO;

  @BeforeEach
  void setUp() throws Exception {

    productName = "Macbook";
    clientUsername = "maria@gmail.com";
    clientPassword = "123456";
    adminUsername = "alex@gmail.com";
    adminPassword = "123456";

    existingProductId = 2L;
    nonExistingProductId = 100L;
    dependentProductId = 3L;

    adminToken = tokenUtil.obtainAccessToken(mockMvc, adminUsername, adminPassword);
    clientToken = tokenUtil.obtainAccessToken(mockMvc, clientUsername, clientPassword);
    invalidToken = adminToken + "xpto";
    Category category = new Category(2L, "Eletro");
    product = new Product(null, "Console PlayStation 5",
        "Lorem ipsum, dolor sit amet consectetur adipisicing elit.",
        3990.0,
        "https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/1-big.jpg");
    product.getCategories().add(category);

    productDTO = new ProductDTO(product);
  }

  @Test
  void findAllShouldReturnPageWhenNameParamIsNotEmpty() throws Exception {

    ResultActions resultActions = mockMvc
        .perform(MockMvcRequestBuilders.get("/products?name={productName}", productName)
            .accept(MediaType.APPLICATION_JSON));

    resultActions.andExpect(status().isOk());
    resultActions.andExpect(jsonPath("$.content[0].id").value(3L));
    resultActions.andExpect(jsonPath("$.content[0].name").value("Macbook Pro"));
    resultActions.andExpect(jsonPath("$.content[0].price").value(1250.0));
  }

  @Test
  void findAllShouldReturnPageWhenNameParamIsEmpty() throws Exception {

    ResultActions resultActions = mockMvc
        .perform(MockMvcRequestBuilders.get("/products")
            .accept(MediaType.APPLICATION_JSON));

    resultActions.andExpect(status().isOk());
    resultActions.andExpect(jsonPath("$.content[0].id").value(1L));
    resultActions.andExpect(jsonPath("$.content[0].name").value("The Lord of the Rings"));
    resultActions.andExpect(jsonPath("$.content[0].price").value(90.5));
  }

  @Test
  void insertShouldReturnProductDTOCreatedWhenAdminLogged() throws Exception {

    jsonBody = objectMapper.writeValueAsString(productDTO);
    ResultActions resultActions = mockMvc
        .perform(MockMvcRequestBuilders.post("/products")
            .header("Authorization", "Bearer " + adminToken)
            .content(jsonBody)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andDo(MockMvcResultHandlers.print());

    resultActions.andExpect(status().isCreated());
    resultActions.andExpect(jsonPath("$.id").value(26L));
    resultActions.andExpect(jsonPath("$.name").value("Console PlayStation 5"));
    resultActions.andExpect(jsonPath("$.description").value("Lorem ipsum, dolor sit amet consectetur adipisicing elit."));
    resultActions.andExpect(jsonPath("$.price").value(3990.0));
    resultActions.andExpect(jsonPath("$.imgUrl").value("https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/1-big.jpg"));
    resultActions.andExpect(jsonPath("$.categories[0].id").value(2L));
  }

  @Test
  void insertShouldReturnUnprocessableEntityWhenAdminLoggedAndInvalidName() throws Exception {

    product.setName("ab");
    productDTO = new ProductDTO(product);
    jsonBody = objectMapper.writeValueAsString(productDTO);
    ResultActions resultActions = mockMvc
        .perform(MockMvcRequestBuilders.post("/products")
            .header("Authorization", "Bearer " + adminToken)
            .content(jsonBody)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andDo(MockMvcResultHandlers.print());

    resultActions.andExpect(status().isUnprocessableEntity());
  }

  @Test
  void insertShouldReturnUnprocessableEntityWhenAdminLoggedAndInvalidDescription() throws Exception {

    product.setDescription("ab");
    productDTO = new ProductDTO(product);
    jsonBody = objectMapper.writeValueAsString(productDTO);
    ResultActions resultActions = mockMvc
        .perform(MockMvcRequestBuilders.post("/products")
            .header("Authorization", "Bearer " + adminToken)
            .content(jsonBody)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andDo(MockMvcResultHandlers.print());

    resultActions.andExpect(status().isUnprocessableEntity());
  }

  @Test
  void insertShouldReturnUnprocessableEntityWhenAdminLoggedAndPriceIsNegative() throws Exception {

    product.setPrice(-50.0);
    productDTO = new ProductDTO(product);
    jsonBody = objectMapper.writeValueAsString(productDTO);
    ResultActions resultActions = mockMvc
        .perform(MockMvcRequestBuilders.post("/products")
            .header("Authorization", "Bearer " + adminToken)
            .content(jsonBody)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andDo(MockMvcResultHandlers.print());

    resultActions.andExpect(status().isUnprocessableEntity());
  }

  @Test
  void insertShouldReturnUnprocessableEntityWhenAdminLoggedAndPriceIsZero() throws Exception {

    product.setPrice(0.0);
    productDTO = new ProductDTO(product);
    jsonBody = objectMapper.writeValueAsString(productDTO);
    ResultActions resultActions = mockMvc
        .perform(MockMvcRequestBuilders.post("/products")
            .header("Authorization", "Bearer " + adminToken)
            .content(jsonBody)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andDo(MockMvcResultHandlers.print());

    resultActions.andExpect(status().isUnprocessableEntity());
  }

  @Test
  void insertShouldReturnUnprocessableEntityWhenAdminLoggedAndProductHasNotCategory() throws Exception {

    product.getCategories().clear();
    productDTO = new ProductDTO(product);
    jsonBody = objectMapper.writeValueAsString(productDTO);
    ResultActions resultActions = mockMvc
        .perform(MockMvcRequestBuilders.post("/products")
            .header("Authorization", "Bearer " + adminToken)
            .content(jsonBody)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andDo(MockMvcResultHandlers.print());

    resultActions.andExpect(status().isUnprocessableEntity());
  }

  @Test
  void insertShouldReturnForbiddenWhenClientLogged() throws Exception {

    jsonBody = objectMapper.writeValueAsString(productDTO);
    ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/products")
        .header("Authorization", "Bearer " + clientToken)
        .content(jsonBody)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON));

    resultActions.andExpect(status().isForbidden());
  }


  @Test
  void insertShouldReturnUnauthorizedWhenInvalidToken() throws Exception {

    jsonBody = objectMapper.writeValueAsString(productDTO);
    ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/products")
        .header("Authorization", "Bearer " + invalidToken)
        .content(jsonBody)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON));

    resultActions.andExpect(status().isUnauthorized());
  }

  @Test
  void deleteShouldReturnNoContentWhenIdExistsAndAdminLogged() throws Exception {

    ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.delete("/products/{id}", existingProductId)
        .header("Authorization", "Bearer " + adminToken)
        .accept(MediaType.APPLICATION_JSON));

    resultActions.andExpect(status().isNoContent());
  }

  @Test
  void deleteShouldReturnNotFoundWhenIdExistsAndAdminLogged() throws Exception {

    ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.delete("/products/{id}", nonExistingProductId)
        .header("Authorization", "Bearer " + adminToken)
        .accept(MediaType.APPLICATION_JSON));

    resultActions.andExpect(status().isNotFound());
  }

  @Test
  @Transactional(propagation = Propagation.SUPPORTS)
  void deleteShouldReturnBadRequestExceptionWhenIdDependentAndAdminLogged() throws Exception {

    ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.delete("/products/{id}", dependentProductId)
        .header("Authorization", "Bearer " + adminToken)
        .accept(MediaType.APPLICATION_JSON));

    resultActions.andExpect(status().isBadRequest());
  }


  @Test
  void deleteShouldReturnForbiddenWhenIdDependentAndClientLogged() throws Exception {

    ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.delete("/products/{id}", dependentProductId)
        .header("Authorization", "Bearer " + clientToken)
        .accept(MediaType.APPLICATION_JSON));

    resultActions.andExpect(status().isForbidden());
  }


  @Test
  void deleteShouldReturnUnauthorizedWhenIdExistsAndInvalidToken() throws Exception {

    ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.delete("/products/{id}", existingProductId)
        .header("Authorization", "Bearer " + invalidToken)
        .accept(MediaType.APPLICATION_JSON));

    resultActions.andExpect(status().isUnauthorized());
  }


}
