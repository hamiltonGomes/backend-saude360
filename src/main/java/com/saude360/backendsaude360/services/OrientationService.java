package com.saude360.backendsaude360.services;

import com.saude360.backendsaude360.dtos.FileDto;
import com.saude360.backendsaude360.dtos.OrientationDto;
import com.saude360.backendsaude360.dtos.OrientationResponseReturnDto;
import com.saude360.backendsaude360.dtos.OrientationWithResponsesDto;
import com.saude360.backendsaude360.entities.File;
import com.saude360.backendsaude360.entities.Orientation;
import com.saude360.backendsaude360.entities.OrientationResponse;
import com.saude360.backendsaude360.entities.users.Patient;
import com.saude360.backendsaude360.exceptions.ObjectNotFoundException;
import com.saude360.backendsaude360.repositories.OrientationRepository;
import com.saude360.backendsaude360.repositories.OrientationResponseRepository;
import com.saude360.backendsaude360.services.external.AzureService;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OrientationService {
    private final OrientationRepository orientationRepository;
    private final OrientationResponseRepository orientationResponseRepository;
    private final AzureService azureService;
    private static final String NOT_FOUND_MESSAGE = "Orientação com ID: %d não foi encontrada.";

    @Autowired
    public OrientationService(OrientationRepository orientationRepository, OrientationResponseRepository orientationResponseRepository, AzureService azureService) {
        this.orientationRepository = orientationRepository;
        this.orientationResponseRepository = orientationResponseRepository;

        this.azureService = azureService;
    }

    public List<OrientationWithResponsesDto> findAllWithResponsesByPatientId(Long patientId) {
        List<Orientation> orientations = orientationRepository.findAllByPatientId(patientId);
        return orientations.stream().map(this::mapToOrientationWithResponsesDto).toList();
    }

    private OrientationWithResponsesDto mapToOrientationWithResponsesDto(Orientation orientation) {
        List<OrientationResponse> responses = orientationResponseRepository.findAllByOrientationId(orientation.getId());
        List<OrientationResponseReturnDto> orientationResponses = responses.stream()
                .map(this::mapToOrientationResponseReturnDto)
                .toList();

        return new OrientationWithResponsesDto(
                orientation.getId(),
                orientation.getTitle(),
                orientation.getDescription(),
                orientationResponses
        );
    }


    private OrientationResponseReturnDto mapToOrientationResponseReturnDto(OrientationResponse response) {
        List<FileDto> files = new ArrayList<>();
       for(File filePath : response.getFiles()) {
            try {
                FileDto fileDto = new FileDto(azureService.downloadFile(filePath.getName()), filePath.getType(), filePath.getSize());
                files.add(fileDto);
            } catch (IOException e) {
                e.printStackTrace();
            }
       }
        return new OrientationResponseReturnDto(response.getContent(), response.getOrientation(), files, response.getUser(), response.getCreatedAt());
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

            if (orientationDto.title() != null) {
                orientation.setTitle(orientationDto.title());
            }
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