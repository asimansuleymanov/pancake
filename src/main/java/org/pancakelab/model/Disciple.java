package org.pancakelab.model;

import java.util.UUID;

public class Disciple {
    private final UUID id;
    private String building;
    private String roomNumber;

    public Disciple(String building, String roomNumber) {
        this.id = UUID.randomUUID();
        this.building = building;
        this.roomNumber = roomNumber;
    }

    public UUID getId() {
        return id;
    }

    public String getBuilding() {
        return building;
    }

    public String getRoomNumber() {
        return roomNumber;
    }
}
