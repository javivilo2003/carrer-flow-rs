package app.careerflow.rs.contact.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Service;

import app.careerflow.rs.common.exception.ResourceNotFoundException;
import app.careerflow.rs.company.domain.Company;
import app.careerflow.rs.company.repository.CompanyRepository;
import app.careerflow.rs.contact.domain.Contact;
import app.careerflow.rs.contact.dto.ContactDTO;
import app.careerflow.rs.contact.dto.ContactRequest;
import app.careerflow.rs.contact.mapper.ContactMapper;
import app.careerflow.rs.contact.repository.ContactRepository;

@Service
public class ContactService {

    private final ContactRepository repository;
    private final CompanyRepository companyRepository;
    private final ContactMapper mapper;

    public ContactService(ContactRepository repository, CompanyRepository companyRepository, ContactMapper mapper) {
        this.repository = repository;
        this.companyRepository = companyRepository;
        this.mapper = mapper;
    }

    public List<ContactDTO> getAllContacts(){
        return StreamSupport.stream(repository.findAll().spliterator(), false)
            .map(mapper)
            .toList();
    }

    public ContactDTO getContactById(UUID id)throws ResourceNotFoundException{
        return repository.findById(id)
            .map(mapper)
            .orElseThrow(() -> new ResourceNotFoundException(id + " not found."));
    }

    public void addNewContact(ContactRequest request) throws ResourceNotFoundException {
        Company company = companyRepository.findById(request.companyId())
            .orElseThrow(() -> new ResourceNotFoundException(request.companyId() + " not found."));

        Contact contact = mapper.toEntityContact(request, company);

        repository.save(contact);
    }
    
}
