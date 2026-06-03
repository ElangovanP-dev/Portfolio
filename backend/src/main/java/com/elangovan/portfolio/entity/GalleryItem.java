package com.elangovan.portfolio.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "gallery_items", indexes = {
    @Index(name = "idx_gallery_user", columnList = "user_id")
})
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class GalleryItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    private String caption;

    @Column(name = "display_order")
    @Builder.Default
    private Integer displayOrder = 0;
}
