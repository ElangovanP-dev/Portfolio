package com.elangovan.portfolio.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExperienceRequest {

    private String company;

    @NotBlank(message = "Role is required")
    private String role;

    private String description;

    private LocalDate startDate;

    private LocalDate endDate;

    private Integer displayOrder;
}
