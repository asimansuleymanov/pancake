package org.pancakelab.model.pancakes;

import org.pancakelab.model.enumaration.Ingredient;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class Pancake implements PancakeRecipe {
    private final UUID id;
    private final UUID orderId;
    private final List<Ingredient> ingredients;

    Pancake(UUID orderId, List<Ingredient> ingredients) {
        this.id = UUID.randomUUID();
        this.ingredients = ingredients;
        this.orderId = orderId;
    }

    public UUID getId() {
        return id;
    }

    public List<Ingredient> getIngredients() {
        return Collections.unmodifiableList(ingredients);
    }

    public static PancakeBuilder builder() {
        return new PancakeBuilder();
    }

    @Override
    public UUID getOrderId() {
        return orderId;
    }

    @Override
    public List<String> ingredients() {
        return ingredients.stream().map(Ingredient::toString).collect(Collectors.toList());
    }
}
