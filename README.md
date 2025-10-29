# lead-project-service
Core microservice for the LEAD platform. Manages projects, tasks, and dependencies with PostgreSQL and REST APIs, built with Spring Boot for scalable, domain-driven architecture.

**Domain** : Projects

**Sub-Domain** : Tasks

## Overview of test strategy

| Layer                                                           | Purpose                                                                    | Framework         | Type        |
|-----------------------------------------------------------------|----------------------------------------------------------------------------|-------------------|-------------|
| **Services**<br/> <br/>ProjectServiceImpl<br/>TaskServiceImpl   | Core logic and branch coverage                                             | Mockito + JUnit 5 | Unit        |
| **Controllers**<br/> <br/>ProjectController<br/>TaskController  | REST endpoints validation, response status codes, and service integration  | Mockito + JUnit 5 | Unit        |
| **Exception handler**<br/> <br/>GlobalExceptionHandler          | Coverage of exception responses                                            | JUnit 5           | Unit        |
| **Repositories**<br/> <br/>ProjectRepository<br/>TaskRepository | Verifies repository behavior and JPA mappings                              | `@DataJpaTest`    | Integration |
