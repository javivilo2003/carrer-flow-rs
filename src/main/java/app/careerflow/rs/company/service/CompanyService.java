package app.careerflow.rs.company.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import app.careerflow.rs.common.exception.ResourceNotFoundException;
import app.careerflow.rs.company.domain.Company;
import app.careerflow.rs.company.repository.CompanyRepository;

@Service
public class CompanyService {
    
    private final CompanyRepository repository;

    public CompanyService(CompanyRepository repository) {
        this.repository = repository;
    }

    public List<Company> getAllCompanies(){
        return (List<Company>) repository.findAll();
    }

    public Company getCompanyById(UUID id) throws Exception{
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id + " not found."));
    }

    public void addNewComapny(Company company ){
        repository.save(company);
    }
}
