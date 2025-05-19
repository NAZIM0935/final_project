package com.example.candidateonboardingsystem.service;


import com.example.candidateonboardingsystem.dto.CandidateDocumentDTO;
import com.example.candidateonboardingsystem.model.CandidateDocument;
import com.example.candidateonboardingsystem.model.CandidateModel;
import com.example.candidateonboardingsystem.repository.CandidateDocumentRepository;
import com.example.candidateonboardingsystem.repository.CandidateModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CandidateDocumentService {

    @Autowired
    private CandidateDocumentRepository candidateDocumentRepository;

    @Autowired
    private CandidateModelRepository candidateRepository;

    public void addDocument(Long candidateId, CandidateDocumentDTO candidateDocumentDto) {
        CandidateModel candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new RuntimeException("Candidate not found with id: " + candidateId));

        CandidateDocument candidateDocument = new CandidateDocument();
        candidateDocument.setFileUrl(candidateDocumentDto.getFileUrl());
        candidateDocument.setDocumentType(candidateDocumentDto.getDocumentType());
        candidateDocument.setVerified(candidateDocumentDto.isVerified());
        candidateDocument.setCandidate(candidate);

        candidateDocumentRepository.save(candidateDocument);
    }
}