package com.saude360.backendsaude360.services;

import com.saude360.backendsaude360.dtos.ProfessionalDto;
import com.saude360.backendsaude360.entities.users.Professional;
import com.saude360.backendsaude360.exceptions.DatabaseException;
import com.saude360.backendsaude360.exceptions.ObjectNotFoundException;
import com.saude360.backendsaude360.repositories.users.ProfessionalRepository;
import com.saude360.backendsaude360.utils.BCryptPassword;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProfessionalService {

    private final ProfessionalRepository professionalRepository;
    private static final String NOT_FOUND_MESSAGE = "Profissional com ID: %s não foi encontrado.";

    @Autowired
    public ProfessionalService(ProfessionalRepository professionalRepository) {
        this.professionalRepository = professionalRepository;
    }

    public Professional create(Professional professional) {
        return professionalRepository.save(professional);
    }

    public Optional<Professional> update(ProfessionalDto professionalDto) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Professional professional = professionalRepository.findByCpf(userDetails.getUsername());

        if (professional != null) {
            var oldPassword = professional.getPassword();
            BeanUtils.copyProperties(professionalDto, professional);
            if(professionalDto.password() == null) {
                professional.setPassword(oldPassword);
            } else {
                professional.setPassword(BCryptPassword.encryptPassword(professional));
            }
            return Optional.of(professionalRepository.save(professional));
        } else {
            throw new ObjectNotFoundException(String.format(String.format(NOT_FOUND_MESSAGE, userDetails.getUsername())));
        }
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
