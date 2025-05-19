package com.example.candidateonboardingsystem.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Data
public class JobOfferNotificationDTO {
    @NotNull(message = "Candidate ID is required")
    private Long candidateId;

    @NotNull(message = "Candidate email is required")
    @Email(message = "Email should be valid")
    private String email;

    public JobOfferNotificationDTO(Long candidateId, String email) {
        this.candidateId = candidateId;
        this.email = email;
    }

}
