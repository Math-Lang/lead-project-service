package com.lead.leadprojectservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "tasks")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String title;

    private String description;
    private LocalDate dueDate;
    private String priority; // "LOW", "MEDIUM", "HIGH"
    private String status; // e.g. "TO-DO", "IN_PROGRESS", "DONE"

    private String dependencies;

    @Column(nullable = false)
    private Long projectId;

    //Optional Jira field
    private String JiraIssueId;
}
