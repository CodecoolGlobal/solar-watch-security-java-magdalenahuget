package com.company.solarwatch;

import com.company.solarwatch.model.dto.CityRequestDto;
import com.company.solarwatch.model.dto.CityResponseDto;
import com.company.solarwatch.model.solarWatchData.City;
import com.company.solarwatch.repository.CityRepository;
import com.company.solarwatch.service.CityMapper;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.client.RestTemplate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class CityControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private CityMapper cityMapper;

    @Test
    public void testGetCityById() throws Exception {
        // GIVEN
        CityResponseDto cityResponseDto = createTestCityDto();
//        String jwtToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbGEiLCJpYXQiOjE3MDAwNjk1ODEsImV4cCI6MTcwMDE1NTk4MX0.rGFngg-XHMAIGWTjkL4VYJEhik9neUIOX8eTE6foGJA";
//        String jwtToken = authenticateAndGetToken("ela", "ela");
        String jwtToken = authenticateAndGetUserJwt();

        // WHEN
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/city/{cityId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andReturn();

        // THEN
        String content = result.getResponse().getContentAsString();
        System.out.println(content);

        JSONObject jsonObject = new JSONObject(content);

        assertNotNull(jsonObject);
        assertEquals(cityResponseDto.getName(), jsonObject.get("name"));
        assertEquals(cityResponseDto.getLongitude(), jsonObject.get("longitude"));
        assertEquals(cityResponseDto.getLatitude(), jsonObject.get("latitude"));
        assertEquals(cityResponseDto.getState(), jsonObject.get("state"));
        assertEquals(cityResponseDto.getCountry(), jsonObject.get("country"));
    }

    @Test
    public void testAddCity() throws Exception {
        // GIVEN
        String jwtToken = authenticateAndGetAdminJwt();
        String requestBody = "{\"name\": \"testname\",\"longitude\": \"123.1\",\"latitude\": \"123.1\",\"state\": \"teststate\",\"country\": \"testcountry\"}";

        // WHEN
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/city")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isCreated())
                .andReturn();

        // THEN
        assertEquals(result.getResponse().getStatus(), 201);

        // CLEAN UP ADDED DATA TO DB
        String content = result.getResponse().getContentAsString();
        JSONObject jsonObject = new JSONObject(content);
        Long cityId = Long.valueOf(jsonObject.get("id").toString());
        City cityById = cityRepository.findById(cityId)
                .orElseThrow(() -> new RuntimeException("City not found with id: {}" + cityId));
        cityRepository.delete(cityById);
    }

    @Test
    public void testUpdateCity() throws Exception {
        // GIVEN
        String jwtToken = authenticateAndGetAdminJwt();
        String requestBodyToAdd = "{\"name\": \"testname\",\"longitude\": \"123.1\",\"latitude\": \"123.1\",\"state\": \"teststate\",\"country\": \"testcountry\"}";
        String requestBodyToUpdate = "{\"name\": \"testnameupdated\",\"longitude\": \"123.1\",\"latitude\": \"123.1\",\"state\": \"teststateupdated\",\"country\": \"testcountryupdated\"}";

        // WHEN
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/city")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBodyToAdd)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isCreated())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        JSONObject jsonObject = new JSONObject(content);
        Long cityId = Long.valueOf(jsonObject.get("id").toString());

        // WHEN
        MvcResult resultPatch = mockMvc.perform(MockMvcRequestBuilders.patch("/city/{cityId}", cityId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBodyToUpdate)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andReturn();
        String contentPatch = resultPatch.getResponse().getContentAsString();
        JSONObject jsonObjectPatch = new JSONObject(contentPatch);
        // THEN
        assertEquals(resultPatch.getResponse().getStatus(), 200);
        assertNotNull(jsonObject);
        assertNotEquals(jsonObject.get("name"), jsonObjectPatch.get("name"));
        assertEquals(jsonObject.get("longitude"), jsonObjectPatch.get("longitude"));
        assertEquals(jsonObject.get("latitude"), jsonObjectPatch.get("latitude"));
        assertNotEquals(jsonObject.get("state"), jsonObjectPatch.get("state"));
        assertNotEquals(jsonObject.get("country"), jsonObjectPatch.get("country"));

        // CLEAN UP ADDED DATA TO DB
        City cityById = cityRepository.findById(cityId)
                .orElseThrow(() -> new RuntimeException("City not found with id: {}" + cityId));
        cityRepository.delete(cityById);
    }

    private CityResponseDto createTestCityDto() {
        CityRequestDto cityRequestDto = CityRequestDto.builder()
                .name("Test City")
                .longitude(30.0)
                .latitude(20.0)
                .state("Test State")
                .country("Test Country")
                .build();

        City testCity = cityMapper.mapCityRequestDtoToCity(cityRequestDto);
        CityResponseDto cityResponseDto = cityMapper.mapCityToCityResponseDto(testCity);
        return cityResponseDto;
    }

    public String authenticateAndGetUserJwt() throws JSONException {
        String URL = "http://localhost:8080/user/signin";
        String username = "ela";
        String password = "ela";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String requestBody = "{\"username\": \"" + username + "\", \"password\": \"" + password + "\"}";
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(URL, HttpMethod.POST, requestEntity, String.class);
        HttpHeaders responseHeaders = responseEntity.getHeaders();
        String body = responseEntity.getBody();
        JSONObject jsonObject = new JSONObject(body);
        String jwt = jsonObject.get("jwt").toString();

        List<String> authorizationHeader = responseHeaders.get(HttpHeaders.AUTHORIZATION);
        if (jwt != null) {
            return jwt;
        }

        if (authorizationHeader != null && !authorizationHeader.isEmpty()) {
            return authorizationHeader.get(0);
        } else {
            return null;
        }
    }

    public String authenticateAndGetAdminJwt() throws JSONException {
        String URL = "http://localhost:8080/user/signin";
        String username = "ala";
        String password = "ala";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String requestBody = "{\"username\": \"" + username + "\", \"password\": \"" + password + "\"}";
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(URL, HttpMethod.POST, requestEntity, String.class);
        HttpHeaders responseHeaders = responseEntity.getHeaders();
        String body = responseEntity.getBody();
        JSONObject jsonObject = new JSONObject(body);
        String jwt = jsonObject.get("jwt").toString();

        List<String> authorizationHeader = responseHeaders.get(HttpHeaders.AUTHORIZATION);
        if (jwt != null) {
            return jwt;
        }

        if (authorizationHeader != null && !authorizationHeader.isEmpty()) {
            return authorizationHeader.get(0);
        } else {
            return null;
        }
    }
}
