package com.elangovan.portfolio.service;

import com.elangovan.portfolio.dto.request.GalleryRequest;
import com.elangovan.portfolio.dto.response.ProfileResponse.GalleryResponse;
import com.elangovan.portfolio.entity.GalleryItem;
import com.elangovan.portfolio.entity.User;
import com.elangovan.portfolio.exception.ResourceNotFoundException;
import com.elangovan.portfolio.repository.GalleryRepository;
import com.elangovan.portfolio.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GalleryService {

    private final GalleryRepository galleryRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<GalleryResponse> getAllGalleryItems() {
        User user = getFirstUser();
        return galleryRepository.findByUserIdOrderByDisplayOrderAsc(user.getId())
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public GalleryResponse createGalleryItem(String email, GalleryRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        GalleryItem item = GalleryItem.builder()
                .user(user)
                .imageUrl(request.getImageUrl())
                .caption(request.getCaption())
                .displayOrder(request.getDisplayOrder() != null ? request.getDisplayOrder() : 0)
                .build();

        return mapToResponse(galleryRepository.save(item));
    }

    @Transactional
    public void deleteGalleryItem(Long id) {
        if (!galleryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Gallery item", "id", id);
        }
        galleryRepository.deleteById(id);
    }

    private User getFirstUser() {
        return userRepository.findAll().stream()
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("No user found"));
    }

    private GalleryResponse mapToResponse(GalleryItem g) {
        return GalleryResponse.builder()
                .id(g.getId())
                .imageUrl(g.getImageUrl())
                .caption(g.getCaption())
                .displayOrder(g.getDisplayOrder())
                .build();
    }
}
