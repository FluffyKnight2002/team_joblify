//package com.ace_inspiration.team_joblify;
//
//import com.ace_inspiration.team_joblify.entity.Position;
//import com.ace_inspiration.team_joblify.repository.PositionRepository;
//import com.ace_inspiration.team_joblify.service_implement.PositionServiceImpl;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import static org.mockito.Mockito.*;
//
//class PositionServiceImplTest {
//
//    @Mock
//    private PositionRepository positionRepository;
//
//    @InjectMocks
//    private PositionServiceImpl positionService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void addPositionTest() {
//        Position position = TestUtil.createMockPosition();
//
//        positionService.addPosition(position);
//
//        verify(positionRepository, times(1)).save(position);
//    }
//
//    @Test
//    void selectAllPositionTest() {
//        List<Position> positions = new ArrayList<>();
//
//        when(positionRepository.findAll()).thenReturn(positions);
//
//        List<Position> result = positionService.selectAllPosition();
//
//        verify(positionRepository, times(1)).findAll();
//        assertTrue(result != null);
//    }
//
//    @Test
//    void removePositionTest() {
//        long id = 1L;
//
//        positionService.removePosition(id);
//
//        verify(positionRepository, times(1)).deleteById(id);
//    }
//}
//
