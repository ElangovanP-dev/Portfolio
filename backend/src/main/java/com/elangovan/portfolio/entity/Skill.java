package com.elangovan.portfolio.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "skills", indexes = {
    @Index(name = "idx_skill_user", columnList = "user_id")
})
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "skill_name", nullable = false)
    private String skillName;

    @Column(name = "icon_class")
    private String iconClass;

    @Column(name = "proficiency_percent")
    @Builder.Default
    private Integer proficiencyPercent = 0;

    private String category;

    @Column(name = "display_order")
    @Builder.Default
    private Integer displayOrder = 0;
}
