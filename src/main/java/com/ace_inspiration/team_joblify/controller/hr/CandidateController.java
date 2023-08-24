package com.ace_inspiration.team_joblify.controller.hr;


import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.MergedAnnotations.Search;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;


import com.ace_inspiration.team_joblify.entity.*;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.StreamUtils;
import org.springframework.util.xml.DomUtils;
import org.springframework.util.xml.SimpleNamespaceContext;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

//import com.ace_inspiration.team_joblify.config.FirstDaySpecification;
import com.ace_inspiration.team_joblify.dto.CandidateDto;
import com.ace_inspiration.team_joblify.dto.CountDto;
import com.ace_inspiration.team_joblify.dto.SummaryDto;
import com.ace_inspiration.team_joblify.dto.VacancyDto;
import com.ace_inspiration.team_joblify.entity.Position;
import com.ace_inspiration.team_joblify.entity.Summary;
import com.ace_inspiration.team_joblify.repository.CandidateRepository;
import com.ace_inspiration.team_joblify.repository.DasboardRespository;
import com.ace_inspiration.team_joblify.entity.AllPost;
import com.ace_inspiration.team_joblify.entity.Candidate;
import com.ace_inspiration.team_joblify.entity.Interview;
import com.ace_inspiration.team_joblify.entity.InterviewProcess;
import com.ace_inspiration.team_joblify.entity.InterviewStage;
import com.ace_inspiration.team_joblify.entity.InterviewType;
import com.ace_inspiration.team_joblify.repository.AllPostRepository;
import com.ace_inspiration.team_joblify.repository.InterviewProcessRepository;
import com.ace_inspiration.team_joblify.repository.VacancyInfoRepository;
import com.ace_inspiration.team_joblify.service.AllPostService;
//import com.ace_inspiration.team_joblify.service.AllPostService;
//import com.ace_inspiration.team_joblify.service.DasboardService;
import com.ace_inspiration.team_joblify.service.InterviewProcessService;
import com.ace_inspiration.team_joblify.service.PositionService;
import com.ace_inspiration.team_joblify.service.VacancyInfoService;
import com.ace_inspiration.team_joblify.service.candidate_service.CandidateService;
import com.ace_inspiration.team_joblify.service.candidate_service.SummaryService;
import com.ace_inspiration.team_joblify.service_implement.PositionServiceImpl;
import com.ace_inspiration.team_joblify.service_implement.candidate_service_implement.CandidateServiceImplement;
import com.ace_inspiration.team_joblify.service_implement.candidate_service_implement.SummaryServiceImplement;

import jakarta.mail.Multipart;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import static org.springframework.util.StringUtils.hasText;

@RestController
@RequiredArgsConstructor
public class CandidateController {

	  @Value("${xml.file.path}") // Specify the path to the XML file in your application.properties
	   private String xmlFilePath;

	private final CandidateService candidateService;

	private final SummaryService summaryService;

	private final PositionService positioinService;

	private final AllPostService allPostService;

	
	//private final DasboardService dasboardservice;

	private final InterviewProcessService interviewservice;
	
	private final InterviewProcessRepository repo;
	
	private final VacancyInfoService vacancyInfoService;
	
	private final VacancyInfoRepository vanInfoReopository;
	
//	private FirstDaySpecification firstDaySpecification;


	@GetMapping("/allCandidate")
	@ResponseBody
	public DataTablesOutput<InterviewProcess> getAllCandidate( DataTablesInput input) {

   		DataTablesOutput<InterviewProcess> interviewData= interviewservice.getAll(input);
//   		 firstDaySpecification = new FirstDaySpecification(input);
//
//   		System.out.println(input);
//
//   		if(firstDaySpecification==null) {
//   		 return interviewData;
//   		}
//   		else {
//   			interviewData=repo.findAll(input,firstDaySpecification);
//   		}
   		return interviewData;

   	  }


	
	@GetMapping("/allPositions")
	@ResponseBody
	public List<Position> getAllPosition(){
		List<Position> position=positioinService.selectAllPosition();
		return position;
	}

	@PostMapping("/changStatus")
	public void changeStatus(@RequestBody long id){
		candidateService.changeStatus(id);
	}
	
	 @PostMapping("/seeMore")
	 @ResponseBody
	 public SummaryDto updateStatus(@RequestBody long id) {
	     SummaryDto summaryDto=candidateService.findByid(id);
	    return summaryDto;
	 }
	
	@PostMapping("/changeInterview")
	@ResponseBody
	public ResponseEntity<?> changeInterview(@RequestParam("id") long id,@RequestParam("status") String status) {
		
		candidateService.changeInterviewstatus(id, status);
		return ResponseEntity.ok("okokok");
	}

