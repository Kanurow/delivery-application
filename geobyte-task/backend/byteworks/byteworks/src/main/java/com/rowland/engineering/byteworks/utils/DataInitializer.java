package com.rowland.engineering.byteworks.utils;

import com.rowland.engineering.byteworks.model.Role;
import com.rowland.engineering.byteworks.model.User;
import com.rowland.engineering.byteworks.repository.RoleRepository;
import com.rowland.engineering.byteworks.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

import static com.rowland.engineering.byteworks.model.RoleName.ROLE_ADMIN;
import static com.rowland.engineering.byteworks.model.RoleName.ROLE_STAFF;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        List<Role> roles = Arrays.asList(
                new Role(ROLE_STAFF),
                new Role(ROLE_ADMIN)
        );
        roleRepository.saveAll(roles);
    }
}
