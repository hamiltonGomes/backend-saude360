package com.saude360.backendsaude360.services;

import com.saude360.backendsaude360.dtos.ClinicUpdateDto;
import com.saude360.backendsaude360.dtos.ProfessionalUpdateDto;
import com.saude360.backendsaude360.entities.Clinic;
import com.saude360.backendsaude360.entities.users.Professional;
import com.saude360.backendsaude360.exceptions.DatabaseException;
import com.saude360.backendsaude360.exceptions.ObjectNotFoundException;
import com.saude360.backendsaude360.repositories.ClinicRepository;
import com.saude360.backendsaude360.repositories.users.ProfessionalRepository;
import com.saude360.backendsaude360.utils.BCryptPassword;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProfessionalService {

    private final ProfessionalRepository professionalRepository;
    private final ClinicRepository clinicRepository;
    private static final String NOT_FOUND_MESSAGE = "Profissional com ID: %s não foi encontrado.";

    @Autowired
    public ProfessionalService(ProfessionalRepository professionalRepository, ClinicRepository clinicRepository) {
        this.professionalRepository = professionalRepository;
        this.clinicRepository = clinicRepository;
    }

    public Professional create(Professional professional) {
        return professionalRepository.save(professional);
    }

    public Optional<Professional> update(ProfessionalUpdateDto professionalUpdateDto) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Professional professional = professionalRepository.findByCpf(userDetails.getUsername());

        if (professional == null) {
            throw new ObjectNotFoundException(String.format(NOT_FOUND_MESSAGE, userDetails.getUsername()));
        }

        var oldPassword = professional.getPassword();
        BeanUtils.copyProperties(professionalUpdateDto, professional, "clinics", "password");

        if (professionalUpdateDto.password() == null) {
            professional.setPassword(oldPassword);
        } else {
            professional.setPassword(BCryptPassword.encryptPassword(professionalUpdateDto.password()));
        }

        if (professionalUpdateDto.clinic() != null) {
            List<Clinic> updatedClinics = new ArrayList<>();

            for (ClinicUpdateDto clinicDto : professionalUpdateDto.clinic()) {
                Optional<Clinic> existingClinicOpt = clinicRepository.findByCnpj(clinicDto.cnpj());

                if (existingClinicOpt.isPresent()) {
                    Clinic existingClinic = existingClinicOpt.get();

                    if (clinicDto.phoneNumber() != null) {
                        existingClinic.setPhoneNumber(clinicDto.phoneNumber());
                    }
                    if (clinicDto.telephoneNumber() != null) {
                        existingClinic.setTelephoneNumber(clinicDto.telephoneNumber());
                    }
                    if (clinicDto.cnesNumber() != null) {
                        existingClinic.setCnesNumber(clinicDto.cnesNumber());
                    }
                    if (clinicDto.address() != null) {
                        if (clinicDto.address().cep() != null) {
                            existingClinic.getAddress().setCep(clinicDto.address().cep());
                        }
                        if (clinicDto.address().state() != null) {
                            existingClinic.getAddress().setState(clinicDto.address().state());
                        }
                        if (clinicDto.address().city() != null) {
                            existingClinic.getAddress().setCity(clinicDto.address().city());
                        }
                        if (clinicDto.address().neighborhood() != null) {
                            existingClinic.getAddress().setNeighborhood(clinicDto.address().neighborhood());
                        }
                        if (clinicDto.address().street() != null) {
                            existingClinic.getAddress().setStreet(clinicDto.address().street());
                        }
                        if (clinicDto.address().number() != null) {
                            existingClinic.getAddress().setNumber(clinicDto.address().number());
                        }
                        if (clinicDto.address().complement() != null) {
                            existingClinic.getAddress().setComplement(clinicDto.address().complement());
                        }
                    }

                    updatedClinics.add(existingClinic);
                }
            }

            professional.setClinics(updatedClinics);
        }

        return Optional.of(professionalRepository.save(professional));
    }


    public Professional findById(Long id) {
        Optional<Professional> professional = professionalRepository.findById(id);
        return professional.orElseThrow(() -> new ObjectNotFoundException(String.format(NOT_FOUND_MESSAGE, id)));
    }

    public List<Professional> findAll() {
        return professionalRepository.findAll();
    }

    public void delete(Long id) {
        try {
            professionalRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ObjectNotFoundException(String.format(String.format(NOT_FOUND_MESSAGE, id)));
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException(e.getMessage());
        }
    }
}
