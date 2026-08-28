package app.careerflow.rs.company.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.careerflow.rs.company.domain.Company;
import app.careerflow.rs.company.service.CompanyService;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;





@RestController
@RequestMapping("/api/companies")
public class CompanyController {
    
    private CompanyService service;

    public CompanyController(CompanyService service) {
        this.service = service;
    }

    @GetMapping()
    public List<Company> getAllCompanies() {
        return service.getAllCompanies();
    }
    
    @GetMapping("{id}")
    public Company getCompanyById(@PathVariable UUID id) throws Exception{
        return service.getCompanyById(id);
    }

    @PostMapping()
    public void createNewCompany(Company company){
        service.addNewComapny(company);
    }

}
