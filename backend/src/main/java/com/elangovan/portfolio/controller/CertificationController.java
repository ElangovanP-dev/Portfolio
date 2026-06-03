package com.elangovan.portfolio.controller;

import com.elangovan.portfolio.dto.request.CertificationRequest;
import com.elangovan.portfolio.dto.response.ApiResponse;
import com.elangovan.portfolio.dto.response.ProfileResponse.CertificationResponse;
import com.elangovan.portfolio.service.CertificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/certifications")
@RequiredArgsConstructor
@Tag(name = "Certifications", description = "Professional certifications management")
public class CertificationController {

    private final CertificationService certificationService;

    @GetMapping
    @Operation(summary = "Get all certifications (public)")
    public ResponseEntity<ApiResponse<List<CertificationResponse>>> getAllCertifications() {
        List<CertificationResponse> certs = certificationService.getAllCertifications();
        return ResponseEntity.ok(ApiResponse.success(certs));
    }

    @PostMapping
    @Operation(summary = "Add certification (requires authentication)")
    public ResponseEntity<ApiResponse<CertificationResponse>> createCertification(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody CertificationRequest request) {
        CertificationResponse cert = certificationService.createCertification(userDetails.getUsername(), request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Certification added", cert));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete certification (requires authentication)")
    public ResponseEntity<ApiResponse<Void>> deleteCertification(@PathVariable Long id) {
        certificationService.deleteCertification(id);
        return ResponseEntity.ok(ApiResponse.success("Certification deleted", null));
    }
}
