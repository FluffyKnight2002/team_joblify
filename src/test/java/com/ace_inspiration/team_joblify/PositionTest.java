//package com.ace_inspiration.team_joblify;
//
//import com.ace_inspiration.team_joblify.entity.Position;
//import com.ace_inspiration.team_joblify.repository.PositionRepository;
//import com.ace_inspiration.team_joblify.service.PositionService;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.mockito.Mockito.when;
//
//class PositionTest {
//
//    @Mock
//    private PositionRepository positionRepository;
//
//    @Mock
//    private PositionService positionService;
//
//    private void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void checkAndSetPosition() {
//        setUp(); // Initialize the mocks
//
//        String positionName = "New Position";
//
//        // Mock the behavior of positionRepository
//        when(positionRepository.findByName(positionName)).thenReturn(null);
//
//        // Call the method under test
//        if (positionRepository.findByName(positionName) == null) {
//            autoFillPosition(positionName);
//        }
//        Position position = convertPosition(positionName);
//
//        assertNotNull(position);
//        assertNotNull(position.getName());
//    }
//
//    void autoFillPosition(String newName) {
//        Position newPosition = Position.builder()
//                .name(newName)
//                .build();
//        positionService.addPosition(newPosition);
//    }
//
//    Position convertPosition(String positionName) {
//        return positionRepository.findByName(positionName)
//                .orElseThrow(()-> new UsernameNotFoundException("Position not found"));
//    }
//}
//
