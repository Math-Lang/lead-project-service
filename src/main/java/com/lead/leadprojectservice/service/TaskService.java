package com.lead.leadprojectservice.service;

import com.lead.leadprojectservice.model.Task;

import java.util.List;

public interface TaskService {
    Task createTask(Task task);
    Task updateTask(Long id, Task task);
    void deleteTask(Long id);
    Task getTaskById(Long id);
    Task finByProjectId(Long projectId);
    List<Task> getAllTasks();
    List<Task> getTasksByProjectId(Long projectId);
}
