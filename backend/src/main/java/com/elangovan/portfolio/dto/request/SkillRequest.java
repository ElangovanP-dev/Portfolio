package com.elangovan.portfolio.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SkillRequest {

    @NotBlank(message = "Skill name is required")
    private String skillName;

    private String iconClass;

    @Min(value = 0, message = "Proficiency must be between 0 and 100")
    @Max(value = 100, message = "Proficiency must be between 0 and 100")
    private Integer proficiencyPercent;

    private String category;

    private Integer displayOrder;
}