	@GetMapping("/process")
	@ResponseBody
	public DataTablesOutput<AllPost> interviewProcess(DataTablesInput input){
		DataTablesOutput<AllPost> allpost=allPostService.getAll(input);
		return allpost;
	}
	
	@GetMapping("/count")
	@ResponseBody
	public List<CountDto> getCounts() {
	    List<Object[]> results = vanInfoReopository.getVacancyInfoWithCandidateCounts();
	    
	    List<CountDto> dtos = new ArrayList<>();
	    for (Object[] result : results) {
	        CountDto dto = new CountDto();
	        dto.setId((long) result[0]);
	        
	        // Convert java.sql.Date to LocalDate
	        dto.setClose(( (Date) result[1]).toLocalDate()); 
	        dto.setOpen(( (Date) result[2]).toLocalDate());
	        
	        dto.setPostTotal((int) result[3]);
	        dto.setHired((int) result[4]);
	        dto.setTotalCandidates( (long) result[5]); // Make sure this is appropriate
	        
	        dtos.add(dto);
	        System.err.println(result[1].toString() + result[2].toString());
	    }
	    return dtos;
	}
	

	

	@ModelAttribute("candidate")
	public CandidateDto getCandidateDto() {
		return new CandidateDto();
	}

//	@GetMapping("/job-details")
//    public String showJobDetails() {
//        return "job-details";
//    }

