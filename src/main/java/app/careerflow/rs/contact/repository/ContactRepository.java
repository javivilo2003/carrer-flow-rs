package app.careerflow.rs.contact.repository;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import app.careerflow.rs.contact.domain.Contact;

public interface ContactRepository extends CrudRepository<Contact, UUID>{    
}
