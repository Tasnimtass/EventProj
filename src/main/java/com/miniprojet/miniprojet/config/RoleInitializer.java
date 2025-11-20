package com.miniprojet.miniprojet.config;

import com.miniprojet.miniprojet.entity.ERole;
import com.miniprojet.miniprojet.entity.Role;
import com.miniprojet.miniprojet.repository.roleRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RoleInitializer {
    @Autowired
    private roleRepository roleRepository;

    @PostConstruct//pour dire que cette methode doit appeller automatiquement au moment de lacement de la aplication
    public void initRoles() {
        for (ERole roleName : ERole.values()) {
            if (roleRepository.findByName(roleName).isEmpty()) {
                Role role = new Role();
                role.setName(roleName);
                roleRepository.save(role);
                System.out.println("Role ajouté : " + roleName);
            }
        }
    }
}