	@PostMapping(value = "/apply-job", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Candidate> applyJob(CandidateDto candidateDto) throws IOException{
	System.err.println("MMMMMMMMMMMMMMMMMMMMMMMMMM");

		Candidate candidate=candidateService.saveCandidate(candidateDto);
	return new ResponseEntity<>(candidate, HttpStatus.OK);
	}

	@GetMapping("/view-summaryinfo")
	public String ViewSummaryInfo(Model model) {
		java.util.List<Summary> summaries = summaryService.getAllSummarys();
		model.addAttribute("listsummaryinfo", summaries);
		return "view-summaryinfo";

	}

//	 @PostMapping("/downloadFile")
//	    public void downloadFile(@RequestBody Long ids,HttpServletResponse response) throws IOException {
//		 Optional<Candidate> result=repo.findById(ids);
//		 if(!result.isPresent()) {
//			 System.err.println("could nt download");
//		 }
//		 Candidate can=result.get();
//		 response.setContentType("application/octet-stream");
//		 String headerKey="Content-Disposition";
//		 String headerValue="attachment;filename"+can.getSummary().getName();
//		 response.setHeader(headerKey, headerValue);
//
//		 ServletOutputStream output=response.getOutputStream();
//		 output.write(can.getResume());
//		 output.close();
//
//	 }

	@GetMapping("/post")
	@ResponseBody
	public List<VacancyDto> getAllPost(){
		List<VacancyDto> vacancyDto=vacancyInfoService.selectAllVacancyInfo();
		return vacancyDto;
	}


//	  @GetMapping("/downloadFile")
//	  public ResponseEntity<StreamingResponseBody> getFile(@RequestParam List<Long> id) {
//				System.err.println(id);
//			 List<Candidate> fileEntities = candidateImpl.getFile(id);
//		        if (!fileEntities.isEmpty()) {
//					if(fileEntities.size()==1){
//						Candidate fileEntity = fileEntities.get(0);
//
//					HttpHeaders headers = new HttpHeaders();
//					headers.setContentDispositionFormData("attachment",
//							fileEntity.getSummary().getName() + "." + getFileExtension(fileEntity.getType()));
//					headers.setContentType(MediaType.parseMediaType(fileEntity.getType()));
//
//					return ResponseEntity.ok().headers(headers)
//							.body(outputStream -> {
//								 byte[] decodedResume = Base64.getDecoder().decode(fileEntity.getResume());
//								outputStream.write(decodedResume);
//							});
//
//					} else {
//						// multiple candidates, create a zip file
//						StreamingResponseBody responseBody = outputStream -> {
//							try (ZipOutputStream zos = new ZipOutputStream(outputStream)) {
//								for (Candidate fileEntity : fileEntities) {
//									byte[] fileContent =  Base64.getDecoder().decode(fileEntity.getResume());
//									String fileName = generateUniqueFileName(fileEntity.getSummary().getName() + "-cv.",
//											fileEntity.getType());
//
//									ZipEntry zipEntry = new ZipEntry(fileName);
//									zos.putNextEntry(zipEntry);
//									zos.write(fileContent);
//									zos.closeEntry();
//								}
//
//								zos.finish();
//							} catch (IOException e) {
//								throw new RuntimeException("Error while streaming file", e);
//							}
//						};
//
//						HttpHeaders headers = new HttpHeaders();
//						headers.setContentDispositionFormData("attachment", "candidate.zip");
//						headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
//
//						return new ResponseEntity<>(responseBody, headers, HttpStatus.OK);
//					}
//				}
//
//				return ResponseEntity.notFound().build();
//			}
//
//		    private String generateUniqueFileName(String originalFileName, String fileType) {
//		        // Appending a unique identifier (UUID) to the original file name to avoid duplicates
//		        String uniqueIdentifier = UUID.randomUUID().toString().substring(0, 8);
//		        return originalFileName + "_" + uniqueIdentifier + "." + getFileExtension(fileType);
//		    }
//		    private String getFileExtension(String contentType) {
//		        switch (contentType) {
//		            case "application/msword":
//		                return "doc";
//		            case "application/vnd.openxmlformats-officedocument.wordprocessingml.document":
//		                return "docx";
//		            case "application/pdf":
//		                return "pdf";
//		            case "image/png":
//		                return "png";
//		            case "image/jpg":
//		               return "jpg";
//
//		            default:
//		                return "txt"; // Default to txt if the content type is unknown
//		        }
//		    }

	  @GetMapping("/downloadFile")
	    public ResponseEntity<Resource> getFile(@RequestParam List<Long> id) {
	        List<Candidate> fileEntities = candidateService.getFile(id);

	        if (!fileEntities.isEmpty()) {

	        	if (fileEntities.size() == 1) {
	        	    Candidate fileEntity = fileEntities.get(0);

	        	    HttpHeaders headers = new HttpHeaders();
	        	    headers.setContentDispositionFormData("attachment",
	        	            fileEntity.getSummary().getName() + "." + getFileExtension(fileEntity.getType()));
	        	    headers.setContentType(MediaType.parseMediaType(fileEntity.getType()));

	        	    byte[] decodedResume = Base64.getDecoder().decode(fileEntity.getResume());
	        	    ByteArrayResource resource = new ByteArrayResource(decodedResume);

	        	    return ResponseEntity.ok()
	        	            .headers(headers)
	        	            .contentLength(decodedResume.length)
	        	            .body(resource);
	        	}
	        		else {
	                // Multiple candidates, create a zip file
	        			try {
	        				System.err.println("vvvvvvv");
	        				 byte[]  zipBytes = generateZipBytes(fileEntities);
	     	                ByteArrayResource resource = new ByteArrayResource(zipBytes);

	     	                HttpHeaders headers = new HttpHeaders();
	     	                headers.setContentDispositionFormData("attachment", "candidates.zip");
	     	                headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

	     	                return ResponseEntity.ok()
	     	                        .headers(headers)
	     	                        .contentLength(zipBytes.length)
	     	                        .body(resource);
	        			}catch (IllegalArgumentException e) {
	        			    // Handle decoding error
	        			    e.printStackTrace();
	        			}

	            }
	        }

	        return ResponseEntity.notFound().build();
	    }

	    private byte[] generateZipBytes(List<Candidate> fileEntities) {
	        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
	             ZipOutputStream zos = new ZipOutputStream(baos)) {

	            for (Candidate candidate : fileEntities) {
	            	byte[]  fileContent = Base64.getDecoder().decode(candidate.getResume());
	                String fileName = generateUniqueFileName(candidate.getSummary().getName() + "-cv.",
	                        candidate.getType());

	                ZipEntry zipEntry = new ZipEntry(fileName);
	                zos.putNextEntry(zipEntry);
	                zos.write(fileContent);
	                zos.closeEntry();
	            }

	                zos.finish();

	            return baos.toByteArray();
	        } catch (IOException e) {
	            throw new RuntimeException("Error while generating zip file", e);
	        }
	    }
	    private String generateUniqueFileName(String originalFileName, String fileType) {
	        // Appending a unique identifier (UUID) to the original file name to avoid duplicates
	        String uniqueIdentifier = UUID.randomUUID().toString().substring(0, 8);
	        return originalFileName + "_" + uniqueIdentifier + "." + getFileExtension(fileType);
	    }
	    private String getFileExtension(String contentType) {
	        switch (contentType) {
	            case "application/msword":
	                return "doc";
	            case "application/vnd.openxmlformats-officedocument.wordprocessingml.document":
	                return "docx";
	            case "application/pdf":
	                return "pdf";
	            case "image/png":
	                return "png";
	            case "image/jpg":
	               return "jpg";

	            default:
	                return "txt"; // Default to txt if the content type is unknown
	        }
	    }
}
