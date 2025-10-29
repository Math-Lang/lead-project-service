package com.lead.leadprojectservice.repository;

import com.lead.leadprojectservice.model.Project;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestPropertySource(properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1"
})
class ProjectRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ProjectRepository projectRepository;

    @Test
    void findByName_whenProjectExists_returnsProject() {
        // Arrange
        Project project = new Project();
        project.setName("Test Project");
        project.setStartDate(LocalDate.now());
        project.setEndDate(LocalDate.now().plusDays(30));
        entityManager.persistAndFlush(project);

        // Act
        Optional<Project> found = projectRepository.findByName("Test Project");

        // Assert
        assertThat(found)
                .isPresent()
                .hasValueSatisfying(p -> assertThat(p.getName()).isEqualTo("Test Project"));
    }

    @Test
    void findByName_whenProjectDoesNotExist_returnsEmpty() {
        Optional<Project> found = projectRepository.findByName("Nonexistent");
        assertThat(found).isEmpty();
    }

    @Test
    void findByJiraProjectId_whenProjectExists_returnsProject() {
        // Arrange
        Project project = new Project();
        project.setName("Test Project");
        project.setJiraProjectId("JIRA-123");
        entityManager.persistAndFlush(project);

        // Act
        Optional<Project> found = projectRepository.findByJiraProjectId("JIRA-123");

        // Assert
        assertThat(found)
                .isPresent()
                .hasValueSatisfying(p -> assertThat(p.getJiraProjectId()).isEqualTo("JIRA-123"));
    }

    @Test
    void findByJiraProjectId_whenProjectDoesNotExist_returnsEmpty() {
        Optional<Project> found = projectRepository.findByJiraProjectId("NONEXISTENT-123");
        assertThat(found).isEmpty();
    }
}
