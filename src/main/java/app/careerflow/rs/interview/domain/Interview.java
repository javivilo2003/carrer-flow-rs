package app.careerflow.rs.interview.domain;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "interviews")
public class Interview {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID jobApplicationId;

    private String stage;

    private InterviewStatus status;

    private LocalDate interviewDate;

    private String notes;
}
