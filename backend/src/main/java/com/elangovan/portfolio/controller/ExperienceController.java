package com.elangovan.portfolio.controller;

import com.elangovan.portfolio.dto.request.ExperienceRequest;
import com.elangovan.portfolio.dto.response.ApiResponse;
import com.elangovan.portfolio.dto.response.ProfileResponse.ExperienceResponse;
import com.elangovan.portfolio.service.ExperienceService;
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
@RequestMapping("/api/experience")
@RequiredArgsConstructor
@Tag(name = "Experience", description = "Work experience management")
public class ExperienceController {

    private final ExperienceService experienceService;

    @GetMapping
    @Operation(summary = "Get all experience entries (public)")
    public ResponseEntity<ApiResponse<List<ExperienceResponse>>> getAllExperiences() {
        List<ExperienceResponse> experiences = experienceService.getAllExperiences();
        return ResponseEntity.ok(ApiResponse.success(experiences));
    }

    @PostMapping
    @Operation(summary = "Add experience entry (requires authentication)")
    public ResponseEntity<ApiResponse<ExperienceResponse>> createExperience(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody ExperienceRequest request) {
        ExperienceResponse experience = experienceService.createExperience(userDetails.getUsername(), request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Experience added", experience));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update experience (requires authentication)")
    public ResponseEntity<ApiResponse<ExperienceResponse>> updateExperience(
            @PathVariable Long id,
            @Valid @RequestBody ExperienceRequest request) {
        ExperienceResponse experience = experienceService.updateExperience(id, request);
        return ResponseEntity.ok(ApiResponse.success("Experience updated", experience));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete experience (requires authentication)")
    public ResponseEntity<ApiResponse<Void>> deleteExperience(@PathVariable Long id) {
        experienceService.deleteExperience(id);
        return ResponseEntity.ok(ApiResponse.success("Experience deleted", null));
    }
}
