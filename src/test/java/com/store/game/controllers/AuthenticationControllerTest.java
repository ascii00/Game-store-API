package com.store.game.controllers;

import com.store.game.models.DTO.AuthenticationRequest;
import com.store.game.models.DTO.RegisterRequest;
import com.store.game.models.Role;
import com.store.game.models.User;
import com.store.game.models.enums.ERole;
import com.store.game.repositories.RoleRepository;
import com.store.game.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = "spring.config.name=application-test")
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private final String password = "test_p@ssworD13";

    @BeforeEach
    public void setup() {
        userRepository.deleteAll();
        roleRepository.deleteAll();
        roleRepository.save(new Role(ERole.ROLE_USER));
        userRepository.save(new User("test1@example.com", passwordEncoder.encode(password), roleRepository.findByName(ERole.ROLE_USER).get()));
    }

    @Test
    public void register() throws Exception {
        RegisterRequest registerRequest = RegisterRequest.builder()
                .email("test2@example.com")
                .password(password)
                .build();

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.token").isNotEmpty());
    }

    @Test
    public void registerWithExistingEmail() throws Exception {
        RegisterRequest registerRequest = RegisterRequest.builder()
                .email("test1@example.com")
                .password(password)
                .build();
        MvcResult result = mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isBadRequest())
                .andReturn();
        String responseJson = result.getResponse().getContentAsString();
        assertEquals("{\"status\":\"fail\",\"data\":\"Email already exists\"}", responseJson);
    }

    @Test
    public void registerWithInvalidEmail() throws Exception {
        RegisterRequest registerRequest = RegisterRequest.builder()
                .email("test1example.com")
                .password(password)
                .build();
        MvcResult result = mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isBadRequest())
                .andReturn();
        String responseJson = result.getResponse().getContentAsString();
        assertEquals("{\"status\":\"fail\",\"data\":[\"Email must be a valid email address\"]}", responseJson);
    }

    @Test
    public void registerWithInvalidPassword() throws Exception {
        RegisterRequest registerRequest = RegisterRequest.builder()
                .email("test2example.com")
                .password("test")
                .build();
        MvcResult result = mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isBadRequest())
                .andReturn();
        String responseJson = result.getResponse().getContentAsString();
        assertEquals("{\"status\":\"fail\",\"data\":\"Password is not valid (Password must be at least 8 characters long, contain at least one number, one uppercase and one lowercase letter)\"}", responseJson);
    }

    @Test
    public void authenticate() throws Exception {
        AuthenticationRequest authenticationRequest = AuthenticationRequest.builder()
                .email("test1@example.com")
                .password(password)
                .build();

        mockMvc.perform(post("/api/v1/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authenticationRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.token").isNotEmpty());
    }

    @Test
    public void authenticateWithInvalidEmail() throws Exception {
        AuthenticationRequest authenticationRequest = AuthenticationRequest.builder()
                .email("test2example.com")
                .password(password)
                .build();
        MvcResult result = mockMvc.perform(post("/api/v1/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authenticationRequest)))
                .andExpect(status().isBadRequest())
                .andReturn();
        String responseJson = result.getResponse().getContentAsString();
        assertEquals("{\"status\":\"fail\",\"data\":\"Invalid email or password\"}", responseJson);
    }

    @Test
    public void authenticateNonExistingUser() throws Exception {
        AuthenticationRequest authenticationRequest = AuthenticationRequest.builder()
                .email("test3@example.com")
                .password(password)
                .build();
        MvcResult result = mockMvc.perform(post("/api/v1/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authenticationRequest)))
                .andExpect(status().isBadRequest())
                .andReturn();
        String responseJson = result.getResponse().getContentAsString();
        assertEquals("{\"status\":\"fail\",\"data\":\"Invalid email or password\"}", responseJson);
    }
}

