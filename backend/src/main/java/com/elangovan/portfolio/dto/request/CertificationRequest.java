package com.elangovan.portfolio.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CertificationRequest {

    @NotBlank(message = "Certification name is required")
    private String certName;

    private String issuer;

    private LocalDate issueDate;

    private LocalDate expiryDate;

    private String credentialUrl;
}
