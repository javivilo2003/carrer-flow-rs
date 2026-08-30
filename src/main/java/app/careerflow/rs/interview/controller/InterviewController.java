package app.careerflow.rs.interview.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.careerflow.rs.interview.domain.Interview;
import app.careerflow.rs.interview.service.InterviewService;

@RestController
@RequestMapping("/api/interviews")
public class InterviewController {
    
    private final InterviewService service;

    public InterviewController(InterviewService service) {
        this.service = service;
    }

    @GetMapping()
    public List<Interview> getAllInterviews(){
        return service.getAllInterviews();
    }

    @GetMapping("{id}")
    public Interview getInterviewById(@PathVariable UUID id) throws Exception{
        return service.getInterviewById(id);
    }

    @PostMapping
    public void addNewInterview(Interview interview){
        service.addNewInterview(interview);
    }
}
