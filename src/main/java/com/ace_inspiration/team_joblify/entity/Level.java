package com.ace_inspiration.team_joblify.entity;

public enum Level {
	ENTRY_LEVEL("A"), JUNIOR_LEVEL("B"), MID_LEVEL("C"), SENIOR_LEVEL("D"), SUPERVISOR_LEVEL("E"), EXECUTIVE_LEVEL("F"),
	DIRECTOR_LEVEL("G");

	private final String name;
	Level(String name) {
		this.name =name;
	}
}