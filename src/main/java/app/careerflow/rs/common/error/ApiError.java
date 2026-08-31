package app.careerflow.rs.common.error;

import java.time.Instant;
import java.util.Map;

public record ApiError(
    Instant timeStamp,
    int status,
    String error,
    String message,
    String path,
    Map<String, String> fieldErrors
) {
    
}
