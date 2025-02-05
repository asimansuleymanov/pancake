package org.pancakelab.service;

import org.pancakelab.model.Disciple;

import java.util.*;

public class DiscipleService {
    private static final Map<UUID, Disciple> DISCIPLES = new HashMap<>();

    static {
        Disciple disciple1 = new Disciple("MainBuilding", "101");
        Disciple disciple2 = new Disciple("SecondaryBuilding", "202");
        Disciple disciple3 = new Disciple("Dormitory", "303");

        DISCIPLES.put(disciple1.getId(), disciple1);
        DISCIPLES.put(disciple2.getId(),disciple2);
        DISCIPLES.put(disciple3.getId(),disciple3);
    }

    public static Map<UUID, Disciple> getSampleDisciples() {
        return DISCIPLES;
    }

    public static Disciple getDiscipleById(UUID discipleId) {
        return DISCIPLES.get(discipleId);
    }

    public static String getAddressByDiscipleId(UUID discipleId) {
        return String.format("building %s, room %s",
                DISCIPLES.get(discipleId).getBuilding(),
                DISCIPLES.get(discipleId).getRoomNumber());
    }
}
