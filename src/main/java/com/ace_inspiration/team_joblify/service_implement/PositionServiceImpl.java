package com.ace_inspiration.team_joblify.service_implement;

import com.ace_inspiration.team_joblify.entity.Position;
import com.ace_inspiration.team_joblify.repository.PositionRepository;
import com.ace_inspiration.team_joblify.service.PositionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PositionServiceImpl implements PositionService {

    private final PositionRepository positionRepository;

    public PositionServiceImpl(PositionRepository positionRepository) {
        this.positionRepository = positionRepository;
    }

    @Override
    public void addPosition(Position position) {
        positionRepository.save(position);
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
}
