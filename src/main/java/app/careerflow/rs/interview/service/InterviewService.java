package app.careerflow.rs.interview.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import app.careerflow.rs.common.error.ResourceNotFoundException;
import app.careerflow.rs.interview.domain.Interview;
import app.careerflow.rs.interview.repository.InterviewRepository;

@Service
public class InterviewService {
    
    private final InterviewRepository repository;

    public InterviewService(InterviewRepository repository) {
        this.repository = repository;
    }

    public List<Interview> getAllInterviews(){
        return (List<Interview>) repository.findAll();
    }

    public Interview getInterviewById(UUID id) throws Exception{
        return repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(id + " not found"));
    }

    public void addNewInterview(Interview interview){
        repository.save(interview);
    }
}
