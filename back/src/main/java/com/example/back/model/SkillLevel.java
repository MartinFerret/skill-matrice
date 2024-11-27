package com.example.back.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum SkillLevel {
    BEGINNER(1),
    INTERMEDIATE(2),
    ADVANCED(3),
    EXPERT(4);

    private final int rank;

    SkillLevel(int rank) {
        this.rank = rank;
    }

    @JsonValue
    public int getRank() {
        return rank;
    }

    @JsonCreator
    public static SkillLevel forValue(int value) {
        for (SkillLevel level : SkillLevel.values()) {
            if (level.getRank() == value) {
                return level;
            }
        }
        throw new IllegalArgumentException("Invalid rank: " + value);
    }
}
