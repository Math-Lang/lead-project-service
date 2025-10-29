package com.lead.leadprojectservice.service;

import com.lead.leadprojectservice.model.Task;
import com.lead.leadprojectservice.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServiceImplTest {

    private TaskRepository taskRepository;
    private TaskServiceImpl taskService;

    @BeforeEach
    void setup() {
        taskRepository = mock(TaskRepository.class);
        taskService = new TaskServiceImpl(taskRepository);
    }

    @Test
    void createTask_savesNewTask() {
        Task task = new Task();
        task.setTitle("Test Task");

        when(taskRepository.save(task)).thenReturn(task);

        Task created = taskService.createTask(task);

        assertEquals(task.getTitle(), created.getTitle());
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void updateTask_updatesExistingTask() {
        Task existing = new Task();
        existing.setId(1L);
        existing.setTitle("Old Title");

        Task updates = new Task();
        updates.setTitle("New Title");
        updates.setDescription("New Description");
        updates.setStatus("IN_PROGRESS");
        updates.setDueDate(LocalDate.now());
        updates.setPriority("HIGH");
        updates.setProjectId(1L);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(taskRepository.save(existing)).thenReturn(existing);

        Task updated = taskService.updateTask(1L, updates);

        assertEquals("New Title", updated.getTitle());
        verify(taskRepository, times(1)).save(existing);
    }

    @Test
    void updateTask_throwsIfNotFound() {
        Task updates = new Task();
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> taskService.updateTask(1L, updates));
        verify(taskRepository, never()).save(any());
    }

    @Test
    void deleteTask_deletesExistingTask() {
        Task task = new Task();
        task.setId(1L);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        doNothing().when(taskRepository).delete(task);

        taskService.deleteTask(1L);

        verify(taskRepository, times(1)).delete(task);
    }

    @Test
    void deleteTask_throwsIfNotFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> taskService.deleteTask(1L));
        verify(taskRepository, never()).delete(any());
    }

    @Test
    void getTaskById_returnsTask() {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Test Task");

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        Task found = taskService.getTaskById(1L);

        assertEquals(task.getTitle(), found.getTitle());
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    void getTaskById_throwsIfNotFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> taskService.getTaskById(1L));
    }

    @Test
    void findByProjectId_returnsFirstTask() {
        Task task = new Task();
        task.setProjectId(1L);

        when(taskRepository.findByProjectId(1L)).thenReturn(List.of(task));

        Task found = taskService.finByProjectId(1L);

        assertEquals(task.getProjectId(), found.getProjectId());
        verify(taskRepository, times(1)).findByProjectId(1L);
    }

    @Test
    void findByProjectId_throwsIfNoTasks() {
        when(taskRepository.findByProjectId(1L)).thenReturn(List.of());

        assertThrows(IllegalArgumentException.class, () -> taskService.finByProjectId(1L));
    }

    @Test
    void getAllTasks_returnsAllTasks() {
        List<Task> tasks = List.of(new Task(), new Task());
        when(taskRepository.findAll()).thenReturn(tasks);

        List<Task> result = taskService.getAllTasks();

        assertEquals(tasks.size(), result.size());
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    void getTasksByProjectId_returnsProjectTasks() {
        List<Task> tasks = List.of(new Task(), new Task());
        when(taskRepository.findByProjectId(1L)).thenReturn(tasks);

        List<Task> result = taskService.getTasksByProjectId(1L);

        assertEquals(tasks.size(), result.size());
        verify(taskRepository, times(1)).findByProjectId(1L);
    }
}
