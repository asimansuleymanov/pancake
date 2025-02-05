package org.pancakelab.service;

import org.pancakelab.model.Order;
import org.pancakelab.model.pancakes.PancakeRecipe;

import java.util.List;

import static org.pancakelab.service.DiscipleService.getAddressByDiscipleId;

public class OrderLog {
    private static final StringBuilder log = new StringBuilder();

    public static void logAddPancakes(Order order, String description, List<? extends PancakeRecipe> pancakes) {
        long pancakesInOrder = pancakes.stream().filter(p -> p.getOrderId().equals(order.getId())).count();

        log.append("Added pancake with description '%s' ".formatted(description))
                .append("to order %s containing %d pancakes, ".formatted(order.getId(), pancakesInOrder))
                .append("for %s.".formatted(getAddressByDiscipleId(order.getDiscipleId())));
    }

    public static void logRemovePancakes(Order order, String description, int count, List<PancakeRecipe> pancakes) {
        long pancakesInOrder = pancakes.stream().filter(p -> p.getOrderId().equals(order.getId())).count();

        log.append("Removed %d pancake(s) with description '%s' ".formatted(count, description))
                .append("from order %s now containing %d pancakes, ".formatted(order.getId(), pancakesInOrder))
                .append("for %s.".formatted(getAddressByDiscipleId(order.getDiscipleId())));
    }

    public static void logCancelOrder(Order order, List<? extends PancakeRecipe> pancakes) {
        long pancakesInOrder = pancakes.stream().filter(p -> p.getOrderId().equals(order.getId())).count();
        log.append("Cancelled order %s with %d pancakes ".formatted(order.getId(), pancakesInOrder))
                .append("for %s.".formatted(getAddressByDiscipleId(order.getDiscipleId())));
    }

    public static void logDeliverOrder(Order order, List<? extends PancakeRecipe> pancakes) {
        long pancakesInOrder = pancakes.stream().filter(p -> p.getOrderId().equals(order.getId())).count();
        log.append("Order %s with %d pancakes ".formatted(order.getId(), pancakesInOrder))
                .append("for %s.".formatted(getAddressByDiscipleId(order.getDiscipleId())));
    }
}
