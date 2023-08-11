package com.ace_inspiration.team_joblify.service_implement;

import com.ace_inspiration.team_joblify.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

@Service
@RequiredArgsConstructor
public class EmailServiceImplement implements EmailService {

    private final JavaMailSender javaMailSender;
    private final ResourceLoader resourceLoader;


    @Override
    public void sendForgetPasswordEmail(String to, String name, String otp) {
        String templateId = "1"; // Your desired template ID
        String templateContent = getEmailTemplateById(templateId);

            // Replace placeholders with actual data
            templateContent = templateContent.replace("{Name}", name);
            templateContent = templateContent.replace("{OTP}", otp);
            templateContent = templateContent.replace("{Email}", to);

            // Now send the email using JavaMail
            MimeMessage message = javaMailSender.createMimeMessage();
            try {
                String[] ccmail= new String[2];
                ccmail[0]="cze1122121@gmail.com";
                ccmail[1]="selfiebrotjers1500@gmail.com";

                MimeMessageHelper helper = new MimeMessageHelper(message, true);
                helper.setFrom(new InternetAddress("ak4312040@gmail.com", "CZe"));
                helper.setCc(ccmail);
                helper.setTo(to);
                helper.setSubject("OTP for Forget Password");
                helper.setText(templateContent, true);

                javaMailSender.send(message);
            } catch (MessagingException e) {
                e.printStackTrace();
                // Handle any email sending errors here
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }

    @Override
    public void sendJobOfferEmail(String to, String content) {
        String templateId = "2"; // Your desired template ID
        String templateContent = getEmailTemplateById(templateId);

        // Replace placeholders with actual data
        templateContent = templateContent.replace("{Content}", content);
        templateContent = templateContent.replace("{Email}", to);

        // Now send the email using JavaMail
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            String[] ccmail= new String[2];
            ccmail[0]="cze1122121@gmail.com";
            ccmail[1]="selfiebrotjers1500@gmail.com";

            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(new InternetAddress("ak4312040@gmail.com", "CZe"));
            helper.setCc(ccmail);
            helper.setTo(to);
            helper.setSubject("JII");
            helper.setText(templateContent, true);

            javaMailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
            // Handle any email sending errors here
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
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
            e.printStackTrace();
        }

        return templateContent;
    }
}

