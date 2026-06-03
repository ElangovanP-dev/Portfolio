package com.elangovan.portfolio.repository;

import com.elangovan.portfolio.entity.GalleryItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GalleryRepository extends JpaRepository<GalleryItem, Long> {

    List<GalleryItem> findByUserIdOrderByDisplayOrderAsc(Long userId);
}
