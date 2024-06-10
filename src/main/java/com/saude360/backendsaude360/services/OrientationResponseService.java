package com.saude360.backendsaude360.services;

import com.saude360.backendsaude360.dtos.OrientationResponseDto;
import com.saude360.backendsaude360.entities.Orientation;
import com.saude360.backendsaude360.entities.OrientationResponse;
import com.saude360.backendsaude360.entities.users.User;
import com.saude360.backendsaude360.repositories.OrientationResponseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrientationResponseService {
    private final OrientationResponseRepository orientationResponseRepository;
    private final OrientationService orientationService;
    private final UserService userService;

    @Autowired
    public OrientationResponseService(OrientationResponseRepository orientationResponseRepository, OrientationService orientationService, UserService userService) {
        this.orientationResponseRepository = orientationResponseRepository;
        this.orientationService = orientationService;
        this.userService = userService;
    }

    public OrientationResponse createResponse(OrientationResponseDto orientationResponseDto, Long orientationId) {
        Orientation orientation = orientationService.findById(orientationId);
        User user = userService.findById(orientationResponseDto.userId());

        OrientationResponse response = new OrientationResponse();
        response.setContent(orientationResponseDto.content());
        response.setOrientation(orientation);
        response.setUser(user);
        response.setCreatedAt(LocalDateTime.now());

        return orientationResponseRepository.save(response);
    }

    public List<OrientationResponse> findAllByOrientationId(Long orientationId) {
        return orientationResponseRepository.findAllByOrientationId(orientationId);
    }
}
