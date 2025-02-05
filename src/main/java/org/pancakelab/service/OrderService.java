package org.pancakelab.service;

import org.pancakelab.model.Disciple;
import org.pancakelab.model.Order;
import org.pancakelab.model.enumaration.Ingredient;
import org.pancakelab.model.enumaration.OrderStatus;
import org.pancakelab.model.pancakes.Pancake;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;

public class OrderService {
    private final ConcurrentHashMap<UUID, Order> orders = new ConcurrentHashMap<>();

    private final Lock lock = new ReentrantLock();
    public UUID createOrder(Disciple disciple) {
        validateDisciple(disciple);
        Order order = new Order(disciple.getId());
        orders.put(order.getId(), order);
        return order.getId();
    }

    public void addPancakes(UUID orderId, List<Ingredient> ingredients, int count) {
        Order order = getOrder(orderId);
        validateOrderStatus(order, OrderStatus.PENDING);
        List<Pancake> newPancakes = generatePancakes(orderId, ingredients, count);
        OrderLog.logAddPancakes(order, "newPancakes added", newPancakes);
        newPancakes.forEach(order::addPancake);
    }

    public void completeOrder(UUID orderId) {
        executeWithLock(() -> {
            Order order = getOrder(orderId);
            validateOrderStatus(order, OrderStatus.PENDING);
            if (order.getPancakes().isEmpty()) {
                throw new IllegalStateException("Order must have at least one pancake to complete.");
            }
            order.setStatus(OrderStatus.COMPLETED);
        });
    }

    public void cancelOrder(UUID orderId) {
        executeWithLock(() -> {
            Order order = getOrder(orderId);
            validateOrderStatus(order, OrderStatus.PENDING);
            orders.remove(orderId);
            OrderLog.logCancelOrder(order, order.getPancakes());
        });
    }

    public void prepareOrder(UUID orderId) {
        executeWithLock(() -> {
            Order order = getOrder(orderId);
            validateOrderStatus(order, OrderStatus.COMPLETED);
            order.setStatus(OrderStatus.PREPARING);
        });
    }

    public void deliverOrder(UUID orderId) {
        executeWithLock(() -> {
            Order order = getOrder(orderId);
            validateOrderStatus(order, OrderStatus.PREPARING);
            orders.remove(orderId);
            OrderLog.logDeliverOrder(order, order.getPancakes());
        });
    }

    public Order getOrder(UUID orderId) {
        Order order = orders.get(orderId);
        if (order == null) {
            throw new IllegalArgumentException("Order not found for ID: " + orderId);
        }
        return order;
    }

    private void validateDisciple(Disciple disciple) {
        if (disciple == null || disciple.getBuilding() == null || disciple.getBuilding().isEmpty() ||
                disciple.getRoomNumber() == null || disciple.getRoomNumber().isEmpty()) {
            throw new IllegalArgumentException("Disciple building and room must be valid.");
        }
    }

    private void validateOrderStatus(Order order, OrderStatus expectedStatus) {
        if (!expectedStatus.equals(order.getStatus())) {
            throw new IllegalStateException("Invalid order status. Expected: " + expectedStatus);
        }
    }

    private List<Pancake> generatePancakes(UUID orderId, List<Ingredient> ingredients, int count) {
        return IntStream.range(0, count)
                .mapToObj(i -> Pancake
                        .builder()
                        .orderId(orderId)
                        .ingredients(ingredients)
                        .build())
                .toList();
    }

    private void executeWithLock(Runnable action) {
        lock.lock();
        try {
            action.run();
        } finally {
            lock.unlock();
        }
    }
}
