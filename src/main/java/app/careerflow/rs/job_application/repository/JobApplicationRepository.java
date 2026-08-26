package app.careerflow.rs.job_application.repository;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;


import app.careerflow.rs.job_application.domain.JobApplication;

public interface JobApplicationRepository extends CrudRepository<JobApplication, UUID>{
}
