package app.careerflow.rs.user.repository;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import app.careerflow.rs.user.domain.User;

public interface UserRepository extends CrudRepository<User, UUID>{
}
