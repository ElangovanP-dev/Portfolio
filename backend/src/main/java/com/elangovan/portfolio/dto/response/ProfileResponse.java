package com.elangovan.portfolio.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Full public profile response including all portfolio sections.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileResponse {

    private Long id;
    private String name;
    private String email;
    private String bio;
    private String tagline;
    private String subtitle;
    private String githubUrl;
    private String linkedinUrl;
    private String profileImageUrl;

    private List<ProjectResponse> projects;
    private List<SkillResponse> skills;
    private List<ExperienceResponse> experiences;
    private List<CertificationResponse> certifications;
    private List<GalleryResponse> galleryItems;

    // Nested response records for each section

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ProjectResponse {
        private Long id;
        private String title;
        private String description;
        private String techStack;
        private String githubUrl;
        private String liveUrl;
        private Integer displayOrder;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SkillResponse {
        private Long id;
        private String skillName;
        private String iconClass;
        private Integer proficiencyPercent;
        private String category;
        private Integer displayOrder;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ExperienceResponse {
        private Long id;
        private String company;
        private String role;
        private String description;
        private String startDate;
        private String endDate;
        private Integer displayOrder;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CertificationResponse {
        private Long id;
        private String certName;
        private String issuer;
        private String issueDate;
        private String expiryDate;
        private String credentialUrl;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class GalleryResponse {
        private Long id;
        private String imageUrl;
        private String caption;
        private Integer displayOrder;
    }
}
