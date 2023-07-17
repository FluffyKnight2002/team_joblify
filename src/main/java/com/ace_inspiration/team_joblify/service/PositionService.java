package com.ace_inspiration.team_joblify.service;

import com.ace_inspiration.team_joblify.entity.Position;

import java.util.List;

public interface PositionService {

    void addPosition(Position position);
    List<Position> selectAllPosition();
    void removePosition(long id);
    List<Position> findByNameContainingIgnoreCase(String term);

}
