package com.saude360.backendsaude360.services;

import com.saude360.backendsaude360.entities.Orientation;
import com.saude360.backendsaude360.entities.OrientationResponse;
import com.saude360.backendsaude360.exceptions.ObjectNotFoundException;
import com.saude360.backendsaude360.repositories.OrientationResponseRepository;
import com.saude360.backendsaude360.repositories.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrientationResponseService {
    private final OrientationResponseRepository orientationResponseRepository;
    private final OrientationService orientationService;
    private final UserRepository userRepository;

    @Autowired
    public OrientationResponseService(OrientationResponseRepository orientationResponseRepository, OrientationService orientationService, UserService userService, UserRepository userRepository) {
        this.orientationResponseRepository = orientationResponseRepository;
        this.orientationService = orientationService;
        this.userRepository = userRepository;
    }

    public OrientationResponse createResponse(String content, String filePath, Long orientationId) {

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Orientation orientation = orientationService.findById(orientationId);
        var user = userRepository.findByCpf(userDetails.getUsername());

        if(user.isEmpty()) {
            throw new ObjectNotFoundException("Usuário com CPF: " + userDetails.getUsername() + " não encontrado.");
        }

        OrientationResponse response = new OrientationResponse();
        response.setContent(content);
        response.setFilePath(filePath);
        response.setOrientation(orientation);
        response.setUser(user.get());
        response.setCreatedAt(LocalDateTime.now());

        return orientationResponseRepository.save(response);
    }

    public List<OrientationResponse> findAllByOrientationId(Long orientationId) {
        return orientationResponseRepository.findAllByOrientationId(orientationId);
    }
}
