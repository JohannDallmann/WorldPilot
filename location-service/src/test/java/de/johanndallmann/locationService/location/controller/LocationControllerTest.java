package de.johanndallmann.locationService.location.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        this.locationJpaRepository.deleteAll();

        List<LocationEntity> locationEntityList = List.of(
                this.createTestLocationEntity("Location1", LocationType.RESTAURANT, "City1", "country1"),
                this.createTestLocationEntity("Location2", LocationType.BAR, "City1", "country1"),
                this.createTestLocationEntity("Location3", LocationType.BAR, "City2", "country1"),
                this.createTestLocationEntity("Location4", LocationType.RESTAURANT, "City3", "country2"),
                this.createTestLocationEntity("Location5", LocationType.BAR, "City3", "country2")
        );

        this.locationJpaRepository.saveAll(locationEntityList);
    }

    @Test
    void getLocationPage_noFilter_returnAllLocations_unsorted() throws Exception {
        LocationFilterDto filter = LocationFilterDto.builder()
                .build();

        mockMvc.perform(get("/locations?page=0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(filter)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(5)));
    }

    @Test
    void getLocationPage_filterApplied_returnAllRestaurants() throws Exception {
        LocationFilterDto filter = LocationFilterDto.builder()
                .type("RESTAURANT")
                .build();

        mockMvc.perform(get("/locations?page=0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(filter)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].type").value("RESTAURANT"))
                .andExpect(jsonPath("$[1].type").value("RESTAURANT"));
    }

    @Test
    void getLocationPage_filterApplied_returnAllBars() throws Exception {
        LocationFilterDto filter = LocationFilterDto.builder()
                .type("BAR")
                .build();

        mockMvc.perform(get("/locations?page=0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(filter)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].type").value("BAR"))
                .andExpect(jsonPath("$[1].type").value("BAR"))
                .andExpect(jsonPath("$[2].type").value("BAR"));
    }

    @Test
    void getLocationPage_filterApplied_returnAllRestaurantsDespiteLowCase() throws Exception {
        LocationFilterDto filter = LocationFilterDto.builder()
                .type("restaurant")
                .build();

        mockMvc.perform(get("/locations?page=0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(filter)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].type").value("RESTAURANT"))
                .andExpect(jsonPath("$[1].type").value("RESTAURANT"));
    }

    @Test
    void getLocationPage_filterApplied_throwErrorForNotExistingType() throws Exception {
        LocationFilterDto filter = LocationFilterDto.builder()
                .type("notExistingType")
                .build();

        mockMvc.perform(get("/locations?page=0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(filter)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(
                        "Invalid value 'notExistingType' for field 'type'. Allowed values: [BAR, RESTAURANT, NATURE, OTHER]"));
    }

    @Test
    void getLocationPage_filterApplied_returnNoLocation() throws Exception {
        LocationFilterDto filter = LocationFilterDto.builder()
                .type("NATURE")
                .build();

        mockMvc.perform(get("/locations?page=0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(filter)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void getLocationPage_twoFilterApplied_returnAllBarsInCountry1() throws Exception {
        LocationFilterDto filter = LocationFilterDto.builder()
                .type("BAR")
                .country("country1")
                .build();

        mockMvc.perform(get("/locations?page=0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(filter)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].type").value("BAR"))
                .andExpect(jsonPath("$[0].country").value("country1"))
                .andExpect(jsonPath("$[1].type").value("BAR"))
                .andExpect(jsonPath("$[1].country").value("country1"));
    }

    @Test
    void getLocationPage_noFilter_returnFirstPageContentWithSize2_unsorted() throws Exception {
        LocationFilterDto filter = LocationFilterDto.builder()
                .build();

        mockMvc.perform(get("/locations?page=0&size=3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(filter)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    void getLocationPage_noFilter_returnSecondPageContentWithSize2_unsorted() throws Exception {
        LocationFilterDto filter = LocationFilterDto.builder()
                .build();

        mockMvc.perform(get("/locations?page=1&size=3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(filter)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void getLocationPage_noFilter_returnAllLocations_sortedByNameAsc() throws Exception {
        LocationFilterDto filter = LocationFilterDto.builder()
                .build();

        mockMvc.perform(get("/locations?page=0&size=5&sort=name,asc")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(filter)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(5)))
                .andExpect(jsonPath("$[0].name").value("Location1"))
                .andExpect(jsonPath("$[1].name").value("Location2"))
                .andExpect(jsonPath("$[2].name").value("Location3"))
                .andExpect(jsonPath("$[3].name").value("Location4"))
                .andExpect(jsonPath("$[4].name").value("Location5"));
    }

    @Test
    void getLocationPage_noFilter_returnAllLocations_sortedByNameDsc() throws Exception {
        LocationFilterDto filter = LocationFilterDto.builder()
                .build();

        mockMvc.perform(get("/locations?page=0&size=5&sort=name,desc")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(filter)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(5)))
                .andExpect(jsonPath("$[0].name").value("Location5"))
                .andExpect(jsonPath("$[1].name").value("Location4"))
                .andExpect(jsonPath("$[2].name").value("Location3"))
                .andExpect(jsonPath("$[3].name").value("Location2"))
                .andExpect(jsonPath("$[4].name").value("Location1"));
    }

    @Test
    void postNewLocation_returnObjectLocation_newLocationPersisted() throws Exception {
        NewLocationDto newLocation = NewLocationDto.builder()
                .name("NewLocation")
                .type(LocationType.RESTAURANT)
                .country("Country1")
                .city("City1")
                .build();

        MvcResult result = mockMvc.perform(post("/locations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(newLocation)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andReturn();

        Optional<LocationEntity> savedLocation = this.locationJpaRepository.findByName(newLocation.name());
        assertTrue(savedLocation.isPresent());
        assertEquals(newLocation.city(),savedLocation.get().getCity());
        assertEquals(newLocation.type(), savedLocation.get().getType());

        String locationHeader = result.getResponse().getHeader("Location");
        assertTrue(locationHeader.endsWith("/locations/" + savedLocation.get().getId()));
    }

    private LocationEntity createTestLocationEntity(String name, LocationType type, String city, String country){
        LocationEntity entity = LocationEntity.builder()
                .name(name)
                .type(type)
                .city(city)
                .country(country)
                .build();
        return entity;
    }
}