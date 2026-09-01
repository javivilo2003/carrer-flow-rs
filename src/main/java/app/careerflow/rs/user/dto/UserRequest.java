package app.careerflow.rs.user.dto;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record UserRequest(
    @NotNull(message = "Username not found") String username,
    LocalDate dob,
    String cv
) {
    
}
