package com.ace_inspiration.team_joblify.service;

import com.ace_inspiration.team_joblify.entity.Position;

import java.util.List;

import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

public interface PositionService {

    Position addPosition(Position position);
    List<Position> selectAllPosition();
    void removePosition(long id);
    List<Position> findByNameContainingIgnoreCase(String term);
    Position checkAndSetPosition(String name);
    void autoFillPosition(String newName);
    Position convertPosition(String positionName);
    Position findByName(String name);
}
