package com.ace_inspiration.team_joblify.service_implement;


import com.ace_inspiration.team_joblify.entity.Position;
import com.ace_inspiration.team_joblify.repository.PositionRepository;
import com.ace_inspiration.team_joblify.service.PositionService;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PositionServiceImpl implements PositionService {

    private final PositionRepository positionRepository;

    @Override
    public Position addPosition(Position position) {
        return positionRepository.save(position);
    }

    @Override
    public List<Position> selectAllPosition() {
        return positionRepository.findAll();
    }

    @Override
    public void removePosition(long id) {
        positionRepository.deleteById(id);
    }

    @Override
    public List<Position> findByNameContainingIgnoreCase(String term) {
        return positionRepository.findByNameContainingIgnoreCase(term);
    }

    @Override
    public Position checkAndSetPosition(String positionName) {
        Position position = new Position();
        if(positionRepository.findByName(positionName).isEmpty()) {
            autoFillPosition(positionName);
        }
        position = convertPosition(positionName);
        return position;
    }
    public void autoFillPosition(String newName) {
        Position newPosition = Position.builder()
                .name(newName)
                .build();
        positionRepository.save(newPosition);
    }
    public Position convertPosition(String positionName) {
        return positionRepository.findByName(positionName).orElse(null);
    }

    @Override
    public Position findByName(String name){
        return positionRepository.findByName(name).orElse(null);
    }
    

}
