package com.example.Career;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class FastTagController {

    @Autowired
    WhatsAppService whatsAppService;

    @GetMapping("/upload")
    public String getUploadPage() {
        return "index";
    }

    @PostMapping("/upload")
    public ResponseEntity<String> handleFileUpload(@RequestParam("email") String email,
                                                   @RequestParam("mobileNumber") String mobileNumber,
                                                   @RequestParam("pdf") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Please upload a PDF file.");
        }

        // Save the PDF file to the server
        String fileName = file.getOriginalFilename();
        String filePath = "D:/cloudfiles/" + fileName; // Update this path to where you want to save the file

        try {
            file.transferTo(new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Failed to save the file.");
        }

        // Encode the file name for the media URL
        String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8);
        String mediaUrl = "http://localhost:5000/files/" + encodedFileName; // Ensure this URL is accessible

        // Log the media URL for debugging
        System.out.println("Media URL: " + mediaUrl);

        // Send WhatsApp message with the PDF file
        String toWhatsAppNumber = "+917339356493"; // Assuming mobile number includes country code and is WhatsApp-enabled
        String message = "Email: " + email + "\nMobile Number: " + mobileNumber;

        try {
            whatsAppService.sendWhatsAppMessageWithMedia(toWhatsAppNumber, message, mediaUrl);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Failed to send WhatsApp message.");
        }

        return ResponseEntity.ok("File uploaded successfully and WhatsApp message sent.");
    }
}
