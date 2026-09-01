package app.careerflow.rs.interview.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Service;

import app.careerflow.rs.common.error.ResourceNotFoundException;
import app.careerflow.rs.interview.domain.Interview;
import app.careerflow.rs.interview.dto.InterviewDTO;
import app.careerflow.rs.interview.dto.InterviewRequest;
import app.careerflow.rs.interview.mapper.InterviewMapper;
import app.careerflow.rs.interview.repository.InterviewRepository;

@Service
public class InterviewService {
    
    private final InterviewRepository repository;
        private final InterviewMapper mapper;

    public InterviewService(InterviewRepository repository, InterviewMapper interviewMapper) {
        this.repository = repository;
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

    public void addNewInterview(InterviewRequest request){
        Interview interview = mapper.toEntityInterview(request);
        repository.save(interview);
    }
}
