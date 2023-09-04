package com.ace_inspiration.team_joblify.service_implement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import com.ace_inspiration.team_joblify.dto.CandidateDto;
import com.ace_inspiration.team_joblify.entity.*;
import com.ace_inspiration.team_joblify.repository.*;
import com.ace_inspiration.team_joblify.service.InterviewService;
import com.ace_inspiration.team_joblify.service_implement.candidate_service_implement.CandidateServiceImplement;
import jakarta.persistence.EntityManager;
import java.io.IOException;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
public class CandidateServiceImplementTest {

	@InjectMocks
	private CandidateServiceImplement candidateService;

	@Mock
	private CandidateRepository candidateRepository;

	@Mock
	private SummaryRepository summaryRepository;

	@Mock
	private InterviewService interviewService;

	@Mock
	private LanguageSkillsRepository languageSkillsRepository;

	@Mock
	private TechSkillsRepository techSkillsRepository;

	@Mock
	private VacancyInfoRepository vacancyInfoRepository;

	@Mock
	private EntityManager entityManager;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testGetAllcandidate() {
		// Mock the behavior of candidateRepository
		DataTablesInput input = new DataTablesInput();
		DataTablesOutput<Candidate> expectedOutput = new DataTablesOutput<>();
		when(candidateRepository.findAll(input)).thenReturn(expectedOutput);

		// Call the getAllcandidate method
		DataTablesOutput<Candidate> result = candidateService.getAllcandidate(input);

		// Assertions to check if the method returns the expected result
		assertEquals(expectedOutput, result);
	}
	
	@Test
	public void testChangeStatus() {
		long candidateId = 1L;

		// Create a sample Candidate
		Candidate sampleCandidate = new Candidate();
		sampleCandidate.setId(candidateId);

		// Mock the behavior of entityManager.find
		when(entityManager.find(Candidate.class, candidateId)).thenReturn(sampleCandidate);

		// Call the changeStatus method
		candidateService.changeStatus(candidateId);

		// Verify that the selectionStatus was set to VIEWED and entityManager.persist
		// was called
		assertEquals(Status.VIEWED, sampleCandidate.getSelectionStatus());
		verify(entityManager).persist(sampleCandidate);
	}

	@Test
	public void testSaveCandidate() throws IOException {
		// Create a sample CandidateDto
		CandidateDto candidateDto = new CandidateDto();
		// Set up your candidateDto with necessary data

		// Mock the behavior of languageSkillsRepository and techSkillsRepository
		when(languageSkillsRepository.findByNameIgnoreCase(anyString())).thenReturn(Optional.empty());
		when(techSkillsRepository.findByNameIgnoreCase(anyString())).thenReturn(Optional.empty());

		// Create a sample VacancyInfo
		VacancyInfo vacancyInfo = new VacancyInfo();
		vacancyInfo.setId(1L);

		// Mock the behavior of vacancyInfoRepository
		when(vacancyInfoRepository.findById(anyLong())).thenReturn(Optional.of(vacancyInfo));

		// Create a sample Summary
		Summary summary = new Summary();
		summary.setId(1L);

		// Mock the behavior of summaryRepository
		when(summaryRepository.save(any())).thenReturn(summary);

		// Create a sample Candidate
		Candidate sampleCandidate = new Candidate();
		sampleCandidate.setId(1L);

		// Mock the behavior of candidateRepository
		when(candidateRepository.save(any())).thenReturn(sampleCandidate);
	}
}
