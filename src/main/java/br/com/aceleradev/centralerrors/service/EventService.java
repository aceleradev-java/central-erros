package br.com.aceleradev.centralerrors.service;

import static br.com.aceleradev.centralerrors.EventSpecification.descriptionContains;
import static br.com.aceleradev.centralerrors.EventSpecification.hasDate;
import static br.com.aceleradev.centralerrors.EventSpecification.hasLevel;
import static br.com.aceleradev.centralerrors.EventSpecification.hasLog;
import static br.com.aceleradev.centralerrors.EventSpecification.sourceContains;
import static org.springframework.data.jpa.domain.Specification.where;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import br.com.aceleradev.centralerrors.entity.Event;
import br.com.aceleradev.centralerrors.enums.Level;
import br.com.aceleradev.centralerrors.exception.EntityNotFound;
import br.com.aceleradev.centralerrors.repository.EventRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class EventService implements EventServiceInterface {
    
    private EventRepository repository;

    @Override
    public Event save(Event event) {
        event.setDate(LocalDateTime.now());
        return repository.save(event);
    }

    @Override
    public Event findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFound("Event not found"));
    }

    @Override
    public Page<Event> findAll(String log, String description, Level level, String source, LocalDateTime date, Pageable pageable){
        Specification<Event> specificationLog = hasLog(log);
        Specification<Event> specificationDescription = descriptionContains(description);
        Specification<Event> specificationLevel = hasLevel(level);
        Specification<Event> specificationSource = sourceContains(source);
        Specification<Event> specificationDate = hasDate(date);
        return repository.findAll(where(specificationLog).and(specificationDescription)
                .and(specificationLevel)
                .and(specificationSource)
                .and(specificationDate),
        pageable );
    }

    @Override
    public void delete(Long id) {
        Event event = repository.findById(id)
                .orElseThrow(() -> new EntityNotFound("Event not found"));
        repository.delete(event);
    }
    
}
