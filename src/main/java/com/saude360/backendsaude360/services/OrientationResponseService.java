package com.saude360.backendsaude360.services;

import com.saude360.backendsaude360.dtos.ImgurDto;
import com.saude360.backendsaude360.entities.Orientation;
import com.saude360.backendsaude360.entities.OrientationResponse;
import com.saude360.backendsaude360.exceptions.ObjectNotFoundException;
import com.saude360.backendsaude360.repositories.OrientationResponseRepository;
import com.saude360.backendsaude360.repositories.users.UserRepository;
import com.saude360.backendsaude360.services.external.ImgurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
public class OrientationResponseService {
    private final OrientationResponseRepository orientationResponseRepository;
    private final OrientationService orientationService;
    private final UserRepository userRepository;
    private final ImgurService imgurService;

    @Autowired
    public OrientationResponseService(OrientationResponseRepository orientationResponseRepository, OrientationService orientationService, UserService userService, UserRepository userRepository, ImgurService imgurService) {
        this.orientationResponseRepository = orientationResponseRepository;
        this.orientationService = orientationService;
        this.userRepository = userRepository;
        this.imgurService = imgurService;
    }

    public OrientationResponse createResponse(String content, List<MultipartFile> images, Long orientationId) throws IOException {

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Orientation orientation = orientationService.findById(orientationId);
        var user = userRepository.findByCpf(userDetails.getUsername());

        if(user.isEmpty()) {
            throw new ObjectNotFoundException("Usuário com CPF: " + userDetails.getUsername() + " não encontrado.");
        }

        List<String> urlImages = new ArrayList<>();

        for (MultipartFile image : images) {
            String imageBase64 = Base64.getEncoder().encodeToString(image.getBytes());
            ImgurDto response = imgurService.uploadImage(imageBase64);
            urlImages.add(response.data().link());
        }

        OrientationResponse response = new OrientationResponse();
        response.setContent(content);
        response.setFilePath(urlImages);
        response.setOrientation(orientation);
        response.setUser(user.get());
        response.setCreatedAt(LocalDateTime.now());

        return orientationResponseRepository.save(response);
    }

    public List<OrientationResponse> findAllByOrientationId(Long orientationId) {
        return orientationResponseRepository.findAllByOrientationId(orientationId);
    }
}
