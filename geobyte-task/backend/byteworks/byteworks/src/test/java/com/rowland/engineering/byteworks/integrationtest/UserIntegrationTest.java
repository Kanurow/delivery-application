package com.rowland.engineering.byteworks.integrationtest;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.rowland.engineering.byteworks.controller.UserController;
import com.rowland.engineering.byteworks.model.User;
import com.rowland.engineering.byteworks.repository.DeliveryRepository;
import com.rowland.engineering.byteworks.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
public class UserIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private DeliveryRepository deliveryRepository;

    @MockBean
    private UserService userService;

    @InjectMocks
    private UserController userController;


    @Test
    @WithMockUser(username = "rowland", password = "rowland", roles = "STAFF")
    public void getUserById_ShouldReturnOk() throws Exception {

        Long userId = 1L;
        User mockUser = new User();
        mockUser.setId(userId);
        mockUser.setName("Rowland");
        mockUser.setEmail("Rowland@gmail.com");
        mockUser.setUsername("kanu");

        when(userService.findUserById(userId)).thenReturn(Optional.of(mockUser));

        mockMvc.perform(get("/api/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Rowland"));
    }
    @Test
    @WithMockUser(username = "rowland", password = "rowland", roles = "STAFF")
    public void getUsers_ShouldReturnListOfUsers() throws Exception {
        User user1 = User.builder()
                .id(1L)
                .name("Samuel")
                .username("sammy")
                .email("samuel@mail.com")
                .password("11111")
                .build();
        User user2 = User.builder()
                .id(2L)
                .name("Rowland")
                .username("rowly")
                .email("rowland@rmail.com")
                .password("22222")
                .build();
        List<User> userList = Arrays.asList(user1, user2);

        when(userService.getAllUsers()).thenReturn(userList);

        // When
        ResultActions result = mockMvc.perform(get("/api/users/all")
                .contentType(MediaType.APPLICATION_JSON));

        // Then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Samuel"))
                .andExpect(jsonPath("$[0].password").value("11111"))
                .andExpect(jsonPath("$[0].email").value("samuel@mail.com"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].password").value("22222"))
                .andExpect(jsonPath("$[1].name").value("Rowland"))
                .andExpect(jsonPath("$[1].email").value("rowland@rmail.com"));
    }
}
