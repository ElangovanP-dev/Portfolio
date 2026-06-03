package com.elangovan.portfolio.service;

import com.elangovan.portfolio.dto.request.CertificationRequest;
import com.elangovan.portfolio.dto.response.ProfileResponse.CertificationResponse;
import com.elangovan.portfolio.entity.Certification;
import com.elangovan.portfolio.entity.User;
import com.elangovan.portfolio.exception.ResourceNotFoundException;
import com.elangovan.portfolio.repository.CertificationRepository;
import com.elangovan.portfolio.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CertificationService {

    private final CertificationRepository certificationRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<CertificationResponse> getAllCertifications() {
        User user = getFirstUser();
        return certificationRepository.findByUserIdOrderByIssueDateDesc(user.getId())
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public CertificationResponse createCertification(String email, CertificationRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        Certification cert = Certification.builder()
                .user(user)
                .certName(request.getCertName())
                .issuer(request.getIssuer())
                .issueDate(request.getIssueDate())
                .expiryDate(request.getExpiryDate())
                .credentialUrl(request.getCredentialUrl())
                .build();

        return mapToResponse(certificationRepository.save(cert));
    }

    @Transactional
    public void deleteCertification(Long id) {
        if (!certificationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Certification", "id", id);
        }
        certificationRepository.deleteById(id);
    }

    private User getFirstUser() {
        return userRepository.findAll().stream()
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("No user found"));
    }

    private CertificationResponse mapToResponse(Certification c) {
        return CertificationResponse.builder()
                .id(c.getId())
                .certName(c.getCertName())
                .issuer(c.getIssuer())
                .issueDate(c.getIssueDate() != null ? c.getIssueDate().toString() : null)
                .expiryDate(c.getExpiryDate() != null ? c.getExpiryDate().toString() : null)
                .credentialUrl(c.getCredentialUrl())
                .build();
    }
}
