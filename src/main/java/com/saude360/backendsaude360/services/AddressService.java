package com.saude360.backendsaude360.services;

import com.saude360.backendsaude360.dtos.AddressDto;
import com.saude360.backendsaude360.entities.Address;
import com.saude360.backendsaude360.exceptions.ObjectNotFoundException;
import com.saude360.backendsaude360.repositories.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressService {
    private final AddressRepository addressRepository;

    @Autowired

    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    public void update(Long id, AddressDto addressDto) {
        Address existingAddress = addressRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Address with ID: " + id + " was not found."));

        if (addressDto.cep() != null) {
            existingAddress.setCep(addressDto.cep());
        }
        if (addressDto.state() != null) {
            existingAddress.setState(addressDto.state());
        }
        if (addressDto.city() != null) {
            existingAddress.setCity(addressDto.city());
        }
        if (addressDto.neighborhood() != null) {
            existingAddress.setNeighborhood(addressDto.neighborhood());
        }
        if (addressDto.street() != null) {
            existingAddress.setStreet(addressDto.street());
        }
        if (addressDto.number() != null) {
            existingAddress.setNumber(addressDto.number());
        }
        if (addressDto.complement() != null) {
            existingAddress.setComplement(addressDto.complement());
        }

        addressRepository.save(existingAddress);
    }
}
