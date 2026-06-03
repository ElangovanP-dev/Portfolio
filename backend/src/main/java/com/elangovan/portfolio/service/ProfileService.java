package com.elangovan.portfolio.service;

import com.elangovan.portfolio.dto.response.ProfileResponse;
import com.elangovan.portfolio.entity.*;
import com.elangovan.portfolio.exception.ResourceNotFoundException;
import com.elangovan.portfolio.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final UserRepository userRepository;

    /**
     * Get the first user's full profile (single-user portfolio).
     */
    @Transactional(readOnly = true)
    public ProfileResponse getProfile() {
        User user = userRepository.findAll().stream()
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("No profile found"));

        return mapToProfileResponse(user);
    }

    /**
     * Update profile fields for the given user email.
     */
    @Transactional
    public ProfileResponse updateProfile(String email, ProfileResponse updates) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        if (updates.getName() != null) user.setName(updates.getName());
        if (updates.getBio() != null) user.setBio(updates.getBio());
        if (updates.getTagline() != null) user.setTagline(updates.getTagline());
        if (updates.getSubtitle() != null) user.setSubtitle(updates.getSubtitle());
        if (updates.getGithubUrl() != null) user.setGithubUrl(updates.getGithubUrl());
        if (updates.getLinkedinUrl() != null) user.setLinkedinUrl(updates.getLinkedinUrl());
        if (updates.getProfileImageUrl() != null) user.setProfileImageUrl(updates.getProfileImageUrl());

        User saved = userRepository.save(user);
        return mapToProfileResponse(saved);
    }

    private ProfileResponse mapToProfileResponse(User user) {
        return ProfileResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .bio(user.getBio())
                .tagline(user.getTagline())
                .subtitle(user.getSubtitle())
                .githubUrl(user.getGithubUrl())
                .linkedinUrl(user.getLinkedinUrl())
                .profileImageUrl(user.getProfileImageUrl())
                .projects(user.getProjects().stream()
                        .map(this::mapProject)
                        .collect(Collectors.toList()))
                .skills(user.getSkills().stream()
                        .map(this::mapSkill)
                        .collect(Collectors.toList()))
                .experiences(user.getExperiences().stream()
                        .map(this::mapExperience)
                        .collect(Collectors.toList()))
                .certifications(user.getCertifications().stream()
                        .map(this::mapCertification)
                        .collect(Collectors.toList()))
                .galleryItems(user.getGalleryItems().stream()
                        .map(this::mapGallery)
                        .collect(Collectors.toList()))
                .build();
    }

    private ProfileResponse.ProjectResponse mapProject(Project p) {
        return ProfileResponse.ProjectResponse.builder()
                .id(p.getId())
                .title(p.getTitle())
                .description(p.getDescription())
                .techStack(p.getTechStack())
                .githubUrl(p.getGithubUrl())
                .liveUrl(p.getLiveUrl())
                .displayOrder(p.getDisplayOrder())
                .build();
    }

    private ProfileResponse.SkillResponse mapSkill(Skill s) {
        return ProfileResponse.SkillResponse.builder()
                .id(s.getId())
                .skillName(s.getSkillName())
                .iconClass(s.getIconClass())
                .proficiencyPercent(s.getProficiencyPercent())
                .category(s.getCategory())
                .displayOrder(s.getDisplayOrder())
                .build();
    }

    private ProfileResponse.ExperienceResponse mapExperience(Experience e) {
        return ProfileResponse.ExperienceResponse.builder()
                .id(e.getId())
                .company(e.getCompany())
                .role(e.getRole())
                .description(e.getDescription())
                .startDate(e.getStartDate() != null ? e.getStartDate().toString() : null)
                .endDate(e.getEndDate() != null ? e.getEndDate().toString() : null)
                .displayOrder(e.getDisplayOrder())
                .build();
    }

    private ProfileResponse.CertificationResponse mapCertification(Certification c) {
        return ProfileResponse.CertificationResponse.builder()
                .id(c.getId())
                .certName(c.getCertName())
                .issuer(c.getIssuer())
                .issueDate(c.getIssueDate() != null ? c.getIssueDate().toString() : null)
                .expiryDate(c.getExpiryDate() != null ? c.getExpiryDate().toString() : null)
                .credentialUrl(c.getCredentialUrl())
                .build();
    }

    private ProfileResponse.GalleryResponse mapGallery(GalleryItem g) {
        return ProfileResponse.GalleryResponse.builder()
                .id(g.getId())
                .imageUrl(g.getImageUrl())
                .caption(g.getCaption())
                .displayOrder(g.getDisplayOrder())
                .build();
    }
}
