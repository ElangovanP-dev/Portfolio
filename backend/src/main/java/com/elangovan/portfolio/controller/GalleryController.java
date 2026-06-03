package com.elangovan.portfolio.controller;

import com.elangovan.portfolio.dto.request.GalleryRequest;
import com.elangovan.portfolio.dto.response.ApiResponse;
import com.elangovan.portfolio.dto.response.ProfileResponse.GalleryResponse;
import com.elangovan.portfolio.service.GalleryService;
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
@RequestMapping("/api/gallery")
@RequiredArgsConstructor
@Tag(name = "Gallery", description = "Creative gallery management")
public class GalleryController {

    private final GalleryService galleryService;

    @GetMapping
    @Operation(summary = "Get all gallery items (public)")
    public ResponseEntity<ApiResponse<List<GalleryResponse>>> getAllGalleryItems() {
        List<GalleryResponse> items = galleryService.getAllGalleryItems();
        return ResponseEntity.ok(ApiResponse.success(items));
    }

    @PostMapping
    @Operation(summary = "Add gallery item (requires authentication)")
    public ResponseEntity<ApiResponse<GalleryResponse>> createGalleryItem(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody GalleryRequest request) {
        GalleryResponse item = galleryService.createGalleryItem(userDetails.getUsername(), request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Gallery item added", item));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete gallery item (requires authentication)")
    public ResponseEntity<ApiResponse<Void>> deleteGalleryItem(@PathVariable Long id) {
        galleryService.deleteGalleryItem(id);
        return ResponseEntity.ok(ApiResponse.success("Gallery item deleted", null));
    }
}
