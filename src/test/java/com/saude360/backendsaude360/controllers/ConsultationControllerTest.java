package com.saude360.backendsaude360.controllers;

import com.saude360.backendsaude360.entities.Consultation;
import com.saude360.backendsaude360.services.ConsultationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ConsultationControllerTest {


    @InjectMocks
    private ConsultationController consultationController;

    @Mock
    private ConsultationService consultationService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(consultationController).build();
    }

    /**
     * Realiza uma requisição GET para o endpoint /consultation/ e verifica se o status é OK
     * @throws Exception
     */
    @Test
    void testFindAll() throws Exception {
        when(consultationService.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/consultation/"))
                .andExpect(status().isOk());
    }

    /**
     * Realiza uma requisição GET para o endpoint /consultation/1 e verifica se o status é OK
     * @throws Exception
     */
    @Test
    void testFindById() throws Exception {
        when(consultationService.findById(anyLong())).thenReturn(new Consultation());

        mockMvc.perform(get("/consultation/1"))
                .andExpect(status().isOk());
    }

    /**
     * Realiza uma requisição DELETE para o endpoint /consultation/1 e verifica se o status é No Content
     * @throws Exception
     */
    @Test
    void testDelete() throws Exception {
        mockMvc.perform(delete("/consultation/1"))
                .andExpect(status().isNoContent());
    }

}