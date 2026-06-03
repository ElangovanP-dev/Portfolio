package com.elangovan.portfolio.controller;

import com.elangovan.portfolio.dto.request.SkillRequest;
import com.elangovan.portfolio.dto.response.ApiResponse;
import com.elangovan.portfolio.dto.response.ProfileResponse.SkillResponse;
import com.elangovan.portfolio.service.SkillService;
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
@RequestMapping("/api/skills")
@RequiredArgsConstructor
@Tag(name = "Skills", description = "Technical skills management")
public class SkillController {

    private final SkillService skillService;

    @GetMapping
    @Operation(summary = "Get all skills (public)")
    public ResponseEntity<ApiResponse<List<SkillResponse>>> getAllSkills() {
        List<SkillResponse> skills = skillService.getAllSkills();
        return ResponseEntity.ok(ApiResponse.success(skills));
    }

    @PostMapping
    @Operation(summary = "Add a new skill (requires authentication)")
    public ResponseEntity<ApiResponse<SkillResponse>> createSkill(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody SkillRequest request) {
        SkillResponse skill = skillService.createSkill(userDetails.getUsername(), request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Skill added", skill));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update skill (requires authentication)")
    public ResponseEntity<ApiResponse<SkillResponse>> updateSkill(
            @PathVariable Long id,
            @Valid @RequestBody SkillRequest request) {
        SkillResponse skill = skillService.updateSkill(id, request);
        return ResponseEntity.ok(ApiResponse.success("Skill updated", skill));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete skill (requires authentication)")
    public ResponseEntity<ApiResponse<Void>> deleteSkill(@PathVariable Long id) {
        skillService.deleteSkill(id);
        return ResponseEntity.ok(ApiResponse.success("Skill deleted", null));
    }
}
