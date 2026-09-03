package app.careerflow.rs.followup.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.careerflow.rs.common.exception.ResourceNotFoundException;
import app.careerflow.rs.followup.dto.FollowUpDTO;
import app.careerflow.rs.followup.dto.FollowUpRequest;
import app.careerflow.rs.followup.service.FollowUpService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/followups")
public class FollowUpController {
    
    private final FollowUpService service;

    public FollowUpController(FollowUpService service) {
        this.service = service;
    }

    @GetMapping()
    public List<FollowUpDTO> getAllFollowUps(){
        return service.getAllFollowUps();
    }

    @GetMapping("{id}")
    public FollowUpDTO getFollowUpById(@PathVariable UUID id) throws ResourceNotFoundException{
        return service.getFollowUpById(id);
    }

    @PostMapping()
    public void addNewFollowUp(@Valid @RequestBody FollowUpRequest request) throws ResourceNotFoundException{
        service.addNewFollowUp(request);
    }

}
