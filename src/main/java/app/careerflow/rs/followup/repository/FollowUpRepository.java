package app.careerflow.rs.followup.repository;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import app.careerflow.rs.followup.domain.FollowUp;

public interface FollowUpRepository extends CrudRepository<FollowUp, UUID>{
}
