package app.careerflow.rs.note.repository;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import app.careerflow.rs.note.domain.Note;

public interface NoteRepository extends CrudRepository<Note, UUID>{
}
