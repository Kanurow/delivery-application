package com.rowland.engineering.byteworks.service;


import com.rowland.engineering.byteworks.dto.UserSummary;
import com.rowland.engineering.byteworks.model.User;
import com.rowland.engineering.byteworks.repository.UserRepository;
import com.rowland.engineering.byteworks.security.UserPrincipal;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }


    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }


    public Optional<UserSummary> getBasicUserInfo(UserPrincipal currentUser) {
        Optional<User> activeUser = userRepository.findById(currentUser.getId());
        return activeUser.stream().map(user -> {
            UserSummary userDetail = new UserSummary();

            userDetail.setId(user.getId());
            userDetail.setUsername(user.getUsername());
            userDetail.setRole(user.getRoles().toString());
            userDetail.setEmail(user.getEmail());
            userDetail.setName(user.getName());
            return userDetail;
        }).findFirst();
    }



    @Transactional
    public ResponseEntity<String> updateUserInformation(String userId,
                                                        String email,
                                                        String username,
                                                        String name) {
        User foundUser = userRepository.getReferenceById(Long.valueOf(userId));

        User userUpdatedInfo = User.builder()
                .id(Long.valueOf(userId))
                .name(name)
                .email(email)
                .username(username)
                .password(foundUser.getPassword())
                .build();


        User savedUserUpdatedInfo = userRepository.save(userUpdatedInfo);

        return ResponseEntity.status(HttpStatus.OK).body(savedUserUpdatedInfo.getName() + " has updated profile information");
    }
}
