package com.elangovan.portfolio.service;

import com.elangovan.portfolio.dto.request.ExperienceRequest;
import com.elangovan.portfolio.dto.response.ProfileResponse.ExperienceResponse;
import com.elangovan.portfolio.entity.Experience;
import com.elangovan.portfolio.entity.User;
import com.elangovan.portfolio.exception.ResourceNotFoundException;
import com.elangovan.portfolio.repository.ExperienceRepository;
import com.elangovan.portfolio.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExperienceService {

    private final ExperienceRepository experienceRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<ExperienceResponse> getAllExperiences() {
        User user = getFirstUser();
        return experienceRepository.findByUserIdOrderByDisplayOrderAsc(user.getId())
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public ExperienceResponse createExperience(String email, ExperienceRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        Experience experience = Experience.builder()
                .user(user)
                .company(request.getCompany())
                .role(request.getRole())
                .description(request.getDescription())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .displayOrder(request.getDisplayOrder() != null ? request.getDisplayOrder() : 0)
                .build();

        return mapToResponse(experienceRepository.save(experience));
    }

    @Transactional
    public ExperienceResponse updateExperience(Long id, ExperienceRequest request) {
        Experience experience = experienceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Experience", "id", id));

        experience.setCompany(request.getCompany());
        experience.setRole(request.getRole());
        experience.setDescription(request.getDescription());
        experience.setStartDate(request.getStartDate());
        experience.setEndDate(request.getEndDate());
        if (request.getDisplayOrder() != null) experience.setDisplayOrder(request.getDisplayOrder());

        return mapToResponse(experienceRepository.save(experience));
    }

    @Transactional
    public void deleteExperience(Long id) {
        if (!experienceRepository.existsById(id)) {
            throw new ResourceNotFoundException("Experience", "id", id);
        }
        experienceRepository.deleteById(id);
    }

    private User getFirstUser() {
        return userRepository.findAll().stream()
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("No user found"));
    }

    private ExperienceResponse mapToResponse(Experience e) {
        return ExperienceResponse.builder()
                .id(e.getId())
                .company(e.getCompany())
                .role(e.getRole())
                .description(e.getDescription())
                .startDate(e.getStartDate() != null ? e.getStartDate().toString() : null)
                .endDate(e.getEndDate() != null ? e.getEndDate().toString() : null)
                .displayOrder(e.getDisplayOrder())
                .build();
    }
}
