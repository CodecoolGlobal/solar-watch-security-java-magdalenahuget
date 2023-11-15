package com.company.solarwatch.controller;

import com.company.solarwatch.model.RoleType;
import com.company.solarwatch.model.UserEntity;
import com.company.solarwatch.model.payload.JwtResponse;
import com.company.solarwatch.model.solarWatchData.City;
import com.company.solarwatch.repository.UserEntityRepository;
import com.company.solarwatch.service.UserService;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class UserEntityControllerTest {

    @Mock
    private UserService userService;

    @Autowired
    private UserEntityController userEntityController;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserEntityRepository userEntityRepository;

    @Test
    void register() throws Exception {
        String registerRequest = "{\"username\": \"John\",\"password\": \"password\"}";
        String jwtToken = authenticateAndGetUserJwt();

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/user/register_user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerRequest)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isCreated())
                .andReturn();
        System.out.println(result.getResponse());

        // CLEAN UP ADDED DATA TO DB
        String username = "John";
        UserEntity userEntity = userEntityRepository.findUserEntityByUsername(username);
        userEntityRepository.delete(userEntity);
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

    @Test
    void authenticate() throws Exception {
        String authenticationRequest = "{\"username\": \"ela\",\"password\": \"ela\"}";
        String jwtToken = authenticateAndGetUserJwt();

//        JwtResponse response = JwtResponse.builder()
//                .jwt("jwtToken")
//                .userName("johndoe@example.com")
//                .roles(List.of(RoleType.ROLE_USER.name()))
//                .build();
//
//        when(userService.createJwtResponse("jwtToken", "johndoe@example.com", List.of(RoleType.ROLE_USER.name()))).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post("/user/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authenticationRequest)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName").value("ela"))
                .andExpect(jsonPath("$.roles[0]").value(RoleType.ROLE_USER.name()));
    }
}