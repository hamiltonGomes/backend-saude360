package com.saude360.backendsaude360.controllers;
import com.saude360.backendsaude360.dtos.OrientationWithResponsesDto;
import com.saude360.backendsaude360.entities.OrientationResponse;
import com.saude360.backendsaude360.services.OrientationResponseService;
import com.saude360.backendsaude360.services.OrientationService;
import com.saude360.backendsaude360.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/orientation-responses")
public class OrientationResponseController {

    private final OrientationResponseService orientationResponseService;
    private final OrientationService orientationService;
    private final UserService userService;

    private static final String UPLOAD_DIR = "uploads/";

    @Autowired
    public OrientationResponseController(OrientationResponseService orientationResponseService, OrientationService orientationService, UserService userService) {
        this.orientationResponseService = orientationResponseService;
        this.orientationService = orientationService;
        this.userService = userService;
    }

    @PostMapping("/{orientationId}")
    public ResponseEntity<OrientationResponse> createResponse(@PathVariable Long orientationId,  @RequestParam("content") String content,
                                                              @RequestParam(value = "images", required = false) List<MultipartFile> images) throws IOException {
        OrientationResponse orientationResponse = orientationResponseService.createResponse(content, images, orientationId);
        return ResponseEntity.ok(orientationResponse);
    }

    @GetMapping("/{patientId}")
    public ResponseEntity<List<OrientationWithResponsesDto>> findAllResponsesByOrientationId(@PathVariable Long patientId) {
        List<OrientationWithResponsesDto> orientationsWithResponses = orientationService.findAllWithResponsesByPatientId(patientId);

        return ResponseEntity.ok(orientationsWithResponses);
    }

    @GetMapping("/")
    public ResponseEntity<List<OrientationWithResponsesDto>> findAllResponses() {
        var userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var user = userService.findByCpf(userDetails.getUsername());

        List<OrientationWithResponsesDto> orientationsWithResponses = orientationService.findAllWithResponsesByPatientId(user.getId());

        return ResponseEntity.ok(orientationsWithResponses);
    }
}
