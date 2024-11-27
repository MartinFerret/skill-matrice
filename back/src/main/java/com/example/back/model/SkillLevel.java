package com.example.back.model;

public enum SkillLevel {
    BEGINNER(1),
    INTERMEDIATE(2),
    ADVANCED(3),
    EXPERT(4);

    private final int rank;
    SkillLevel(int rank) {
        this.rank = rank;
    }

    public int getRank() {
        return rank;
    }
}
