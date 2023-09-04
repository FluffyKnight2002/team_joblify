package com.ace_inspiration.team_joblify.service_implement;

import com.ace_inspiration.team_joblify.dto.EmailTemplateDto;
import com.ace_inspiration.team_joblify.entity.Otp;
import com.ace_inspiration.team_joblify.entity.User;
import com.ace_inspiration.team_joblify.repository.InterviewRepository;
import com.ace_inspiration.team_joblify.repository.OtpRepository;
import com.ace_inspiration.team_joblify.repository.UserRepository;
import com.ace_inspiration.team_joblify.service.EmailService;
import com.ace_inspiration.team_joblify.service.candidate_service.CandidateService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImplement implements EmailService {
	private final CandidateService candidateService;
    private final UserRepository userRepository;
    private final OtpRepository otpRepository;
    private final JavaMailSender javaMailSender;
    private final ResourceLoader resourceLoader;
    private final InterviewRepository inter;


    @Override
    public void sendForgetPasswordEmail(String to) {

        String otp = UUID.randomUUID().toString().substring(0, 6).toUpperCase();

        String templateId = "1"; // Your desired template ID
        String templateContent = getEmailTemplateById(templateId);
        User user = userRepository.findByEmail(to).orElseThrow(()-> new NoSuchElementException("Email Not Found."));

            // Replace placeholders with actual data
            templateContent = templateContent.replace("{Name}", user.getName());
            templateContent = templateContent.replace("{OTP}", otp);
            templateContent = templateContent.replace("{Email}", to);

            // Now send the email using JavaMail
            MimeMessage message = javaMailSender.createMimeMessage();
            try {
                MimeMessageHelper helper = new MimeMessageHelper(message, true);
                helper.setFrom(new InternetAddress("ak4312040@gmail.com", "CZe"));
                helper.setTo(to);
                helper.setSubject("OTP for Forget Password");
                helper.setText(templateContent, true);

                javaMailSender.send(message);
                LocalDateTime now = LocalDateTime.now().plusMinutes(3);
                Otp o = Otp.builder()
                        .code(otp)
                        .expiredDate(now)
                        .user(user)
                        .build();
                otpRepository.save(o);

            } catch (MessagingException e) {
                log.error("Error sending email: {}", e.getMessage());
                // Handle any email sending errors here
            } catch (UnsupportedEncodingException e) {
                log.error("Unsupported encoding: {}", e.getMessage());
            }
        }

    @Override
    public boolean sendJobOfferEmail(EmailTemplateDto emailTemplateDto) {
        String templateId = "2"; // Your desired template ID
        String templateContent = getEmailTemplateById(templateId);

        // Replace placeholders with actual data
        templateContent = templateContent.replace("{Content}", emailTemplateDto.getContent());
        templateContent = templateContent.replace("{Email}", emailTemplateDto.getTo());

        // Now send the email using JavaMail
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            if(emailTemplateDto.getCcmail().length <= 0) {

                MimeMessageHelper helper = new MimeMessageHelper(message, true);
                helper.setFrom(new InternetAddress("ak4312040@gmail.com", "CZe"));
                helper.setTo(emailTemplateDto.getTo());
                helper.setSubject(emailTemplateDto.getSubject());
                helper.setText(templateContent, true);

                javaMailSender.send(message);
                return true;
            }
            else if(emailTemplateDto.getDate() == null && emailTemplateDto.getTime()==null && emailTemplateDto.getType()==null && emailTemplateDto.getStatus()==null){
//                String[] ccmail = emailTemplateDto.getCcmail().split(",");
                for (String email: emailTemplateDto.getCcmail()){
                    System.err.println(email);
                }
                MimeMessageHelper helper = new MimeMessageHelper(message, true);
                helper.setFrom(new InternetAddress("ak4312040@gmail.com", "CZe"));
                helper.setCc(emailTemplateDto.getCcmail());
                helper.setTo(emailTemplateDto.getTo());
                helper.setSubject(emailTemplateDto.getSubject());
                helper.setText(templateContent, true);

                javaMailSender.send(message);
                return true;
            }
            else{
//                String[] ccmail = emailTemplateDto.getCcmail().split(",");
                for (String email: emailTemplateDto.getCcmail()){
                    System.err.println(email);
                }

                MimeMessageHelper helper = new MimeMessageHelper(message, true);
                helper.setFrom(new InternetAddress("ak4312040@gmail.com", "CZe"));
                helper.setCc(emailTemplateDto.getCcmail());
                helper.setTo(emailTemplateDto.getTo());
                helper.setSubject(emailTemplateDto.getSubject());
                helper.setText(templateContent, true);

                javaMailSender.send(message);
                return true;
            }



        
        } catch (MessagingException e) {
            log.error("Error sending email: {}", e.getMessage());
            return false;
        } catch (UnsupportedEncodingException e) {
            log.error("Unsupported encoding: {}", e.getMessage());
            return false;
        }
      
    }

    @Override
    public boolean sendDirectMail(String senderEmail, String recipientEmail, String subject, String text) {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = null;

        try {
            helper = new MimeMessageHelper(message, true);
            helper.setTo(new InternetAddress(recipientEmail));
            helper.setFrom(senderEmail);
            helper.setSubject(subject);
            helper.setText(text, true); // Use true to indicate HTML content

            javaMailSender.send(message);
            return true;
        } catch (MessagingException e) {
            // Handle the exception, log it, or return false
            e.printStackTrace(); // Log the exception for debugging purposes
            return false; // Return false to indicate that the email was not sent successfully
        } finally {
            if (helper != null) {
                try {
                    helper.getMimeMessage().setContent(null); // Clear the content to prevent memory leaks
                } catch (MessagingException e) {
                    e.printStackTrace(); // Handle or log this exception as well
                }
            }
        }
    }



    public String getEmailTemplateById(String templateId) {
        String templateContent = null;

        try {
            Resource resource = resourceLoader.getResource("classpath:email_templates.xml");
            InputStream inputStream = resource.getInputStream();

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            dbFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            dbFactory.setFeature("http://xml.org/sax/features/external-general-entities", false);
            dbFactory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            dbFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            dbFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
            dbFactory.setExpandEntityReferences(false);

            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputStream);
            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("template");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String id = element.getAttribute("id");
                    if (id.equals(templateId)) {
                        templateContent = element.getElementsByTagName("content").item(0).getTextContent();
                        break;
                    }
                }
            }
        } catch (Exception e) {
            // Handle any parsing or IO errors here
            log.error("Error Get Getting Email Template: {}", e.getMessage());
        }

        return templateContent;
    }
}

