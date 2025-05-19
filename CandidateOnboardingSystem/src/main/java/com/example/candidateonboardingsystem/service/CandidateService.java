package com.example.candidateonboardingsystem.service;

import com.example.candidateonboardingsystem.dto.BankInfoRequest;
import com.example.candidateonboardingsystem.dto.EducationalInfoRequest;
import com.example.candidateonboardingsystem.dto.EmailMessage;
import com.example.candidateonboardingsystem.dto.PersonalInfoRequest;
import com.example.candidateonboardingsystem.model.BankInfo;
import com.example.candidateonboardingsystem.model.CandidateDocument;
import com.example.candidateonboardingsystem.model.CandidateEducation;
import com.example.candidateonboardingsystem.model.CandidateModel;
import com.example.candidateonboardingsystem.repository.BankInfoRepository;
import com.example.candidateonboardingsystem.repository.CandidateDocumentRepository;
import com.example.candidateonboardingsystem.repository.CandidateEducationRepository;
import com.example.candidateonboardingsystem.repository.CandidateModelRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CandidateService {

    private final BankInfoRepository bankInfoRepository;
    private static final Logger logger = LoggerFactory.getLogger(CandidateService.class);

    private final CandidateModelRepository candidateModelRepository;
    private final CandidateEducationRepository educationRepository;
    private final CandidateDocumentRepository documentRepository;

    public CandidateService(CandidateModelRepository candidateModelRepository,
                            BankInfoRepository bankInfoRepository,
                            CandidateEducationRepository educationRepository,
                            CandidateDocumentRepository documentRepository) {
        this.candidateModelRepository = candidateModelRepository;
        this.bankInfoRepository = bankInfoRepository;
        this.educationRepository = educationRepository;
        this.documentRepository = documentRepository;
    }

    // Get all hired candidates
    public List<CandidateModel> getHiredCandidates() {
        logger.info("Fetching all candidates with status ONBOARDED");
        List<CandidateModel> candidates = candidateModelRepository.findByStatus(CandidateModel.Status.ONBOARDED);
        logger.info("Found {} hired candidates", candidates.size());
        return candidates;
    }

    // Get all candidates
    public List<CandidateModel> getAllCandidates() {
        logger.info("Fetching all candidates");
        List<CandidateModel> candidates = candidateModelRepository.findAll();
        logger.info("Found {} candidates", candidates.size());
        return candidates;
    }

    // Count candidates
    public long countCandidates() {
        long count = candidateModelRepository.count();
        logger.info("Total candidates count: {}", count);
        return count;
    }

    public boolean updateStatus(Long id, String status) {
        logger.info("Updating status for candidate ID {} to {}", id, status);
        Optional<CandidateModel> candidateOpt = candidateModelRepository.findById(id);
        if (candidateOpt.isPresent()) {
            CandidateModel candidate = candidateOpt.get();
            candidate.setStatus(CandidateModel.Status.valueOf(status));
            candidateModelRepository.save(candidate);
            logger.info("Status updated successfully for candidate ID {}", id);
            return true;
        }
        logger.warn("Candidate with ID {} not found for status update", id);
        return false;
    }

    public boolean updateOnboardingStatus(Long id, String onboardingStatus) {
        logger.info("Updating onboarding status for candidate ID {} to {}", id, onboardingStatus);
        Optional<CandidateModel> candidateOpt = candidateModelRepository.findById(id);
        if (candidateOpt.isPresent()) {
            CandidateModel candidate = candidateOpt.get();
            candidate.setOnboardingStatus(CandidateModel.OnboardingStatus.valueOf(onboardingStatus));
            candidateModelRepository.save(candidate);
            logger.info("Onboarding status updated successfully for candidate ID {}", id);
            return true;
        }
        logger.warn("Candidate with ID {} not found for onboarding status update", id);
        return false;
    }

    public boolean updatePersonalInfo(Long id, PersonalInfoRequest request) {
        logger.info("Updating personal info for candidate ID {}", id);
        Optional<CandidateModel> optionalCandidate = candidateModelRepository.findById(id);
        if (optionalCandidate.isPresent()) {
            CandidateModel candidate = optionalCandidate.get();
            candidate.setFirst_name(request.getFirstName());
            candidate.setLast_name(request.getLastName());
            candidate.setEmail(request.getEmail());
            candidate.setPhone_no(request.getPhoneNo());
            candidate.setUpdated_at(LocalDateTime.now());
            candidateModelRepository.save(candidate);
            logger.info("Personal info updated successfully for candidate ID {}", id);
            return true;
        }
        logger.warn("Candidate with ID {} not found for personal info update", id);
        return false;
    }

    public boolean updateBankInfo(Long id, BankInfoRequest request) {
        logger.info("Updating bank info for candidate ID {}", id);
        Optional<BankInfo> optionalBankInfo = bankInfoRepository.findByCandidateId(id);
        if (optionalBankInfo.isPresent()) {
            BankInfo bankInfo = optionalBankInfo.get();
            bankInfo.setBank_name(request.getBankName());
            bankInfo.setAccount_number(request.getAccountNumber());
            bankInfo.setIfsc_code(request.getIfscCode());
            bankInfoRepository.save(bankInfo);
            logger.info("Bank info updated successfully for candidate ID {}", id);
            return true;
        }
        logger.warn("Bank info not found for candidate ID {}", id);
        return false;
    }

    public boolean updateEducationalInfo(Long id, EducationalInfoRequest request) {
        logger.info("Updating educational info for candidate ID {}", id);

        Optional<CandidateEducation> optionalEducation = educationRepository.findByCandidateId(id);
        if (optionalEducation.isPresent()) {
            CandidateEducation education = optionalEducation.get();

            education.setDegree(request.getDegree());
            education.setInstitution(request.getInstitution());
            education.setPassingYear(request.getPassingYear());

            educationRepository.save(education);

            logger.info("Educational info updated successfully for candidate ID {}", id);
            return true;
        }

        logger.warn("Educational info not found for candidate ID {}", id);
        return false;
    }



    @Autowired
    private EmailProducer emailProducer;

    public void sendJobOfferEmail(String toEmail) {
        EmailMessage message = new EmailMessage();
        message.setTo(toEmail);
        message.setSubject("Congratulations! You have a Job Offer 🎉");
        message.setBody("Dear Candidate,\n\nYou have been selected. Please login to view your offer letter.\n\nRegards,\nTeam");

        emailProducer.sendEmailMessage(message);
    }


}
