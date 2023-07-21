package com.ace_inspiration.team_joblify.entity;

public enum OnSiteOrRemote {

    ON_SITE("On-site"),
    REMOTE("Remote"),
    HYBRID("Hybrid");

    private final String displayName;

    OnSiteOrRemote(String displayName) {
        this.displayName = displayName;
    }
    @Override
    public String toString() {
        return displayName;
    }
}
