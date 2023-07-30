package com.ace_inspiration.team_joblify.entity;

import java.io.Serializable;

public enum Status implements Serializable {
	NONE,
    RECEIVED,
    VIEWED,
    CONSIDERING,
    PASSED,
    PENDING,
    CANCEL,
    OPEN,
    CLOSE,
    EXPIRED
}
