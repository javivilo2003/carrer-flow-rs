package app.careerflow.rs.followup.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Service;

import app.careerflow.rs.common.error.ResourceNotFoundException;
import app.careerflow.rs.followup.domain.FollowUp;
import app.careerflow.rs.followup.dto.FollowUpDTO;
import app.careerflow.rs.followup.dto.FollowUpRequest;
import app.careerflow.rs.followup.mapper.FollowUpMapper;
import app.careerflow.rs.followup.repository.FollowUpRepository;
import app.careerflow.rs.job_application.domain.JobApplication;
import app.careerflow.rs.job_application.repository.JobApplicationRepository;

@Service
public class FollowUpService {
    
    private final FollowUpRepository repository;
    private final JobApplicationRepository jobApplicationRepository;
    private final FollowUpMapper mapper;

    public FollowUpService(FollowUpRepository repository, JobApplicationRepository jobApplicationRepository,
            FollowUpMapper mapper) {
        this.repository = repository;
        this.jobApplicationRepository = jobApplicationRepository;
        this.mapper = mapper;
    }

    public List<FollowUpDTO> getAllFollowUps(){
        return StreamSupport.stream(repository.findAll().spliterator(), false)
            .map(mapper)
            .toList();
    }

    public FollowUpDTO getFollowUpById(UUID id) throws ResourceNotFoundException{
        return repository.findById(id)
            .map(mapper)
            .orElseThrow(() -> new ResourceNotFoundException(id + " not found"));
    }
    
    public void addNewFollowUp(FollowUpRequest request)throws ResourceNotFoundException{
        JobApplication application = jobApplicationRepository.findById(request.applicationId())
            .orElseThrow(() -> new ResourceNotFoundException(request.applicationId() + " not found."));

        FollowUp followUp = mapper.toEntityFollowUp(request, application);

        repository.save(followUp);
    }
}
