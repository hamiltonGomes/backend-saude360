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
                                                              @RequestParam("images") List<MultipartFile> images) throws IOException {
        OrientationResponse orientationResponse = orientationResponseService.createResponse(content, images, orientationId);
        return ResponseEntity.ok(orientationResponse);
    }

    @GetMapping("/{patientId}")
    public ResponseEntity<List<OrientationWithResponsesDto>> findAllResponsesByOrientationId(@PathVariable Long patientId) {
        List<OrientationWithResponsesDto> orientationsWithResponses = orientationService.findAllWithResponsesByPatientId(patientId);

        return ResponseEntity.ok(orientationsWithResponses);
    }
}
