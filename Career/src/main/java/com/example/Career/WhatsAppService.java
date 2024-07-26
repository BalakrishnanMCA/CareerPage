package com.example.Career;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class WhatsAppService {

    private String accountSid = "ACf298b69fff17e8d5d5f7ced271c0df91";
    private String authToken = "0a81ce428fff7becd3eed4d4d646d840";
    private String twilioPhoneNumber = "+14155238886";
    private Path uploadDir = Paths.get("uploads");

    public WhatsAppService() throws IOException {
        Twilio.init(accountSid, authToken);
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }
    }

    public String saveFile(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
        Path filePath = uploadDir.resolve(fileName);
        try (FileOutputStream fos = new FileOutputStream(filePath.toFile())) {
            fos.write(file.getBytes());
        }
        return filePath.toUri().toString(); // Return the URL to access the file
    }

    public void sendWhatsAppMessageWithMedia(String to, String message, String mediaUrl) {
        // Validate input parameters
        if (to == null || message == null || mediaUrl == null) {
            throw new IllegalArgumentException("To, message, and mediaUrl cannot be null");
        }

        try {
            // Log the input parameters for debugging
            System.out.println("Sending WhatsApp message");
            System.out.println("To: " + to);
            System.out.println("Message: " + message);
            System.out.println("Media URL: " + mediaUrl);

            // Create the URI object for the media URL
            URI mediaUri = new URI(mediaUrl);

            // Create and send the message
            Message.creator(
                            new PhoneNumber("whatsapp:" + to),
                            new PhoneNumber("whatsapp:" + twilioPhoneNumber),
                            message)
                    .setMediaUrl(mediaUri)
                    .create();

            System.out.println("WhatsApp message sent successfully");
        } catch (URISyntaxException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Invalid media URL", e);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to send WhatsApp message", e);
        }
    }

    public void deleteFile(String fileUrl) throws IOException {
        URI uri = URI.create(fileUrl);
        Path filePath = Paths.get(uri);
        Files.deleteIfExists(filePath);
    }
}
