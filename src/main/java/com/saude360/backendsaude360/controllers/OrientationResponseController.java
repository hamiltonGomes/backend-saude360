package com.saude360.backendsaude360.controllers;
import com.saude360.backendsaude360.dtos.OrientationResponseDto;
import com.saude360.backendsaude360.entities.OrientationResponse;
import com.saude360.backendsaude360.services.OrientationResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orientation-responses")
public class OrientationResponseController {

    private final OrientationResponseService orientationResponseService;

    @Autowired
    public OrientationResponseController(OrientationResponseService orientationResponseService) {
        this.orientationResponseService = orientationResponseService;
    }

    @PostMapping("/{orientationId}")
    public ResponseEntity<OrientationResponse> createResponse(@PathVariable Long orientationId, @RequestBody OrientationResponseDto orientationResponseDto) {
        OrientationResponse orientationResponse = orientationResponseService.createResponse(orientationResponseDto, orientationId);
        return ResponseEntity.ok(orientationResponse);
    }

    @GetMapping("/{orientationId}")
    public ResponseEntity<List<OrientationResponse>> findAllResponsesByOrientationId(@PathVariable Long orientationId) {
        List<OrientationResponse> responses = orientationResponseService.findAllByOrientationId(orientationId);
        return ResponseEntity.ok(responses);
    }
}
