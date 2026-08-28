package app.careerflow.rs.job_application.dto;

import java.time.LocalDate;
import java.util.UUID;

import app.careerflow.rs.job_application.domain.ApplicationStatus;
import jakarta.validation.constraints.NotNull;

public record JobApplicationRequest(
    @NotNull UUID userId,
    @NotNull UUID companyId,
    @NotNull String position,
    @NotNull String jobType,
    String description,
    Integer salary,
    @NotNull ApplicationStatus status,
    LocalDate appliedAt,
    @NotNull String source,
    String postingLink
) {
    
}
