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
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.*;

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

    private UUID UserId1;
    private UUID UserId2;

    @BeforeEach
    void setup() {
        this.locationJpaRepository.deleteAll();

        this.UserId1 = UUID.fromString("a6b424e1-b86d-418d-a0f7-f0666920d047");
        this.UserId2 = UUID.fromString("a6b424e1-b86d-418d-a0f7-f0666920d048");
        Instant createdAt = LocalDateTime.of(2025, Month.JANUARY, 1, 12, 0).atZone(ZoneId.of("UTC")).toInstant();

        List<LocationEntity> locationEntityList = List.of(
                this.createTestLocationEntity("Location1", LocationType.RESTAURANT, "City1", "country1", UserId1, UserId1, createdAt),
                this.createTestLocationEntity("Location2", LocationType.BAR, "City1", "country1", UserId1, UserId1, createdAt),
                this.createTestLocationEntity("Location3", LocationType.BAR, "City2", "country1", UserId1, UserId1, createdAt),
                this.createTestLocationEntity("Location4", LocationType.RESTAURANT, "City3", "country2", UserId1, UserId1, createdAt),
                this.createTestLocationEntity("Location5", LocationType.BAR, "City3", "country2", UserId1, UserId1, createdAt),
                this.createTestLocationEntity("Location6", LocationType.RESTAURANT, "City1", "country1", UserId2, UserId2, createdAt),
                this.createTestLocationEntity("Location7", LocationType.RESTAURANT, "City1", "country1", UserId1, UserId2, createdAt)
        );

        this.locationJpaRepository.saveAll(locationEntityList);
    }

    @Test
    void getLocationPage_noFilter_returnAllOwnedLocations_unsorted() throws Exception {
        LocationFilterDto filter = LocationFilterDto.builder()
                .build();

        TestingAuthenticationToken auth = new TestingAuthenticationToken(this.UserId1.toString(), null, "ROLE_user");
        SecurityContextHolder.getContext().setAuthentication(auth);

        mockMvc.perform(get("/locations?page=0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(filter)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(5)));
    }

    @Test
    void getLocationPage_noFilter_returnAllOwnedLocations_despiteCreatedByOtherUser_unsorted() throws Exception {
        LocationFilterDto filter = LocationFilterDto.builder()
                .build();

        TestingAuthenticationToken auth = new TestingAuthenticationToken(this.UserId2.toString(), null, "ROLE_user");
        SecurityContextHolder.getContext().setAuthentication(auth);

        mockMvc.perform(get("/locations?page=0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(filter)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("Location6"))
                .andExpect(jsonPath("$[1].name").value("Location7"));
    }

    @Test
    void getLocationPage_filterApplied_returnAllOwnedRestaurants() throws Exception {
        LocationFilterDto filter = LocationFilterDto.builder()
                .type("RESTAURANT")
                .build();

        TestingAuthenticationToken auth = new TestingAuthenticationToken(this.UserId1.toString(), null, "ROLE_user");
        SecurityContextHolder.getContext().setAuthentication(auth);

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
    void getLocationPage_filterApplied_returnAllOwnedBars() throws Exception {
        LocationFilterDto filter = LocationFilterDto.builder()
                .type("BAR")
                .build();

        TestingAuthenticationToken auth = new TestingAuthenticationToken(this.UserId1.toString(), null, "ROLE_user");
        SecurityContextHolder.getContext().setAuthentication(auth);

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
    void getLocationPage_filterApplied_returnAllOwnedRestaurantsDespiteLowCase() throws Exception {
        LocationFilterDto filter = LocationFilterDto.builder()
                .type("restaurant")
                .build();

        TestingAuthenticationToken auth = new TestingAuthenticationToken(this.UserId1.toString(), null, "ROLE_user");
        SecurityContextHolder.getContext().setAuthentication(auth);

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

        TestingAuthenticationToken auth = new TestingAuthenticationToken(this.UserId1.toString(), null, "ROLE_user");
        SecurityContextHolder.getContext().setAuthentication(auth);

        mockMvc.perform(get("/locations?page=0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(filter)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(
                        "Invalid value 'notExistingType' for field 'type'. Allowed values: [BAR, RESTAURANT, NATURE, OTHER]"));
    }

    @Test
    void getLocationPage_filterApplied_returnNoOwnedLocationsMatchingFilter() throws Exception {
        LocationFilterDto filter = LocationFilterDto.builder()
                .type("NATURE")
                .build();

        TestingAuthenticationToken auth = new TestingAuthenticationToken(this.UserId1.toString(), null, "ROLE_user");
        SecurityContextHolder.getContext().setAuthentication(auth);

        mockMvc.perform(get("/locations?page=0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(filter)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void getLocationPage_twoFilterApplied_returnAllOwnedBarsInCountry1() throws Exception {
        LocationFilterDto filter = LocationFilterDto.builder()
                .type("BAR")
                .country("country1")
                .build();

        TestingAuthenticationToken auth = new TestingAuthenticationToken(this.UserId1.toString(), null, "ROLE_user");
        SecurityContextHolder.getContext().setAuthentication(auth);

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

        TestingAuthenticationToken auth = new TestingAuthenticationToken(this.UserId1.toString(), null, "ROLE_user");
        SecurityContextHolder.getContext().setAuthentication(auth);

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

        TestingAuthenticationToken auth = new TestingAuthenticationToken(this.UserId1.toString(), null, "ROLE_user");
        SecurityContextHolder.getContext().setAuthentication(auth);

        mockMvc.perform(get("/locations?page=1&size=3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(filter)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void getLocationPage_noFilter_returnAllOwnedLocations_sortedByNameAsc() throws Exception {
        LocationFilterDto filter = LocationFilterDto.builder()
                .build();

        TestingAuthenticationToken auth = new TestingAuthenticationToken(this.UserId1.toString(), null, "ROLE_user");
        SecurityContextHolder.getContext().setAuthentication(auth);

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
    void getLocationPage_noFilter_returnAllOwnedLocations_sortedByNameDsc() throws Exception {
        LocationFilterDto filter = LocationFilterDto.builder()
                .build();

        TestingAuthenticationToken auth = new TestingAuthenticationToken(this.UserId1.toString(), null, "ROLE_user");
        SecurityContextHolder.getContext().setAuthentication(auth);

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
    void postNewLocation_validObject_returnObjectLocation_newLocationPersisted_creatorIdAndOwnerIdAndCreationDateAndUpdatedAtSet() throws Exception {
        NewLocationDto newLocation = NewLocationDto.builder()
                .name("NewLocation")
                .type(LocationType.RESTAURANT)
                .country("Country1")
                .city("City1")
                .build();

        TestingAuthenticationToken auth = new TestingAuthenticationToken(this.UserId1.toString(), null, "ROLE_user");
        SecurityContextHolder.getContext().setAuthentication(auth);

        MvcResult result = mockMvc.perform(post("/locations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(newLocation)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andReturn();

        List<LocationEntity> savedLocations = this.locationJpaRepository.findByName(newLocation.name());
        assertEquals(1, savedLocations.size());
        assertEquals(newLocation.city(),savedLocations.get(0).getCity());
        assertEquals(newLocation.type(), savedLocations.get(0).getType());
        assertEquals(this.UserId1, savedLocations.get(0).getCreatorId());
        assertEquals(this.UserId1, savedLocations.get(0).getOwnerId());
        assertNotNull(savedLocations.get(0).getCreatedAt());
        assertNotNull(savedLocations.get(0).getUpdatedAt());

        String locationHeader = result.getResponse().getHeader("Location");
        assertTrue(locationHeader.endsWith("/locations/" + savedLocations.get(0).getId()));
    }

    @Test
    void postNewLocation_invalidObject_return400_newLocationNotPersisted() throws Exception {
        NewLocationDto newLocation = NewLocationDto.builder()
                .name("NewLocation")
                .build();

        TestingAuthenticationToken auth = new TestingAuthenticationToken(this.UserId1.toString(), null, "ROLE_user");
        SecurityContextHolder.getContext().setAuthentication(auth);

        mockMvc.perform(post("/locations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(newLocation)))
                .andExpect(status().isBadRequest());

        List<LocationEntity> savedLocations = this.locationJpaRepository.findByName(newLocation.name());
        assertTrue(savedLocations.isEmpty());
    }

    @Test
    void transferLocationsToOtherUser_withLocationIdFilter_return201andListWithOneLocation_locationCopiedWithNewOwnerId() throws Exception {
        // get ID from repo by name to avoid DirtiesContext (otherwise IDs are unpredictable)
        Long locationId = this.locationJpaRepository.findByName("Location1").get(0).getId();

        TransferLocationDto transferLocationDto = TransferLocationDto.builder()
                .newOwnerId(this.UserId2.toString())
                .locationId(locationId)
                .build();

        TestingAuthenticationToken auth = new TestingAuthenticationToken(this.UserId1.toString(), null, "ROLE_user");
        SecurityContextHolder.getContext().setAuthentication(auth);

        mockMvc.perform(post("/locations/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(transferLocationDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Location1"));

        List<LocationEntity> savedLocations = this.locationJpaRepository.findByName("Location1");

        assertEquals(2, savedLocations.size());
        assertEquals(this.UserId1, savedLocations.get(0).getCreatorId());
        assertEquals(this.UserId1, savedLocations.get(0).getOwnerId());
        assertEquals(this.UserId1, savedLocations.get(1).getCreatorId());
        assertEquals(this.UserId2, savedLocations.get(1).getOwnerId());
    }

    private LocationEntity createTestLocationEntity(String name, LocationType type, String city, String country, UUID creatorId, UUID ownerId, Instant createdAt){
        return LocationEntity.builder()
                .name(name)
                .type(type)
                .city(city)
                .country(country)
                .creatorId(creatorId)
                .ownerId(ownerId)
                .createdAt(createdAt)
                .updatedAt(createdAt)
                .build();
    }
}