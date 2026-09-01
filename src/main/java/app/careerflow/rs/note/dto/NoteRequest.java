package app.careerflow.rs.note.dto;

import java.util.UUID;

public record NoteRequest(
    UUID jobApplicationId,
    String content
) {
    
}
