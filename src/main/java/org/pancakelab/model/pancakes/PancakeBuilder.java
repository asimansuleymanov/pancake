package org.pancakelab.model.pancakes;

import org.pancakelab.model.enumaration.Ingredient;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PancakeBuilder {
    private List<Ingredient> ingredients = new ArrayList<>();
    private UUID orderId;

    public PancakeBuilder orderId(UUID orderId) {
        this.orderId = orderId;
        return this;
    }

    public PancakeBuilder ingredients(List<Ingredient> ingredients) {
        if(ingredients != null) {
            this.ingredients = ingredients;
        }
        return this;
    }

    public Pancake build() {
        if (orderId == null) {
            throw new IllegalStateException("Order ID must be provided");
        }
        return new Pancake(orderId, new ArrayList<>(this.ingredients));
    }
}
