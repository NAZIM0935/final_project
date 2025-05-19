package com.example.candidateonboardingsystem.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CandidateDocumentDTO {
    @NotBlank(message = "Document type is required")
    private String documentType;

    @NotBlank(message = "File URL is required")
    private String fileUrl;


    private boolean verified;
}
