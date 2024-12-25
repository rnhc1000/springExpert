package com.devsuperior.dscommerce.controllers;

import com.devsuperior.dscommerce.dto.OrderDTO;
import com.devsuperior.dscommerce.entities.*;
import com.devsuperior.dscommerce.tests.ProductFactory;
import com.devsuperior.dscommerce.tests.TokenUtil;
import com.devsuperior.dscommerce.tests.UserFactory;
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
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class OrderControllerIT {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private TokenUtil tokenUtil;

  @Autowired
  private ObjectMapper objectMapper;

  private String jsonBody;
  private String clientUsername, clientPassword, adminUsername, adminPassword;
  private String clientToken, adminToken, invalidToken;

  private Long existingOrderId, nonExistingOrderId;

  private Order order;

  private OrderDTO orderDTO;

  private User user;

  @BeforeEach
  void setUp() throws Exception {

    clientUsername = "maria@gmail.com";
    clientPassword = "123456";
    adminUsername = "alex@gmail.com";
    adminPassword = "123456";

    existingOrderId = 1L;
    nonExistingOrderId = 100L;

    adminToken = tokenUtil.obtainAccessToken(mockMvc, adminUsername, adminPassword);
    clientToken = tokenUtil.obtainAccessToken(mockMvc, clientUsername, clientPassword);
    invalidToken = adminToken + "crypto";

    user = UserFactory.createClientUser();
    order = new Order(null, Instant.now(), OrderStatus.WAITING_PAYMENT, user, null);

    Product product = ProductFactory.createProduct();

    OrderItem orderItem = new OrderItem(order, product, 2, 10.0);
    order.getItems().add(orderItem);
  }

  @Test
  void findByIdShouldReturnOrderDTOWhenIdExistsAndAdminLogged() throws Exception {

    ResultActions resultActions = mockMvc
        .perform(MockMvcRequestBuilders.get("/orders/{id}", existingOrderId)
            .header("Authorization", "Bearer " + adminToken)
            .accept(MediaType.APPLICATION_JSON))
        .andDo(MockMvcResultHandlers.print());

    resultActions.andExpect(status().isOk());
    resultActions.andExpect(jsonPath("$.id").value(existingOrderId));
    resultActions.andExpect(jsonPath("$.moment").value("2022-07-25T13:00:00Z"));
    resultActions.andExpect(jsonPath("$.status").value("PAID"));
    resultActions.andExpect(jsonPath("$.client").exists());
    resultActions.andExpect(jsonPath("$.client.name").value("Maria Brown"));
    resultActions.andExpect(jsonPath("$.payment").exists());
    resultActions.andExpect(jsonPath("$.items").exists());
    resultActions.andExpect(jsonPath("$.total").exists());
  }


  @Test
  void findByIdShouldReturnOrderDTOWhenIdExistsAndClientLogged() throws Exception {

    ResultActions resultActions = mockMvc
        .perform(MockMvcRequestBuilders.get("/orders/{id}", existingOrderId)
            .header("Authorization", "Bearer " + clientToken)
            .accept(MediaType.APPLICATION_JSON))
        .andDo(MockMvcResultHandlers.print());

    resultActions.andExpect(status().isOk());
    resultActions.andExpect(jsonPath("$.id").value(existingOrderId));
    resultActions.andExpect(jsonPath("$.moment").value("2022-07-25T13:00:00Z"));
    resultActions.andExpect(jsonPath("$.status").value("PAID"));
    resultActions.andExpect(jsonPath("$.client").exists());
    resultActions.andExpect(jsonPath("$.client.name").value("Maria Brown"));
    resultActions.andExpect(jsonPath("$.payment").exists());
    resultActions.andExpect(jsonPath("$.items").exists());
    resultActions.andExpect(jsonPath("$.total").exists());
  }

  @Test
  void findByIdShouldReturnForbiddenWhenIdExistsAndClientLoggedAndOrderDoesNotBelongToClient() throws Exception {
    Long otherOrderId = 2L;

    ResultActions resultActions = mockMvc
        .perform(MockMvcRequestBuilders.get("/orders/{id}", otherOrderId)
            .header("Authorization", "Bearer " + clientToken)
            .accept(MediaType.APPLICATION_JSON))
        .andDo(MockMvcResultHandlers.print());

    resultActions.andExpect(status().isForbidden());
  }

  @Test
  void findByIdShouldReturnNotFoundWhenIdDoesNotExistAndAdminLogged() throws Exception {

    ResultActions resultActions = mockMvc
        .perform(MockMvcRequestBuilders.get("/orders/{id}", nonExistingOrderId)
            .header("Authorization", "Bearer " + adminToken)
            .accept(MediaType.APPLICATION_JSON))
        .andDo(MockMvcResultHandlers.print());

    resultActions.andExpect(status().isNotFound());
  }

  @Test
  void findByIdShouldReturnNotFoundWhenIdDoesNotExistAndClientLogged() throws Exception {

    ResultActions resultActions = mockMvc
        .perform(MockMvcRequestBuilders.get("/orders/{id}", nonExistingOrderId)
            .header("Authorization", "Bearer " + clientToken)
            .accept(MediaType.APPLICATION_JSON))
        .andDo(MockMvcResultHandlers.print());

    resultActions.andExpect(status().isNotFound());
  }

  @Test
  void findByIdShouldReturnUnauthorizedWhenIdDoesNotExistAndInvalidToken() throws Exception {

    ResultActions resultActions = mockMvc
        .perform(MockMvcRequestBuilders.get("/orders/{id}", existingOrderId)
            .header("Authorization", "Bearer " + invalidToken)
            .accept(MediaType.APPLICATION_JSON))
        .andDo(MockMvcResultHandlers.print());

    resultActions.andExpect(status().isUnauthorized());
  }
}
