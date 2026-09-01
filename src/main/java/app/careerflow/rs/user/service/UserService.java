package app.careerflow.rs.user.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Service;

import app.careerflow.rs.common.error.ResourceNotFoundException;
import app.careerflow.rs.user.domain.User;
import app.careerflow.rs.user.dto.UserDTO;
import app.careerflow.rs.user.dto.UserRequest;
import app.careerflow.rs.user.mapper.UserMapper;
import app.careerflow.rs.user.repository.UserRepository;

@Service
public class UserService {
    
    private final UserRepository repository;
    private final UserMapper mapper;


    public UserService(UserRepository repository, UserMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<UserDTO> getAllUsers(){
        return StreamSupport.stream(repository.findAll().spliterator(), false)
            .map(mapper)
            .toList();
    }

    public UserDTO getUserById(UUID id) throws ResourceNotFoundException{
        return repository.findById(id)
            .map(mapper)
            .orElseThrow(() ->
                new ResourceNotFoundException("User with id: " + id + " not found"));
    }

    public void addNewUser(UserRequest request){
        User user = mapper.toEntityUser(request);
        repository.save(user);
    }
}
