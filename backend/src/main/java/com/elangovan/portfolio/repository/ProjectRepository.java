package com.elangovan.portfolio.repository;

import com.elangovan.portfolio.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findByUserIdOrderByDisplayOrderAsc(Long userId);

    Page<Project> findByUserId(Long userId, Pageable pageable);

    @Query("SELECT p FROM Project p WHERE p.user.id = :userId AND LOWER(p.techStack) LIKE LOWER(CONCAT('%', :tech, '%'))")
    List<Project> findByUserIdAndTechStack(@Param("userId") Long userId, @Param("tech") String tech);
}
