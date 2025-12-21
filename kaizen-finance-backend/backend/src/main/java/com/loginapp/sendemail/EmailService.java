package com.loginapp.sendemail;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;

@Service
public class EmailService {
    
    @Value("${email.service.provider:gmail}")
    private String emailProvider;
    
    @Value("${email.sendgrid.api.key:}")
    private String sendgridApiKey;
    
    @Value("${email.sendgrid.from.email:}")
    private String sendgridFromEmail;
    
    @Value("${email.sendgrid.from.name:Kaizen Solution}")
    private String sendgridFromName;
    
    @Value("${email.brevo.api.key:}")
    private String brevoApiKey;
    
    @Value("${email.brevo.from.email:}")
    private String brevoFromEmail;
    
    @Value("${email.brevo.from.name:Kaizen Solution}")
    private String brevoFromName;
    
    @Value("${email.mailgun.api.key:}")
    private String mailgunApiKey;
    
    @Value("${email.mailgun.domain:}")
    private String mailgunDomain;
    
    @Value("${email.mailgun.from.email:}")
    private String mailgunFromEmail;
    
    @Value("${email.mailgun.from.name:Kaizen Solution}")
    private String mailgunFromName;
    
    private final WebClient webClient;
    
    public EmailService() {
        this.webClient = WebClient.builder()
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();
    }
    
    public void sendEmail(String toEmail, String subject, String textContent) {
        switch (emailProvider.toLowerCase()) {
            case "sendgrid":
                sendViaSendGrid(toEmail, subject, textContent);
                break;
            case "brevo":
                sendViaBrevo(toEmail, subject, textContent);
                break;
            case "mailgun":
                sendViaMailgun(toEmail, subject, textContent);
                break;
            case "gmail":
            default:
                // Gmail 使用 JavaMailSender，这里不处理
                throw new UnsupportedOperationException("Gmail should use JavaMailSender");
        }
    }
    
    private void sendViaSendGrid(String toEmail, String subject, String textContent) {
        if (sendgridApiKey == null || sendgridApiKey.isEmpty()) {
            throw new IllegalStateException("SendGrid API key is not configured");
        }
        
        Map<String, Object> emailData = new HashMap<>();
        emailData.put("personalizations", new Object[]{
            Map.of("to", new Object[]{
                Map.of("email", toEmail)
            })
        });
        emailData.put("from", Map.of(
            "email", sendgridFromEmail,
            "name", sendgridFromName
        ));
        emailData.put("subject", subject);
        emailData.put("content", new Object[]{
            Map.of(
                "type", "text/plain",
                "value", textContent
            )
        });
        
        try {
            String response = webClient.post()
                .uri("https://api.sendgrid.com/v3/mail/send")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + sendgridApiKey)
                .bodyValue(emailData)
                .retrieve()
                .bodyToMono(String.class)
                .block();
            
            System.out.println("Email sent via SendGrid to: " + toEmail);
        } catch (Exception e) {
            System.err.println("Error sending email via SendGrid: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to send email via SendGrid", e);
        }
    }
    
    private void sendViaBrevo(String toEmail, String subject, String textContent) {
        if (brevoApiKey == null || brevoApiKey.isEmpty()) {
            throw new IllegalStateException("Brevo API key is not configured");
        }
        
        Map<String, Object> sender = new HashMap<>();
        sender.put("name", brevoFromName);
        sender.put("email", brevoFromEmail);
        
        Map<String, Object> toRecipient = new HashMap<>();
        toRecipient.put("email", toEmail);
        
        Map<String, Object> emailData = new HashMap<>();
        emailData.put("sender", sender);
        emailData.put("to", new Object[]{toRecipient});
        emailData.put("subject", subject);
        emailData.put("textContent", textContent);
        
        try {
            webClient.post()
                .uri("https://api.brevo.com/v3/smtp/email")
                .header("api-key", brevoApiKey)
                .bodyValue(emailData)
                .retrieve()
                .bodyToMono(String.class)
                .block();
            
            System.out.println("Email sent via Brevo to: " + toEmail);
        } catch (Exception e) {
            System.err.println("Error sending email via Brevo: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to send email via Brevo", e);
        }
    }
    
    private void sendViaMailgun(String toEmail, String subject, String textContent) {
        if (mailgunApiKey == null || mailgunApiKey.isEmpty() || mailgunDomain == null || mailgunDomain.isEmpty()) {
            throw new IllegalStateException("Mailgun API key or domain is not configured");
        }
        
        try {
            String response = webClient.post()
                .uri("https://api.mailgun.net/v3/" + mailgunDomain + "/messages")
                .header(HttpHeaders.AUTHORIZATION, "Basic " + java.util.Base64.getEncoder()
                    .encodeToString(("api:" + mailgunApiKey).getBytes()))
                .bodyValue(Map.of(
                    "from", mailgunFromName + " <" + mailgunFromEmail + ">",
                    "to", toEmail,
                    "subject", subject,
                    "text", textContent
                ))
                .retrieve()
                .bodyToMono(String.class)
                .block();
            
            System.out.println("Email sent via Mailgun to: " + toEmail);
        } catch (Exception e) {
            System.err.println("Error sending email via Mailgun: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to send email via Mailgun", e);
        }
    }
}

