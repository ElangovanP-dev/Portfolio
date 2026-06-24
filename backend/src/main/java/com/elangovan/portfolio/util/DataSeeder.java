package com.elangovan.portfolio.util;

import com.elangovan.portfolio.entity.*;
import com.elangovan.portfolio.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * Seeds the database with Elangovan P's actual portfolio data on startup.
 * Only runs if no users exist in the database (safe for re-runs).
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final SkillRepository skillRepository;
    private final ExperienceRepository experienceRepository;
    private final GalleryRepository galleryRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.email}")
    private String adminEmail;

    @Value("${app.admin.password}")
    private String adminPassword;

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) {
            log.info("Database already seeded. Skipping...");
            return;
        }

        log.info("🌱 Seeding database with portfolio data...");

        // ── Create Admin User (your profile) ──
        User user = User.builder()
                .email(adminEmail)
                .passwordHash(passwordEncoder.encode(adminPassword))
                .name("Elangovan P")
                .bio("I am a second-year Computer Science Engineering student passionate about building real-world applications. I enjoy working with Java, frontend technologies, and exploring backend development using Spring Boot. I continuously improve my skills through internships, coding challenges, and hands-on projects.")
                .tagline("Passionate Computer Science Student | Java Developer | Web Enthusiast")
                .subtitle("I build modern, scalable, and user-focused digital experiences using Java, Web Technologies, and real-world project ideas.")
                .githubUrl("https://github.com/ElangovanP-dev")
                .linkedinUrl("https://www.linkedin.com/in/elangovan-p-2a4567251")
                .profileImageUrl("profile.png")
                .build();

        user = userRepository.save(user);
        log.info("✅ Admin user created: {}", user.getEmail());

        // ── Seed Skills (from your portfolio HTML) ──
        seedSkill(user, "Java", "fab fa-java", 90, "Language", 1);
        seedSkill(user, "HTML & CSS", "fab fa-html5", 85, "Frontend", 2);
        seedSkill(user, "JavaScript", "fab fa-js", 80, "Frontend", 3);
        seedSkill(user, "SQL", "fas fa-database", 75, "Database", 4);
        seedSkill(user, "Spring Boot (Learning)", "fas fa-leaf", 50, "Framework", 5);
        seedSkill(user, "Git & Problem Solving", "fab fa-git-alt", 85, "Tool", 6);
        log.info("✅ 6 skills seeded");

        // ── Seed Experience ──
        Experience exp = Experience.builder()
                .user(user)
                .role("Java Developer Intern")
                .company("Internship")
                .description("Focused on building real-time applications and improving backend development skills.")
                .startDate(LocalDate.of(2026, 4, 1))
                .endDate(null) // Ongoing
                .displayOrder(1)
                .build();
        experienceRepository.save(exp);
        log.info("✅ 1 experience entry seeded");

        seedProject(user, "ResuMind",
                "An intelligent AI-powered resume analyzer and parser designed to evaluate resume metrics and suggest optimizations.",
                "Hackathon, Java, AI",
                "https://github.com/ElangovanP-dev/ResuMind", "https://resumind-six-vert.vercel.app/", 1);

        seedProject(user, "BudgetFlow",
                "A robust Java-based application to track and manage personal daily finances.",
                "Java, SQL",
                "https://github.com/ElangovanP-dev/BudgetFlow", "https://budget-flow-tawny-tau.vercel.app/", 2);

        seedProject(user, "Online Quiz Application",
                "A complete full-stack web application for hosting and taking dynamic quizzes.",
                "Full Stack, Web",
                "https://github.com/ElangoHackIOT5", null, 3);

        seedProject(user, "Fitness Connect",
                "An innovative app idea designed to connect fitness enthusiasts globally.",
                "App Idea, Planning",
                null, null, 4);
        log.info("✅ 4 projects seeded");

        // ── Seed Gallery Items ──
        seedGallery(user, "dashboard-ui.png", "Dashboard UI", 1);
        seedGallery(user, "network-graph.png", "Network Graph", 2);
        seedGallery(user, "particles-setup.png", "Particles Setup", 3);
        log.info("✅ 3 gallery items seeded");

        log.info("🎉 Database seeding complete!");
        log.info("📧 Login credentials: {} / {}", adminEmail, adminPassword);
    }

    private void seedSkill(User user, String name, String icon, int proficiency, String category, int order) {
        skillRepository.save(Skill.builder()
                .user(user)
                .skillName(name)
                .iconClass(icon)
                .proficiencyPercent(proficiency)
                .category(category)
                .displayOrder(order)
                .build());
    }

    private void seedProject(User user, String title, String desc, String tech, String github, String live, int order) {
        projectRepository.save(Project.builder()
                .user(user)
                .title(title)
                .description(desc)
                .techStack(tech)
                .githubUrl(github)
                .liveUrl(live)
                .displayOrder(order)
                .build());
    }

    private void seedGallery(User user, String url, String caption, int order) {
        galleryRepository.save(GalleryItem.builder()
                .user(user)
                .imageUrl(url)
                .caption(caption)
                .displayOrder(order)
                .build());
    }
}
