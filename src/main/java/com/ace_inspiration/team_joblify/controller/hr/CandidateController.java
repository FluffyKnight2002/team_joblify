package com.ace_inspiration.team_joblify.controller.hr;


import com.ace_inspiration.team_joblify.config.FirstDaySpecification;
import com.ace_inspiration.team_joblify.dto.CandidateDto;
import com.ace_inspiration.team_joblify.dto.CountDto;
import com.ace_inspiration.team_joblify.dto.SummaryDto;
import com.ace_inspiration.team_joblify.dto.VacancyDto;
import com.ace_inspiration.team_joblify.entity.*;
import com.ace_inspiration.team_joblify.repository.InterviewProcessRepository;
import com.ace_inspiration.team_joblify.repository.VacancyInfoRepository;
import com.ace_inspiration.team_joblify.service.AllPostService;
import com.ace_inspiration.team_joblify.service.OfferMailSendedService;
import com.ace_inspiration.team_joblify.service.PositionService;
import com.ace_inspiration.team_joblify.service.VacancyInfoService;
import com.ace_inspiration.team_joblify.service.candidate_service.CandidateService;
import com.ace_inspiration.team_joblify.service.candidate_service.SummaryService;
import com.ace_inspiration.team_joblify.service.hr_service.InterviewProcessService;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;


import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.ace_inspiration.team_joblify.dto.PindChartDto;
import com.ace_inspiration.team_joblify.entity.Position;
import com.ace_inspiration.team_joblify.entity.Summary;
import com.ace_inspiration.team_joblify.entity.AllPost;
import com.ace_inspiration.team_joblify.entity.Candidate;
import com.ace_inspiration.team_joblify.entity.InterviewProcess;

import lombok.RequiredArgsConstructor;


import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

@RestController
@RequiredArgsConstructor
public class CandidateController {

    @Value("${xml.file.path}") // Specify the path to the XML file in your application.properties
    private String xmlFilePath;

    private final CandidateService candidateService;

    private final SummaryService summaryService;

    private final AllPostService allPostService;

    private final PositionService positionService;

//    private final DasboardService dasboardservice;

    private  final InterviewProcessService interviewService;

    private final InterviewProcessRepository repo;

    private final VacancyInfoService vacancyInfoService;

    private final VacancyInfoRepository vanInfoReopository;

    private final OfferMailSendedService offerMailSendedService;

    private FirstDaySpecification firstDaySpecification;

