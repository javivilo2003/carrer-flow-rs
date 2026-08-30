package app.careerflow.rs.interview.repository;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import app.careerflow.rs.interview.domain.Interview;

public interface InterviewRepository extends CrudRepository<Interview, UUID>{
}
