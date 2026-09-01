package app.careerflow.rs.job_application.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Service;

import app.careerflow.rs.common.error.ResourceNotFoundException;
import app.careerflow.rs.company.domain.Company;
import app.careerflow.rs.company.repository.CompanyRepository;
import app.careerflow.rs.job_application.domain.JobApplication;
import app.careerflow.rs.job_application.dto.JobApplicationDTO;
import app.careerflow.rs.job_application.dto.JobApplicationRequest;
import app.careerflow.rs.job_application.mapper.JobApplicationDTOMapper;
import app.careerflow.rs.job_application.repository.JobApplicationRepository;
import app.careerflow.rs.user.domain.User;
import app.careerflow.rs.user.repository.UserRepository;

@Service
public class JobApplicationService {
    
    private final JobApplicationRepository repository;
    private final JobApplicationDTOMapper mapper;
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;

    public JobApplicationService(JobApplicationRepository repository, JobApplicationDTOMapper mapper, CompanyRepository companyRepository, UserRepository userRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
    }

    public List<JobApplicationDTO> getAllJobApplications(){
        return StreamSupport.stream(repository.findAll().spliterator(), false)
            .map(mapper)
            .toList();
    }

    public JobApplicationDTO getJobApplicationById(UUID id) throws Exception{
        return repository.findById(id)
            .map(mapper)    
            .orElseThrow(() -> new ResourceNotFoundException(id + " not found."));
    }

    public void addNewJobApplication(JobApplicationRequest request) throws Exception{
        Company company = companyRepository.findById(request.companyId())
            .orElseThrow(() -> new ResourceNotFoundException(request.companyId() + " not found."));

        User user = userRepository.findById(request.userid())
            .orElseThrow(() -> new ResourceNotFoundException(request.userid() + " not found."));

        JobApplication application = mapper.toEntity(request, company, user);

        repository.save(application);
    }
}