    @GetMapping("/allCandidate")
    public DataTablesOutput<InterviewProcess> getDataTable(
            @RequestParam(required = false) String datePosted,
            @RequestParam(required = false) String startDateInput,
            @RequestParam(required = false) String endDateInput,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String department,
            @RequestParam(required = false) String jobType,
            @RequestParam(required = false) List<String> level,
            @RequestParam(required = false) String minAndMax,
            @RequestParam(required = false) String applicants,
            @RequestParam(required = false) String status,
           DataTablesInput input) {

        // Create a Specification using the DataTablesInput object
        Specification<InterviewProcess> specification = (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            // Inside your getDataTable method
            if (datePosted != null && !datePosted.isEmpty()) {
                LocalDate currentDate = LocalDate.now();
                LocalDate startDate = null;
                LocalDate endDate = null;

                System.out.println("Start Date Input : " + startDateInput);
                System.out.println("End Date Input : " + endDateInput);

                if (datePosted.equals("Last 24 hours")) {
                    // Calculate the start date as 1 day ago from the current date
                    startDate = currentDate.minusDays(1);
                } else if (datePosted.equals("Last week")) {
                    // Calculate the start date as 7 days ago from the current date
                    startDate = currentDate.minusDays(7);
                } else if (datePosted.equals("Last month")) {
                    // Calculate the start date as 30 days ago from the current date
                    startDate = currentDate.minusDays(30);
                } else if (datePosted.equals("Custom")) {
                    // Check if both startDateInput and endDateInput are provided
                    if (startDateInput != null && endDateInput != null) {
                        // Parse the start and end dates into LocalDate objects
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

                        try {
                            startDate = LocalDate.parse(startDateInput, formatter);
                            endDate = LocalDate.parse(endDateInput, formatter);
                        } catch (DateTimeParseException e) {
                            // Handle date parsing error
                        }
                    }
                }

                // Now, you can use the startDate and endDate to filter your data
                if (startDate != null) {
                    // Add filter condition for the start date
                    predicate = criteriaBuilder.and(
                            predicate, criteriaBuilder.greaterThanOrEqualTo(root.get("openDate"), startDate)
                    );
                }

                if (endDate != null) {
                    // Add filter condition for the end date
                    predicate = criteriaBuilder.and(
                            predicate, criteriaBuilder.lessThanOrEqualTo(root.get("openDate"), endDate)
                    );
                }

                System.out.println("Start Date : " + startDate);
                System.out.println("End Date : " + endDate);
            }if (title != null && !title.isEmpty()) {
                // Add filter condition for title
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("position"), title));
            }
            if (department != null && !department.isEmpty()) {
                // Add filter condition for department
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("department"), department));
            }
            if (jobType != null && !jobType.isEmpty()) {
                String adjustedJobType = jobType.toUpperCase().replace(" ", "_");
                // Correct job type filtering with a list of values
                predicate = criteriaBuilder.and(predicate, root.get("jobType").in(JobType.valueOf(adjustedJobType)));
            }
            if (level != null && level.size() > 0) {
                List<Predicate> levelPredicates = new ArrayList<>();
                level.forEach(lvl -> {
                    String adjustedLevel = lvl.toUpperCase().replace(" ", "_");
                    levelPredicates.add(root.get("level").in(Level.valueOf(adjustedLevel)));
                });

                // Combine all level predicates using OR
                Predicate levelPredicate = criteriaBuilder.or(levelPredicates.toArray(new Predicate[0]));

                // Add the combined level predicate to the overall predicate using AND
                predicate = criteriaBuilder.and(predicate, levelPredicate);
            }
            if (minAndMax != null && !minAndMax.isEmpty()) {
                // Correct salary filtering within a range
                String[] salaryRange = minAndMax.split(",");
                if (salaryRange.length == 2) {
                    double minSalary = Double.parseDouble(salaryRange[0]);
                    double maxSalary = Double.parseDouble(salaryRange[1]);
                    predicate = criteriaBuilder.and(predicate,
                            criteriaBuilder.between(root.get("salary"), minSalary, maxSalary));
                }
            }
            if (applicants != null && !applicants.isEmpty()) {
                Predicate applicantsPredicate = null;

                if (applicants.equals("Over require")) {
                    // Apply filter condition for "Over require"
                    applicantsPredicate = criteriaBuilder.greaterThanOrEqualTo(root.get("applicants"), root.get("post"));
                } else {
                    // Apply filter condition for "Doesn't reach half"
                    applicantsPredicate = criteriaBuilder.lessThanOrEqualTo(root.get("applicants"), root.get("post"));
                }

                // Add the applicantsPredicate to the main predicate with 'AND'
                predicate = criteriaBuilder.and(predicate, applicantsPredicate);
            }
            if (status != null && !status.isEmpty()) {
                // Add filter condition for status
                String adjustedStatus = status.toUpperCase().replace(" ", "_");
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("status"), Status.valueOf(adjustedStatus)));
            }

            return predicate;
        };

        // Use the Specification to filter data
        DataTablesOutput<InterviewProcess> output = repo.findAll(input, specification);

        return output;
    }


    @GetMapping("/allPositions")
    @ResponseBody
    public List<Position> getAllPosition() {
        List<Position> position = positionService.selectAllPosition();
        return position;
    }

    @PostMapping("/changStatus")
    public void changeStatus(@RequestBody long id) {
        candidateService.changeStatus(id);
    }

    @PostMapping("/seeMore")
    @ResponseBody
    public SummaryDto updateStatus(@RequestBody long id) {
        SummaryDto summaryDto = candidateService.findByid(id);
        return summaryDto;
    }

    @PostMapping("/changeInterview")
    @ResponseBody
    public ResponseEntity<?> changeInterview(@RequestParam("id") long id, @RequestParam("status") String status) {

        candidateService.changeInterviewstatus(id, status);
        return ResponseEntity.ok("okokok");
    }

    @GetMapping("/process")
    @ResponseBody
    public DataTablesOutput<AllPost> interviewProcess(DataTablesInput input) {
        DataTablesOutput<AllPost> allpost = allPostService.getAll(input);
        return allpost;
    }

    @GetMapping("/getYear")
    public List<Object[]> getyear(){
    	   List<Object[]> year=vanInfoReopository.getYear();
    	   return year;
    }


    @PostMapping ("/dashboard")
    public List<CountDto> getCounts(@RequestParam("timeSession")String year) {
        String starDate=year+"-01-01";
        String endDate=year+"-12-31";

        List<Object[]> results = vanInfoReopository.getVacancyInfoWithCandidateCounts(starDate,endDate);
        List<CountDto> dtoList = new ArrayList<>();

        for (Object[] resultRow : results) {
            CountDto dto = new CountDto(
                (String) resultRow[0],
                (String) resultRow[1],
                (String) resultRow[2],
                (String) resultRow[3]
            );
            dtoList.add(dto);
        }


        return dtoList;
    }
    @GetMapping("/chart")
    public PindChartDto pindChart(){
    	  LocalDate postDate = LocalDate.parse("2023-08-27");
    	PindChartDto pind =allPostService.findByOpenDate(postDate);
    	System.out.println(pind.getInterviewed());
        return pind;
    }


    @ModelAttribute("candidate")
    public CandidateDto getCandidateDto() {
        return new CandidateDto();
    }


    @PostMapping("/update-xml-content")
    public ResponseEntity<String> updateXmlContent(@RequestBody String updatedContent) {
        try {
            Resource resource = new ClassPathResource("email.xml");
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(resource.getInputStream());

            Element contentElement = findElementByTagName(document.getDocumentElement(), "content");

            if (contentElement != null) {
                contentElement.setTextContent(updatedContent);

                FileOutputStream outputStream = new FileOutputStream(resource.getFile());
                Transformer transformer = TransformerFactory.newInstance().newTransformer();
                transformer.transform(new DOMSource(document), new StreamResult(outputStream));
                outputStream.close();
                System.err.println("sdfzdfgd");
                return ResponseEntity.ok("XML content updated successfully");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to locate <content> element in XML");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update XML content");
        }
    }

    private Element findElementByTagName(Element element, String tagName) {
        if (element.getTagName().equals(tagName)) {
            return element;
        }

        Element foundElement = null;
        for (int i = 0; i < element.getChildNodes().getLength(); i++) {
            if (element.getChildNodes().item(i) instanceof Element) {
                foundElement = findElementByTagName((Element) element.getChildNodes().item(i), tagName);
                if (foundElement != null) {
                    break;
                }
            }
        }
        return foundElement;
    }


    @PostMapping("/apply")
    public boolean submitJobDetail(@ModelAttribute("candidate") CandidateDto dto) {
        Candidate candidate = candidateService.saveCandidate(dto);
        if(candidate == null) {
            return false;
        }
        return true;
    }

    @PostMapping(value = "/apply-job", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public boolean applyJob(CandidateDto candidateDto) throws IOException {
        System.err.println("MMMMMMMMMMMMMMMMMMMMMMMMMM");
        System.out.println(candidateDto.getLanguageSkillsString());
        System.out.println(candidateDto.getTechSkillsString());
        // Split the comma-separated techSkillsString into a string array
        String[] techSkillsArray = candidateDto.getTechSkillsString().split(",");

        // Set the techSkills array in your CandidateDto
        candidateDto.setTechSkills(techSkillsArray);

        // Similarly, do the same for languageSkillsString
        String[] languageSkillsArray = candidateDto.getLanguageSkillsString().split(",");

        candidateDto.setLanguageSkills(languageSkillsArray);

        System.out.println(candidateDto);

        Candidate candidate = candidateService.saveCandidate(candidateDto);
        if (candidate == null) {
            return false;
        }
        return true;
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
    public List<VacancyDto> getAllPost() {
        List<VacancyDto> vacancyDto = vacancyInfoService.selectAllVacancyInfo();
        return vacancyDto;
    }
    @GetMapping("/reject")
    public String handleReject(@RequestParam("id") String id) {
        System.err.println("NOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO"+id);


        return "/thank-you"; // Redirect to a thank-you page or appropriate location
    }

    @GetMapping("/accept")
    public String handleAccept(@RequestParam("id") String id) {
        System.err.println("YESSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS"+id);

        return "/thank-you"; // Redirect to a thank-you page or appropriate location
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
            } else {
                // Multiple candidates, create a zip file
                try {
                    System.err.println("vvvvvvv");
                    byte[] zipBytes = generateZipBytes(fileEntities);
                    ByteArrayResource resource = new ByteArrayResource(zipBytes);

                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentDispositionFormData("attachment", "candidates.zip");
                    headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

                    return ResponseEntity.ok()
                            .headers(headers)
                            .contentLength(zipBytes.length)
                            .body(resource);
                } catch (IllegalArgumentException e) {
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
                byte[] fileContent = Base64.getDecoder().decode(candidate.getResume());
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
