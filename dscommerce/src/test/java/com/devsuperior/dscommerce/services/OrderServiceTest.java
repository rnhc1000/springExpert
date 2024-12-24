package com.devsuperior.dscommerce.services;

import com.devsuperior.dscommerce.dto.OrderDTO;
import com.devsuperior.dscommerce.entities.Order;
import com.devsuperior.dscommerce.entities.Product;
import com.devsuperior.dscommerce.entities.User;
import com.devsuperior.dscommerce.repositories.OrderItemRepository;
import com.devsuperior.dscommerce.repositories.OrderRepository;
import com.devsuperior.dscommerce.repositories.ProductRepository;
import com.devsuperior.dscommerce.services.exceptions.ForbiddenException;
import com.devsuperior.dscommerce.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dscommerce.tests.OrderFactory;
import com.devsuperior.dscommerce.tests.ProductFactory;
import com.devsuperior.dscommerce.tests.UserFactory;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
public class OrderServiceTest {

  @InjectMocks
  private OrderService orderService;

  @Mock
  private OrderRepository orderRepository;

  @Mock
  private ProductRepository productRepository;

  @Mock
  private OrderItemRepository orderItemRepository;

  @Mock
  private UserService userService;

  @Mock
  private AuthService authService;

  private Long existingOrderId, nonExistingOrderId;

  private Long existingProductId, nonExistingProductId;

  private Product product;


  private Order order;

  private OrderDTO orderDTO;

  private User admin, client;

  @BeforeEach
  void setUp() throws Exception {
    existingOrderId = 1L;
    nonExistingOrderId = 2L;

    existingProductId = 1L;
    nonExistingProductId = 2L;

    admin = UserFactory.createCustomAdminUser(1L, "Jeff");
    client = UserFactory.createCustomClientUser(2L, "Bob");

    order = OrderFactory.createOrder(client);
    orderDTO = new OrderDTO(order);

    product = ProductFactory.createProduct();

    Mockito.when(orderRepository.findById(existingOrderId)).thenReturn(Optional.of(order));
    Mockito.when(orderRepository.findById(nonExistingOrderId)).thenReturn(Optional.empty());

    Mockito.when(productRepository.getReferenceById(existingProductId)).thenReturn((product));
    Mockito.when(productRepository.getReferenceById(nonExistingProductId)).thenThrow(EntityNotFoundException.class);

    Mockito.when(orderRepository.save(any())).thenReturn(order);
    Mockito.when(orderItemRepository.saveAll(any())).thenReturn(new ArrayList<>(order.getItems()));

  }

  @Test
  void findByIdShouldReturnOrderDTOWhenIdExistsAndAdminLogged() {

    Mockito.doNothing().when(authService).validateSelfOrAdmin(any());

    OrderDTO result = orderService.findById(existingOrderId);
    Assertions.assertNotNull(result);
    Assertions.assertEquals(result.getId(), existingOrderId);

  }

  @Test
  void findByIdShouldReturnOrderDTOWhenIdExistsAndSelfClientLogged() {

    Mockito.doNothing().when(authService).validateSelfOrAdmin(any());

    OrderDTO result = orderService.findById(existingOrderId);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(result.getId(), existingOrderId);

  }

  @Test
  void findByIdShouldThrowsForbiddenExceptionWhenIdExistsWhenOtherClientLogged() {

    Mockito.doThrow(ForbiddenException.class).when(authService).validateSelfOrAdmin(any());

    Assertions.assertThrows(ForbiddenException.class, () -> {
      orderService.findById(existingOrderId);
    });

  }

  @Test
  void findByIdShouldThrowsResourceNotFoundExceptionWhenIdDoesNotExist() {
    Mockito.doNothing().when(authService).validateSelfOrAdmin(any());
    Assertions.assertThrows(ResourceNotFoundException.class, () -> {
      orderService.findById(nonExistingOrderId);
    });

  }

  @Test
  void insertShouldReturnOrderDTOWhenAdminLogged() {

    Mockito.when(userService.authenticated()).thenReturn(admin);
    OrderDTO result = orderService.insert(orderDTO);
    Assertions.assertNotNull(result);
//    Assertions.assertEquals(result.getId(), existingOrderId);
  }

  @Test
  void insertShouldReturnOrderDTOWhenClientLogged() {

    Mockito.when(userService.authenticated()).thenReturn(client);
    OrderDTO result = orderService.insert(orderDTO);

    Assertions.assertNotNull(result);
//    Assertions.assertEquals(result.getId(), existingOrderId);
  }

}
