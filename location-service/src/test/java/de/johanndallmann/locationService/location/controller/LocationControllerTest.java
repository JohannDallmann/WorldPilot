package de.johanndallmann.locationService.location.controller;

import de.johanndallmann.locationService.common.enums.LocationType;
import de.johanndallmann.locationService.location.repository.LocationEntity;
import de.johanndallmann.locationService.location.repository.LocationJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class LocationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LocationJpaRepository locationJpaRepository;

    @BeforeEach
    void setup() {
        LocationEntity entity = LocationEntity.builder()
                .name("Testlocation")
                .type(LocationType.RESTAURANT)
                .city("Berlin")
                .country("Germany")
                .build();
        locationJpaRepository.save(entity);
    }

    @Test
    void getAllLocations() throws Exception {
        mockMvc.perform(get("/locations/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Testlocation"));
    }
}