package com.rowland.engineering.byteworks.service;

import com.rowland.engineering.byteworks.controller.UserController;
import com.rowland.engineering.byteworks.dto.UserSummary;
import com.rowland.engineering.byteworks.model.Role;
import com.rowland.engineering.byteworks.model.RoleName;
import com.rowland.engineering.byteworks.model.User;
import com.rowland.engineering.byteworks.repository.UserRepository;
import com.rowland.engineering.byteworks.security.UserPrincipal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserServiceTest {
    @InjectMocks
    private UserService userService;
    @InjectMocks
    private UserController userController;

    @Mock
    private UserRepository userRepository;

    @Test
    @DisplayName("Should return a list of all users")
    public void getAllUsers() {
        User staff1 = User.builder()
                .id(1L)
                .name("Rowland")
                .username("rowland")
                .email("rowland@gmail.com")
                .build();
        User staff2 = User.builder()
                .id(3L)
                .name("Kanu")
                .username("kanu")
                .email("kanu@gmail.com")
                .build();
        List<User> expectedUsers = Arrays.asList(
                staff1,
                staff2
        );

        when(userRepository.findAll()).thenReturn(expectedUsers);

        List<User> actualUsers = userService.getAllUsers();

        assertEquals(expectedUsers.size(), actualUsers.size());
        assertEquals(expectedUsers, actualUsers);

        verify(userRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("If no users in database, we should expect an empty list")
    public void testGetAllUsersEmptyList() {
        List<User> expectedUsers = List.of();

        when(userRepository.findAll()).thenReturn(expectedUsers);

        List<User> actualUsers = userService.getAllUsers();

        assertTrue(actualUsers.isEmpty());

        verify(userRepository, times(1)).findAll();
    }



    @Test
    @DisplayName("Should find user by ID")
    public void findUserById() {
        Long userId = 1L;
        User expectedUser = User.builder()
                .id(userId)
                .name("Rowland")
                .email("Rowland@gmail.com")
                .username("Kanu")
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(expectedUser));

        Optional<User> actualUser = userService.findUserById(userId);

        assertTrue(actualUser.isPresent());
        assertEquals(expectedUser, actualUser.get());

        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    @DisplayName("Should not find user by ID")
    public void findUserByIdNotFound() {
        Long userId = 2L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        Optional<User> actualUser = userService.findUserById(userId);

        assertTrue(actualUser.isEmpty());

        verify(userRepository, times(1)).findById(userId);
    }


    @Test
    @DisplayName("Should return basic user information")
    void getBasicUserInfo() {
        UserPrincipal mockUserPrincipal = new UserPrincipal(1L);

        Role role1 = new Role();
        role1.setName(RoleName.ROLE_STAFF);

        Set<Role> roles = new HashSet<>();
        roles.add(role1);

        User mockUser = User.builder()
                .id(1L)
                .username("Rowland")
                .roles(roles)
                .email("rowland@gmail.com")
                .name("Kanu Rowland")
                .build();

        when(userRepository.findById(mockUserPrincipal.getId())).thenReturn(Optional.of(mockUser));

        Optional<UserSummary> result = userService.getBasicUserInfo(mockUserPrincipal);

        assertTrue(result.isPresent());
        UserSummary userSummary = result.get();
        assertEquals(mockUser.getId(), userSummary.getId());
        assertEquals(mockUser.getUsername(), userSummary.getUsername());
        assertEquals(mockUser.getName(), userSummary.getName());
        assertEquals(mockUser.getEmail(), userSummary.getEmail());
        assertEquals(mockUser.getRoles().toString(), userSummary.getRole().toString());
        assertTrue(userSummary.getRole().contains("ROLE_STAFF"));
    }

    @Test
    @DisplayName("Should update user information")
    public void updateUserInformation() {
        Long userId = 1L;
        String email = "kanurowland92@gmail.com";
        String username = "kanurow";
        String name = "Kanu Rowland";

        User foundUser = User.builder()
                .id(userId)
                .email(email)
                .username(username)
                .name(name)
                .build();

        when(userRepository.getReferenceById(userId)).thenReturn(foundUser);
        when(userRepository.save(any(User.class))).thenReturn(foundUser);

        ResponseEntity<String> response = userService.updateUserInformation(userId.toString(), email, username, name);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(userRepository, times(1)).getReferenceById(userId);
        verify(userRepository, times(1)).save(any(User.class));
    }
}