package com.example.candidateonboardingsystem.controller;

import com.example.candidateonboardingsystem.dto.JobOfferNotificationDTO;
import com.example.candidateonboardingsystem.service.JobOfferService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/job-offer")
public class JobOfferNotificationController {

    @Autowired
    private JobOfferService jobOfferService;

    @PostMapping("/notify-candidates")
    public ResponseEntity<String> sendNotification(@RequestBody @Valid JobOfferNotificationDTO dto) {
        boolean notified = jobOfferService.notifyCandidate(dto);
        if (notified) {
            return ResponseEntity.ok("Notification sent to: " + dto.getEmail());
        } else {
            return ResponseEntity.badRequest()
                    .body("Candidate not eligible for notification or error occurred");
        }
    }
}
