package org.pancakelab.model;

import org.pancakelab.model.enumaration.OrderStatus;
import org.pancakelab.model.pancakes.Pancake;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Order {
    private UUID id;
    private UUID discipleId;
    private OrderStatus status;
    private List<Pancake> pancakes;

    public Order(UUID discipleId) {
        this.id = UUID.randomUUID();
        this.discipleId = discipleId;
        this.status = OrderStatus.PENDING;
        this.pancakes = new ArrayList<>();
    }

    public UUID getId() {
        return id;
    }

    public UUID getDiscipleId() {
        return discipleId;
    }

    public void setDiscipleId(UUID discipleId) {
        this.discipleId = discipleId;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public List<Pancake> getPancakes() {
        return pancakes;
    }

    public void addPancake(Pancake pancake) {
        this.pancakes.add(pancake);
    }
}
