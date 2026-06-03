package com.elangovan.portfolio.service;

import com.elangovan.portfolio.dto.request.ProjectRequest;
import com.elangovan.portfolio.dto.response.ProfileResponse.ProjectResponse;
import com.elangovan.portfolio.entity.Project;
import com.elangovan.portfolio.entity.User;
import com.elangovan.portfolio.exception.ResourceNotFoundException;
import com.elangovan.portfolio.repository.ProjectRepository;
import com.elangovan.portfolio.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ProjectService projectService;

    private User user;
    private Project project;
    private ProjectRequest projectRequest;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("test@test.com");

        project = Project.builder()
                .id(1L)
                .title("Test Project")
                .description("Test Description")
                .techStack("Java")
                .user(user)
                .displayOrder(1)
                .build();

        projectRequest = new ProjectRequest("Test Project", "Test Description", "Java", "http://github.com", "http://live.com", 1);
    }

    @Test
    void getAllProjects_shouldReturnList() {
        when(userRepository.findAll()).thenReturn(List.of(user));
        when(projectRepository.findByUserIdOrderByDisplayOrderAsc(1L)).thenReturn(List.of(project));

        List<ProjectResponse> result = projectService.getAllProjects();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Project", result.get(0).getTitle());
    }

    @Test
    void getProjectsPaginated_shouldReturnPage() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Project> page = new PageImpl<>(List.of(project));

        when(userRepository.findAll()).thenReturn(List.of(user));
        when(projectRepository.findByUserId(1L, pageable)).thenReturn(page);

        Page<ProjectResponse> result = projectService.getProjectsPaginated(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Test Project", result.getContent().get(0).getTitle());
    }

    @Test
    void getProjectById_shouldReturnProject() {
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        ProjectResponse result = projectService.getProjectById(1L);

        assertNotNull(result);
        assertEquals("Test Project", result.getTitle());
    }

    @Test
    void getProjectById_notFound_shouldThrowException() {
        when(projectRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> projectService.getProjectById(99L));
    }

    @Test
    void createProject_shouldCreate() {
        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(user));
        when(projectRepository.save(any(Project.class))).thenReturn(project);

        ProjectResponse result = projectService.createProject("test@test.com", projectRequest);

        assertNotNull(result);
        assertEquals("Test Project", result.getTitle());
        verify(projectRepository, times(1)).save(any(Project.class));
    }

    @Test
    void deleteProject_shouldDelete() {
        when(projectRepository.existsById(1L)).thenReturn(true);
        doNothing().when(projectRepository).deleteById(1L);

        assertDoesNotThrow(() -> projectService.deleteProject(1L));
        verify(projectRepository, times(1)).deleteById(1L);
    }
}
