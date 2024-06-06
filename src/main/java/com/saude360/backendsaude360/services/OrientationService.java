package com.saude360.backendsaude360.services;

import com.saude360.backendsaude360.dtos.OrientationDto;
import com.saude360.backendsaude360.entities.Orientation;
import com.saude360.backendsaude360.entities.users.Patient;
import com.saude360.backendsaude360.exceptions.ObjectNotFoundException;
import com.saude360.backendsaude360.repositories.OrientationRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OrientationService {
    private final OrientationRepository orientationRepository;
    private static final String NOT_FOUND_MESSAGE = "Orientação com ID: %d não foi encontrada.";

    @Autowired
    public OrientationService(OrientationRepository orientationRepository) {
        this.orientationRepository = orientationRepository;
    }

    public Orientation create(OrientationDto orientationDto, Patient patient) {
        Orientation orientation = new Orientation();
        orientation.setPatient(patient);
        BeanUtils.copyProperties(orientationDto, orientation, "id");
        return orientationRepository.save(orientation);
    }

    public Orientation findById(Long id) {
        Optional<Orientation> orientation = orientationRepository.findById(id);
        return orientation.orElseThrow(() -> new ObjectNotFoundException(String.format(NOT_FOUND_MESSAGE, id)));
    }

    public List<Orientation> findAll() {
        return orientationRepository.findAll();
    }

    public List<Orientation> findAllByPatientId(Long patientId) {
        return orientationRepository.findAllByPatientId(patientId);
    }

    public void delete(Long orientationId) {
        if (!orientationRepository.existsById(orientationId)) {
            throw new ObjectNotFoundException(String.format(NOT_FOUND_MESSAGE, orientationId));
        }
        orientationRepository.deleteById(orientationId);
    }

    public Optional<Orientation> update(Long id, OrientationDto orientationDto) {
        return orientationRepository.findById(id).map(orientation -> {
            if (orientationDto.description() != null) {
                orientation.setDescription(orientationDto.description());
            }
            if (orientationDto.completed() != null) {
                orientation.setCompleted(orientationDto.completed());
            }
            if (orientationDto.idImages() != null && !orientationDto.idImages().isEmpty()) {
                orientation.setIdImages(orientationDto.idImages());
            }
            orientation.setUpdatedAt(ZonedDateTime.now(ZoneId.of("America/Sao_Paulo")));
            return orientationRepository.save(orientation);
        });
    }
}