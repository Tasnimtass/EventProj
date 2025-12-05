package com.miniprojet.miniprojet.config;

import com.miniprojet.miniprojet.entity.ERole;
import com.miniprojet.miniprojet.entity.Role;
import com.miniprojet.miniprojet.entity.Utilisateur;
import com.miniprojet.miniprojet.repository.UtilisateurRepository;
import com.miniprojet.miniprojet.repository.roleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UtilisateurRepository userRep;
    @Autowired
    private roleRepository roleRep;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        // Vérifier si le rôle ADMIN existe, sinon le créer
        Role admin = roleRep.findByName(ERole.ADMIN)
                .orElseGet(() -> {
                    Role role = new Role();
                    role.setName(ERole.ADMIN);
                    return roleRep.save(role);
                });

        String email = "tasnimmabrouk2004@gmail.com";
        Utilisateur existingUser = userRep.findByEmail(email);

        if (existingUser != null) {
            boolean hasAdmin = existingUser.getRoles().stream()
                    .anyMatch(r -> r.getName().equals(ERole.ADMIN));

            if (!hasAdmin) {
                existingUser.getRoles().add(admin);
                userRep.saveAndFlush(existingUser);
                System.out.println("Rôle ADMIN attribué à l'utilisateur existant !");
            } else {
                System.out.println("L'utilisateur a déjà le rôle ADMIN.");
            }
        } else {
            Utilisateur newUser = new Utilisateur();
            newUser.setNom("Tasnim");
            newUser.setEmail(email);

            newUser.setMdp(passwordEncoder.encode("tass123"));
            newUser.getRoles().add(admin);

            userRep.saveAndFlush(newUser);
            System.out.println("Nouvel utilisateur ADMIN créé !");
        }
    }
}
