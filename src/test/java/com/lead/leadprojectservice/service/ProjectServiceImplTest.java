package com.lead.leadprojectservice.service;

import com.lead.leadprojectservice.model.Project;
import com.lead.leadprojectservice.repository.ProjectRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProjectServiceImplTest {

    private ProjectRepository projectRepository;
    private ProjectServiceImpl projectService;

    @BeforeEach
    void Setup() {
        projectRepository = org.mockito.Mockito.mock(ProjectRepository.class);
        projectService = new ProjectServiceImpl(projectRepository);
    }

    @Test
    void createProject_savesNewProject() {
        Project project = new Project();
        project.setName("New Project");
        project.setDescription("Description");
        project.setStartDate(LocalDate.now());
        project.setEndDate(LocalDate.now().plusDays(30));

        when(projectRepository.findByName(project.getName())).thenReturn(Optional.empty());
        when(projectRepository.save(project)).thenReturn(project);

        Project created = projectService.createProject(project);

        assertEquals(project.getName(), created.getName());
        verify(projectRepository, times(1)).save(project);
    }

    @Test
    void createProject_throwsIfExists() {
        Project project = new Project();
        project.setName("Existing Project");
        when(projectRepository.findByName(project.getName())).thenReturn(Optional.of(project));

        assertThrows(IllegalArgumentException.class, () -> projectService.createProject(project));
        verify(projectRepository, never()).save(any());
    }

    @Test
    void updateProject_updatesExistingProject() {
        Project existing = new Project();
        existing.setId(1L);
        existing.setName("Old Name");

        Project updates = new Project();
        updates.setName("Updated Name");
        updates.setDescription("Updated Description");
        updates.setStartDate(LocalDate.now());
        updates.setEndDate(LocalDate.now().plusDays(60));

        when(projectRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(projectRepository.save(existing)).thenReturn(existing);

        Project updated = projectService.updateProject(1L, updates);

        assertEquals("Updated Name", updated.getName());
        assertEquals("Updated Description", updated.getDescription());
        verify(projectRepository, times(1)).save(existing);
    }

    @Test
    void updateProject_throwsIfNotFound() {
        Project updates = new Project();
        when(projectRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> projectService.updateProject(1L, updates));
        verify(projectRepository, never()).save(any());
    }

    @Test
    void deleteProject_deletesById() {
        doNothing().when(projectRepository).deleteById(1L);

        projectService.deleteProject(1L);

        verify(projectRepository, times(1)).deleteById(1L);
    }

    @Test
    void getProjectById_returnsProject() {
        Project project = new Project();
        project.setId(1L);
        project.setName("Test Project");

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        Project found = projectService.getProjectById(1L);

        assertEquals(project.getName(), found.getName());
        verify(projectRepository, times(1)).findById(1L);
    }

    @Test
    void getProjectById_throwsIfNotFound() {
        when(projectRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> projectService.getProjectById(1L));
        verify(projectRepository, times(1)).findById(1L);
    }

    @Test
    void getAllProjects_returnsAllProjects() {
        List<Project> projects = List.of(
                new Project(),
                new Project()
        );

        when(projectRepository.findAll()).thenReturn(projects);

        List<Project> result = projectService.getAllProjects();

        assertEquals(projects.size(), result.size());
        verify(projectRepository, times(1)).findAll();
    }
}
