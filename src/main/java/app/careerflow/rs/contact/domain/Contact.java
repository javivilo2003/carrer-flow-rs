package app.careerflow.rs.contact.domain;

import java.util.UUID;

import org.hibernate.annotations.ManyToAny;

import app.careerflow.rs.company.domain.Company;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
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
@Table(name = "contacts")
public class Contact {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToAny()
    @JoinColumn(name = "company_id", nullable = false)
    @NotNull
    private Company company;

    @NotNull
    private String name;

    private String phone;

    private String email;

    @NotNull
    @Column(unique = true)
    private String jobRole;

}
