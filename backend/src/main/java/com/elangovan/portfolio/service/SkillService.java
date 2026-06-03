package com.elangovan.portfolio.service;

import com.elangovan.portfolio.dto.request.SkillRequest;
import com.elangovan.portfolio.dto.response.ProfileResponse.SkillResponse;
import com.elangovan.portfolio.entity.Skill;
import com.elangovan.portfolio.entity.User;
import com.elangovan.portfolio.exception.ResourceNotFoundException;
import com.elangovan.portfolio.repository.SkillRepository;
import com.elangovan.portfolio.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SkillService {

    private final SkillRepository skillRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<SkillResponse> getAllSkills() {
        User user = getFirstUser();
        return skillRepository.findByUserIdOrderByDisplayOrderAsc(user.getId())
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public SkillResponse createSkill(String email, SkillRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        Skill skill = Skill.builder()
                .user(user)
                .skillName(request.getSkillName())
                .iconClass(request.getIconClass())
                .proficiencyPercent(request.getProficiencyPercent() != null ? request.getProficiencyPercent() : 0)
                .category(request.getCategory())
                .displayOrder(request.getDisplayOrder() != null ? request.getDisplayOrder() : 0)
                .build();

        return mapToResponse(skillRepository.save(skill));
    }

    @Transactional
    public SkillResponse updateSkill(Long id, SkillRequest request) {
        Skill skill = skillRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Skill", "id", id));

        skill.setSkillName(request.getSkillName());
        skill.setIconClass(request.getIconClass());
        if (request.getProficiencyPercent() != null) skill.setProficiencyPercent(request.getProficiencyPercent());
        if (request.getCategory() != null) skill.setCategory(request.getCategory());
        if (request.getDisplayOrder() != null) skill.setDisplayOrder(request.getDisplayOrder());

        return mapToResponse(skillRepository.save(skill));
    }

    @Transactional
    public void deleteSkill(Long id) {
        if (!skillRepository.existsById(id)) {
            throw new ResourceNotFoundException("Skill", "id", id);
        }
        skillRepository.deleteById(id);
    }

    private User getFirstUser() {
        return userRepository.findAll().stream()
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("No user found"));
    }

    private SkillResponse mapToResponse(Skill s) {
        return SkillResponse.builder()
                .id(s.getId())
                .skillName(s.getSkillName())
                .iconClass(s.getIconClass())
                .proficiencyPercent(s.getProficiencyPercent())
                .category(s.getCategory())
                .displayOrder(s.getDisplayOrder())
                .build();
    }
}
