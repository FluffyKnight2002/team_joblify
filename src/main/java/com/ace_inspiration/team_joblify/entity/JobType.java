package com.ace_inspiration.team_joblify.entity;

public enum JobType {

    PART_TIME("Part time"),
    FULL_TIME("Full time");

    private final String displayName;

    JobType(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }

}
