package app.careerflow.rs.interview.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Service;

import app.careerflow.rs.common.exception.ResourceNotFoundException;
import app.careerflow.rs.interview.domain.Interview;
import app.careerflow.rs.interview.dto.InterviewDTO;
import app.careerflow.rs.interview.dto.InterviewRequest;
import app.careerflow.rs.interview.mapper.InterviewMapper;
import app.careerflow.rs.interview.repository.InterviewRepository;
import app.careerflow.rs.job_application.domain.JobApplication;
import app.careerflow.rs.job_application.repository.JobApplicationRepository;

@Service
public class InterviewService {
    
    private final InterviewRepository repository;
    private final JobApplicationRepository jobApplicationRepository;
    private final InterviewMapper mapper;
    

    public InterviewService(InterviewRepository repository,JobApplicationRepository jobApplicationRepository, InterviewMapper interviewMapper) {
        this.repository = repository;
        this.jobApplicationRepository = jobApplicationRepository;
        this.mapper = interviewMapper;
    }

    public List<InterviewDTO> getAllInterviews(){
        return StreamSupport.stream(repository.findAll().spliterator(), false)
            .map(mapper)
            .toList();
    }

    public InterviewDTO getInterviewById(UUID id) throws Exception{
        return repository.findById(id)
            .map(mapper)
            .orElseThrow(() -> new ResourceNotFoundException(id + " not found"));
    }

    public void addNewInterview(InterviewRequest request) throws ResourceNotFoundException{
        JobApplication application = jobApplicationRepository.findById(request.jobApplicationId())
            .orElseThrow(() -> new ResourceNotFoundException(request.jobApplicationId() + " not found."));

        Interview interview = mapper.toEntityInterview(request, application);
        repository.save(interview);
    }
}
