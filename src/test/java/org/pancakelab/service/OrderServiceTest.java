package org.pancakelab.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pancakelab.model.Disciple;
import org.pancakelab.model.Order;
import org.pancakelab.model.enumaration.Ingredient;
import org.pancakelab.model.enumaration.OrderStatus;
import org.pancakelab.model.pancakes.Pancake;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @Spy
    private OrderService orderService;
    private UUID orderId;
    private Order order;
    private Disciple desciple;


    @BeforeEach
    public void setUp() {
        desciple = DiscipleService.getSampleDisciples().values().stream().findFirst().get();
        orderId = UUID.randomUUID();
        order = new Order(orderId);
        order.setStatus(OrderStatus.PENDING);
        order.setDiscipleId(desciple.getId());
    }

    @Test
    public void testCreateOrder_Success() {
        UUID orderId = orderService.createOrder(desciple);
        assertNotNull(orderId);
        assertEquals(OrderStatus.PENDING, orderService.getOrder(orderId).getStatus());
    }

    @Test
    public void testCreateOrder_ThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> orderService.createOrder(null));
    }

    @Test
    public void testAddPancakes_Success() {
        List<Ingredient> ingredients = List.of(Ingredient.DARK_CHOCOLATE, Ingredient.WHIPPED_CREAM);
        doReturn(order).when(orderService).getOrder(any(UUID.class));

        orderService.addPancakes(orderId, ingredients, 2);

        assertEquals(2, orderService.getOrder(orderId).getPancakes().size());
    }

    @Test
    public void testAddPancakes_ThrowsException() {
        doReturn(order).when(orderService).getOrder(any(UUID.class));
        order.setStatus(OrderStatus.COMPLETED);

        assertThrows(IllegalStateException.class,
                () -> orderService.addPancakes(orderId, Collections.emptyList(), 2));
    }

    @Test
    public void testCompleteOrder_Success() {
        Pancake pancake = Pancake.builder()
                .orderId(orderId)
                .ingredients(Collections.emptyList())
                .build();

        doReturn(order).when(orderService).getOrder(any(UUID.class));
        order.addPancake(pancake);

        orderService.completeOrder(orderId);
        assertEquals(OrderStatus.COMPLETED, order.getStatus());
    }

    @Test
    public void testCompleteOrder_ThrowsException() {
        doReturn(order).when(orderService).getOrder(any(UUID.class));

        assertThrows(IllegalStateException.class,
                () -> orderService.completeOrder(orderId));
    }

    @Test
    public void testCancelOrder_OrderPending_Success() {
        UUID orderId = orderService.createOrder(desciple);

        orderService.cancelOrder(orderId);

        assertThrows(IllegalArgumentException.class,
                () -> orderService.getOrder(orderId));
    }

    @Test
    public void testPrepareOrder_Success() {
        doReturn(order).when(orderService).getOrder(any(UUID.class));
        order.setStatus(OrderStatus.COMPLETED);

        orderService.prepareOrder(orderId);
        assertEquals(OrderStatus.PREPARING, order.getStatus());
    }

    @Test
    public void testDeliverOrder_Success() {
        UUID orderId = orderService.createOrder(desciple);
        var order = orderService.getOrder(orderId);
        order.setStatus(OrderStatus.PREPARING);

        orderService.deliverOrder(orderId);

        assertThrows(IllegalArgumentException.class,
                () -> orderService.getOrder(orderId));
    }
}
