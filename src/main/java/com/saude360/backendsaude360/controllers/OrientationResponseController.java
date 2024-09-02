package com.saude360.backendsaude360.controllers;
import com.saude360.backendsaude360.dtos.OrientationWithResponsesDto;
import com.saude360.backendsaude360.entities.OrientationResponse;
import com.saude360.backendsaude360.services.OrientationResponseService;
import com.saude360.backendsaude360.services.OrientationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/orientation-responses")
public class OrientationResponseController {

    private final OrientationResponseService orientationResponseService;
    private final OrientationService orientationService;

    private static final String UPLOAD_DIR = "uploads/";

    @Autowired
    public OrientationResponseController(OrientationResponseService orientationResponseService, OrientationService orientationService) {
        this.orientationResponseService = orientationResponseService;
        this.orientationService = orientationService;
    }

    @PostMapping("/{orientationId}")
    public ResponseEntity<OrientationResponse> createResponse(@PathVariable Long orientationId,  @RequestParam("content") String content,
                                                              @RequestParam("image") MultipartFile image) throws IOException {

        String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
        Path filePath = Paths.get(UPLOAD_DIR + fileName);
        Files.createDirectories(filePath.getParent());
        Files.write(filePath, image.getBytes());

        OrientationResponse orientationResponse = orientationResponseService.createResponse(content, filePath.toString(), orientationId);
        return ResponseEntity.ok(orientationResponse);
    }

    @GetMapping("/{patientId}")
    public ResponseEntity<List<OrientationWithResponsesDto>> findAllResponsesByOrientationId(@PathVariable Long patientId) {
        List<OrientationWithResponsesDto> orientationsWithResponses = orientationService.findAllWithResponsesByPatientId(patientId);

        return ResponseEntity.ok(orientationsWithResponses);
    }
}
