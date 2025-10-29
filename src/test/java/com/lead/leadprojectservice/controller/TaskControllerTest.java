package com.lead.leadprojectservice.controller;

import com.lead.leadprojectservice.model.Task;
import com.lead.leadprojectservice.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskControllerTest {

    private TaskService taskService;
    private TaskController taskController;

    @BeforeEach
    void setup() {
        taskService = mock(TaskService.class);
        taskController = new TaskController(taskService);
    }

    @Test
    void createTask_returnsCreatedTask() {
        Task task = new Task();
        task.setTitle("Test Task");
        task.setDescription("Test Description");
        task.setDueDate(LocalDate.now());

        when(taskService.createTask(task)).thenReturn(task);

        ResponseEntity<Task> response = taskController.createTask(task);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(task.getTitle(), response.getBody().getTitle());
        verify(taskService, times(1)).createTask(task);
    }

    @Test
    void createTask_handlesServiceException() {
        Task task = new Task();
        when(taskService.createTask(task))
                .thenThrow(new IllegalArgumentException("Invalid task"));

        assertThrows(IllegalArgumentException.class,
                () -> taskController.createTask(task));
        verify(taskService, times(1)).createTask(task);
    }

    @Test
    void getTaskById_returnsTask() {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Test Task");

        when(taskService.getTaskById(1L)).thenReturn(task);

        ResponseEntity<Task> response = taskController.getTaskById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(task.getTitle(), response.getBody().getTitle());
        verify(taskService, times(1)).getTaskById(1L);
    }

    @Test
    void getTaskById_handlesNotFound() {
        when(taskService.getTaskById(1L))
                .thenThrow(new IllegalArgumentException("Task not found"));

        assertThrows(IllegalArgumentException.class,
                () -> taskController.getTaskById(1L));
        verify(taskService, times(1)).getTaskById(1L);
    }

    @Test
    void getAllTasks_returnsResponseEntity() {
        ResponseEntity<Task> response = taskController.getAllTasks();

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getTasksByProjectId_returnsProjectTasks() {
        Task task = new Task();
        task.setProjectId(1L);

        when(taskService.finByProjectId(1L)).thenReturn(task);

        ResponseEntity<Task> response = taskController.getTasksByProjectId(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(task.getProjectId(), response.getBody().getProjectId());
        verify(taskService, times(1)).finByProjectId(1L);
    }

    @Test
    void getTasksByProjectId_handlesNotFound() {
        when(taskService.finByProjectId(1L))
                .thenThrow(new IllegalArgumentException("No tasks found"));

        assertThrows(IllegalArgumentException.class,
                () -> taskController.getTasksByProjectId(1L));
        verify(taskService, times(1)).finByProjectId(1L);
    }

    @Test
    void updateTask_returnsUpdatedTask() {
        Task task = new Task();
        task.setTitle("Updated Task");

        when(taskService.updateTask(1L, task)).thenReturn(task);

        ResponseEntity<Task> response = taskController.updateTask(1L, task);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(task.getTitle(), response.getBody().getTitle());
        verify(taskService, times(1)).updateTask(1L, task);
    }

    @Test
    void updateTask_handlesNotFound() {
        Task task = new Task();
        when(taskService.updateTask(1L, task))
                .thenThrow(new IllegalArgumentException("Task not found"));

        assertThrows(IllegalArgumentException.class,
                () -> taskController.updateTask(1L, task));
        verify(taskService, times(1)).updateTask(1L, task);
    }

    @Test
    void deleteTask_returnsNoContent() {
        doNothing().when(taskService).deleteTask(1L);

        ResponseEntity<String> response = taskController.deleteTask(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(taskService, times(1)).deleteTask(1L);
    }

    @Test
    void deleteTask_handlesNotFound() {
        doThrow(new IllegalArgumentException("Task not found"))
                .when(taskService).deleteTask(1L);

        assertThrows(IllegalArgumentException.class,
                () -> taskController.deleteTask(1L));
        verify(taskService, times(1)).deleteTask(1L);
    }
}
