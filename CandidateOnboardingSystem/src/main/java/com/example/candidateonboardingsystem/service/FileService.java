package com.example.candidateonboardingsystem.service;

import com.example.candidateonboardingsystem.entity.FileEntity;
import com.example.candidateonboardingsystem.model.CandidateModel;
import com.example.candidateonboardingsystem.repository.CandidateModelRepository;

import com.example.candidateonboardingsystem.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class FileService {
    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private CandidateModelRepository candidateRepository;

    public FileEntity storeFile(MultipartFile file , Long candidateId) throws IOException {
        CandidateModel candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new RuntimeException("Candidate not found"));

        FileEntity fileEntity = new FileEntity();
        fileEntity.setFileName(file.getOriginalFilename());
        fileEntity.setFiletype(file.getContentType());
        fileEntity.setData(file.getBytes());
        fileEntity.setCandidate(candidate);

        return fileRepository.save(fileEntity);
    }
}