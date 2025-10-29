package com.lead.leadprojectservice.controller;

import com.lead.leadprojectservice.model.Project;
import com.lead.leadprojectservice.service.ProjectService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProjectControllerTest {

    private ProjectService projectService;
    private ProjectController projectController;

    @BeforeEach
    void setup() {
        projectService = mock(ProjectService.class);
        projectController = new ProjectController(projectService);
    }

    @Test
    void createProject_returnsCreatedProject() {
        Project project = new Project();
        project.setName("Test Project");
        project.setDescription("Test Description");
        project.setStartDate(LocalDate.now());
        project.setEndDate(LocalDate.now().plusDays(30));

        when(projectService.createProject(project)).thenReturn(project);

        ResponseEntity<Project> response = projectController.createProject(project);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(project.getName(), response.getBody().getName());
        verify(projectService, times(1)).createProject(project);
    }

    @Test
    void createProject_handlesServiceException() {
        Project project = new Project();
        project.setName("Existing Project");

        when(projectService.createProject(project))
                .thenThrow(new IllegalArgumentException("Project already exists"));

        assertThrows(IllegalArgumentException.class,
                () -> projectController.createProject(project));
        verify(projectService, times(1)).createProject(project);
    }

    @Test
    void getProjectById_returnsProject() {
        Project project = new Project();
        project.setId(1L);
        project.setName("Test Project");

        when(projectService.getProjectById(1L)).thenReturn(project);

        ResponseEntity<Project> response = projectController.getProjectById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(project.getName(), response.getBody().getName());
        verify(projectService, times(1)).getProjectById(1L);
    }

    @Test
    void getProjectById_handlesNotFound() {
        when(projectService.getProjectById(1L))
                .thenThrow(new EntityNotFoundException("Project not found"));

        assertThrows(EntityNotFoundException.class,
                () -> projectController.getProjectById(1L));
        verify(projectService, times(1)).getProjectById(1L);
    }

    @Test
    void getAllProjects_returnsAllProjects() {
        List<Project> projects = List.of(
                new Project(),
                new Project()
        );

        when(projectService.getAllProjects()).thenReturn(projects);

        ResponseEntity<List<Project>> response = projectController.getAllProjects();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(projects.size(), response.getBody().size());
        verify(projectService, times(1)).getAllProjects();
    }

    @Test
    void deleteProject_returnsNoContent() {
        doNothing().when(projectService).deleteProject(1L);

        ResponseEntity<String> response = projectController.deleteProject(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(projectService, times(1)).deleteProject(1L);
    }

    @Test
    void deleteProject_handlesNotFound() {
        doThrow(new EntityNotFoundException("Project not found"))
                .when(projectService).deleteProject(1L);

        assertThrows(EntityNotFoundException.class,
                () -> projectController.deleteProject(1L));
        verify(projectService, times(1)).deleteProject(1L);
    }
}
