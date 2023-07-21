package com.ace_inspiration.team_joblify.entity;

public enum Level {
    ENTRY_LEVEL("Entry Level"),
    JUNIOR_LEVEL("Junior Level"),
    MID_LEVEL("Mid Level"),
    SENIOR_LEVEL("Senior Level"),
    SUPERVISOR_LEVEL("Supervisor Level"),
    EXECUTIVE_LEVEL("Executive Level");

    private final String displayName;

    Level(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}







