package com.saude360.backendsaude360.services;

import com.saude360.backendsaude360.entities.File;
import com.saude360.backendsaude360.entities.Orientation;
import com.saude360.backendsaude360.entities.OrientationResponse;
import com.saude360.backendsaude360.exceptions.ObjectNotFoundException;
import com.saude360.backendsaude360.repositories.OrientationResponseRepository;
import com.saude360.backendsaude360.repositories.users.UserRepository;
import com.saude360.backendsaude360.services.external.AzureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrientationResponseService {
    private final OrientationResponseRepository orientationResponseRepository;
    private final OrientationService orientationService;
    private final UserRepository userRepository;
    private final AzureService azureService;

    @Autowired
    public OrientationResponseService(OrientationResponseRepository orientationResponseRepository, OrientationService orientationService, UserRepository userRepository, AzureService azureService) {
        this.orientationResponseRepository = orientationResponseRepository;
        this.orientationService = orientationService;
        this.userRepository = userRepository;
        this.azureService = azureService;
    }

    public OrientationResponse createResponse(String content, List<MultipartFile> images, Long orientationId) throws IOException {

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Orientation orientation = orientationService.findById(orientationId);
        var user = userRepository.findByCpf(userDetails.getUsername());

        if(user.isEmpty()) {
            throw new ObjectNotFoundException("Usuário com CPF: " + userDetails.getUsername() + " não encontrado.");
        }

        List<File> files = new ArrayList<>();

        for (MultipartFile image : images) {
                String fileName = azureService.uploadFile(image);
                File file = new File();
                file.setName(fileName);
                file.setType(image.getContentType());
                file.setSize(image.getSize());
                files.add(file);
        }

        OrientationResponse response = new OrientationResponse(content, files, orientation, user.get(), LocalDateTime.now());

        return orientationResponseRepository.save(response);
    }

    public List<OrientationResponse> findAllByOrientationId(Long orientationId) {
        return orientationResponseRepository.findAllByOrientationId(orientationId);
    }
}
