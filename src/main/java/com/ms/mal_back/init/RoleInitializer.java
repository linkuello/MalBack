package com.ms.mal_back.init;

import com.ms.mal_back.entity.Role;
import com.ms.mal_back.repository.RoleRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoleInitializer {

    private final RoleRepository roleRepository;

    @PostConstruct
    public void initRoles() {
        createIfNotExists("ROLE_USER");
        createIfNotExists("ROLE_ADMIN");
        createIfNotExists("ROLE_OPERATOR");
    }

    private void createIfNotExists(String roleName) {
        if (roleRepository.findByName(roleName).isEmpty()) {
            roleRepository.save(new Role(roleName));
        }
    }
}
