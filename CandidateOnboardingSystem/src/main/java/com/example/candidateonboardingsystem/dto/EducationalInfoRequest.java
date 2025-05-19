package com.example.candidateonboardingsystem.dto;

import lombok.Data;

@Data
public class EducationalInfoRequest {
    private String degree;
    private String institution;
    private int passingYear;
    private Long candidateId;
}
