package com.lead.leadprojectservice.repository;

import com.lead.leadprojectservice.model.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestPropertySource(properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1",
        "spring.jpa.show-sql=false"
})
class TaskRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TaskRepository taskRepository;

    private Task createTask(String title, Long projectId, String status) {
        Task task = new Task();
        task.setTitle(title);
        task.setProjectId(projectId);
        task.setStatus(status);
        task.setDueDate(LocalDate.now());
        return task;
    }

    @Test
    void findByProjectId_whenTasksExist_returnsTasks() {
        Task task1 = createTask("Task 1", 1L, "TODO");
        Task task2 = createTask("Task 2", 1L, "IN_PROGRESS");

        entityManager.persist(task1);
        entityManager.persist(task2);
        entityManager.flush();

        List<Task> found = taskRepository.findByProjectId(1L);

        assertThat(found).hasSize(2)
                .extracting(Task::getProjectId)
                .containsOnly(1L);
    }

    @Test
    void findByProjectId_whenNoTasks_returnsEmptyList() {
        List<Task> found = taskRepository.findByProjectId(999L);
        assertThat(found).isEmpty();
    }

    @Test
    void findByJiraIssueId_whenTaskExists_returnsTask() {
        Task task = createTask("Jira Task", 1L, "TODO");
        task.setJiraIssueId("JIRA-123");
        entityManager.persistAndFlush(task);

        Optional<Task> found = taskRepository.findByJiraIssueId("JIRA-123");

        assertThat(found).isPresent()
                .hasValueSatisfying(t -> {
                    assertThat(t.getJiraIssueId()).isEqualTo("JIRA-123");
                    assertThat(t.getTitle()).isEqualTo("Jira Task");
                });
    }

    @Test
    void findByJiraIssueId_whenTaskDoesNotExist_returnsEmpty() {
        Optional<Task> found = taskRepository.findByJiraIssueId("NONEXISTENT-123");
        assertThat(found).isEmpty();
    }
}
