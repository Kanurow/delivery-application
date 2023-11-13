package com.rowland.engineering.byteworks.controller;

import com.rowland.engineering.byteworks.dto.*;
import com.rowland.engineering.byteworks.model.User;
import com.rowland.engineering.byteworks.security.CurrentUser;
import com.rowland.engineering.byteworks.security.UserPrincipal;
import com.rowland.engineering.byteworks.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public Optional<User> getUserById(@PathVariable(value = "id") Long id) {
        return userService.findUserById(id);
    }

    @GetMapping("/all")
    public List<User> getUsers(){
        return userService.getAllUsers();
    }


    @GetMapping("/user/me")
    public Optional<UserSummary> getCurrentUser(@CurrentUser UserPrincipal currentUser) {
        return userService.getBasicUserInfo(currentUser);
    }

}
