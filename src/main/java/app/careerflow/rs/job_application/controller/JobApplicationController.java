package app.careerflow.rs.job_application.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.careerflow.rs.job_application.dto.JobApplicationDTO;
import app.careerflow.rs.job_application.dto.JobApplicationRequest;
import app.careerflow.rs.job_application.service.JobApplicationService;
import jakarta.validation.Valid;



@RestController
@RequestMapping("/api/applications")
public class JobApplicationController {

    private final JobApplicationService service;

    public JobApplicationController(JobApplicationService service) {
        this.service = service;
    }
    
    @GetMapping()
    public List<JobApplicationDTO> getAllApplications(){
        return service.getAllJobApplications();
    }

    @GetMapping("{id}")
    public JobApplicationDTO getApplicationById(@PathVariable UUID id) throws Exception {
        return service.getJobApplicationById(id);
    }
    
    @PostMapping()
    public void saveApplication(@Valid @RequestBody JobApplicationRequest request) throws Exception{
        service.addNewJobApplication(request);
    }

}
