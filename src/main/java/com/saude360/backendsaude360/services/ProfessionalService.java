package com.saude360.backendsaude360.services;

import com.saude360.backendsaude360.dtos.AddressUpdateDto;
import com.saude360.backendsaude360.dtos.ClinicUpdateDto;
import com.saude360.backendsaude360.dtos.ProfessionalUpdateDto;
import com.saude360.backendsaude360.entities.Address;
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
            List<Clinic> updatedClinics = updateClinics(professionalUpdateDto, professional.getClinics());
            professional.setClinics(updatedClinics);
        }

        return Optional.of(professionalRepository.save(professional));
    }

    private List<Clinic> updateClinics(ProfessionalUpdateDto professionalUpdateDto, List<Clinic> existingClinics) {
        List<Clinic> updatedClinics = new ArrayList<>(existingClinics);

        for (ClinicUpdateDto clinicDto : professionalUpdateDto.clinic()) {
            Optional<Clinic> existingClinicOpt = clinicRepository.findByCnpj(clinicDto.cnpj());

            Clinic clinic;
            if (existingClinicOpt.isPresent()) {
                clinic = existingClinicOpt.get();
                updateClinicFields(clinic, clinicDto);

                if (!updatedClinics.contains(clinic)) {
                    updatedClinics.add(clinic);
                }
            } else {
                clinic = new Clinic();
                BeanUtils.copyProperties(clinicDto, clinic);
                if (clinicDto.address() != null) {
                    clinic.setAddress(new Address());
                    updateAddressFields(clinic.getAddress(), clinicDto.address());
                }
                clinicRepository.save(clinic);
                updatedClinics.add(clinic);
            }
        }
        return updatedClinics;
    }

    private void updateClinicFields(Clinic existingClinic, ClinicUpdateDto clinicDto) {
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
            updateAddressFields(existingClinic.getAddress(), clinicDto.address());
        }
    }

    private void updateAddressFields(Address address, AddressUpdateDto addressUpdateDto) {
        if (addressUpdateDto.cep() != null) {
            address.setCep(addressUpdateDto.cep());
        }
        if (addressUpdateDto.state() != null) {
            address.setState(addressUpdateDto.state());
        }
        if (addressUpdateDto.city() != null) {
            address.setCity(addressUpdateDto.city());
        }
        if (addressUpdateDto.neighborhood() != null) {
            address.setNeighborhood(addressUpdateDto.neighborhood());
        }
        if (addressUpdateDto.street() != null) {
            address.setStreet(addressUpdateDto.street());
        }
        if (addressUpdateDto.number() != null) {
            address.setNumber(addressUpdateDto.number());
        }
        if (addressUpdateDto.complement() != null) {
            address.setComplement(addressUpdateDto.complement());
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
