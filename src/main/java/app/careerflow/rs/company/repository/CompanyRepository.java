package app.careerflow.rs.company.repository;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import app.careerflow.rs.company.domain.Company;

public interface CompanyRepository extends CrudRepository<Company, UUID>{
    
}
