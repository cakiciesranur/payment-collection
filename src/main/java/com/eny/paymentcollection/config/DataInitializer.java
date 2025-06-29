package com.eny.paymentcollection.config;

import com.eny.paymentcollection.enums.RoleName;
import com.eny.paymentcollection.model.RoleEntity;
import com.eny.paymentcollection.model.UserEntity;
import com.eny.paymentcollection.repository.RoleRepository;
import com.eny.paymentcollection.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@Profile({"local", "dev"})
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        log.info("Running DataInitializer...");

        // Create role if not exist
        for (RoleName roleName : RoleName.values()) {
            roleRepository.findByName(roleName).orElseGet(() -> {
                log.info("Creating role: {}", roleName);
                return roleRepository.save(new RoleEntity(roleName));
            });
        }

        // create admin user
        userRepository.findByUsername("admin").ifPresentOrElse(
                user -> log.info("Admin user already exists."),
                () -> {
                    UserEntity admin = new UserEntity();
                    admin.setName("Admin");
                    admin.setUsername("admin");
                    admin.setEmail("admin@example.com");
                    admin.setPassword(passwordEncoder.encode("admin123"));
                    admin.setRoles(Set.of(
                            roleRepository.findByName(RoleName.ROLE_ADMIN).orElseThrow()
                    ));

                    userRepository.save(admin);
                    log.info("Admin user created.");
                }
        );
    }
}
