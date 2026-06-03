package com.elangovan.portfolio.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GalleryRequest {

    @NotBlank(message = "Image URL is required")
    private String imageUrl;

    private String caption;

    private Integer displayOrder;
}
