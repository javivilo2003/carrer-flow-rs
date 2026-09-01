package app.careerflow.rs.note.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Service;

import app.careerflow.rs.common.error.ResourceNotFoundException;
import app.careerflow.rs.job_application.domain.JobApplication;
import app.careerflow.rs.job_application.repository.JobApplicationRepository;
import app.careerflow.rs.note.domain.Note;
import app.careerflow.rs.note.dto.NoteDTO;
import app.careerflow.rs.note.dto.NoteRequest;
import app.careerflow.rs.note.mapper.NoteMapper;
import app.careerflow.rs.note.repository.NoteRepository;

@Service
public class NoteService {
    
    private final NoteRepository repository;
    private final JobApplicationRepository jobApplicationRepository;
    private final NoteMapper mapper;
    
    public NoteService(NoteRepository repository, JobApplicationRepository jobApplicationRepository, NoteMapper mapper) {
        this.repository = repository;
        this.jobApplicationRepository = jobApplicationRepository;
        this.mapper = mapper;
    }

    public List<NoteDTO> getAllNotes(){
        return StreamSupport.stream(repository.findAll().spliterator(), false)
            .map(mapper)
            .toList();
    }

    public NoteDTO getNoteById(UUID id) throws ResourceNotFoundException{
        return repository.findById(id)
            .map(mapper)
            .orElseThrow(() -> new ResourceNotFoundException(id + " not found"));
    }

    public void addNewNote(NoteRequest request) throws ResourceNotFoundException{
        JobApplication application = jobApplicationRepository.findById(request.jobApplicationId())
            .orElseThrow(() -> new ResourceNotFoundException(request.jobApplicationId() + " not found."));

        Note note = mapper.toEntityNote(request, application);
        repository.save(note);
    }
    
}
