package com.miniprojet.miniprojet.controller;

import com.miniprojet.miniprojet.DTO.UtilisateurUpdateDTO;
import com.miniprojet.miniprojet.config.JwtUtils;
import com.miniprojet.miniprojet.entity.ERole;
import com.miniprojet.miniprojet.entity.Utilisateur;
import com.miniprojet.miniprojet.service.UtilisateurService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.miniprojet.miniprojet.DTO.UtilisateurDTO;


import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UtilisateurController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UtilisateurService userService;

    @GetMapping("/get-users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UtilisateurDTO>> getUsers(){
        try {
            List<Utilisateur> userlist = userService.getUsers();
            if (userlist.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            List<UtilisateurDTO> userDTOList = userlist.stream()
                    .map(user -> new UtilisateurDTO(user))
                    .toList();

            return new ResponseEntity<>(userDTOList, HttpStatus.OK);
        } catch(Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("#id == authentication.principal.id or hasRole('ADMIN')")//#id c'est le id passe en parametre
    @GetMapping("/get-user-by-id/{id}")
    public ResponseEntity<UtilisateurDTO> getUserById(@PathVariable Integer id) {
        Optional<Utilisateur> user = userService.getUserById(id);
        if (user.isPresent()) {
            UtilisateurDTO userDTO = new UtilisateurDTO(user.get());
            return new ResponseEntity<>(userDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/connexion")
    public ResponseEntity<?> getUserByEmail(@RequestBody Utilisateur utilisateur) {
        Utilisateur user = userService.getUser(utilisateur.getEmail());
        if (user == null) {
            return ResponseEntity.status(401).body("Utilisateur non trouvé");
        }

        System.out.println("Utilisateur trouvé : " + user.getEmail());
        System.out.println("Mot de passe envoyé : " + utilisateur.getMdp());
        System.out.println("Mot de passe en base : " + user.getMdp());

        if (!passwordEncoder.matches(utilisateur.getMdp(), user.getMdp())) {
            return ResponseEntity.status(401).body("Mot de passe incorrect");
        }

        String token = jwtUtils.generateToken(user);
        return ResponseEntity.ok(new AuthResponse(token));
    }

    static class AuthResponse {
        private String token;

        public AuthResponse(String token) {
            this.token = token;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }

    @PostMapping("/create-user")
    public  ResponseEntity<?> createUser(@Valid @RequestBody Utilisateur user, BindingResult bindingResult){//bindingresult contient les erreur que valid detecte
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        userService.createUser(user);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("Utilisateur créé avec succès !");
    }

    @PutMapping("/update-user/{id}")
    @PreAuthorize("#id == authentication.principal.id or hasRole('ADMIN')")
    public ResponseEntity<UtilisateurDTO> updateUser(@PathVariable Integer id, @RequestBody UtilisateurUpdateDTO dto) {
        Utilisateur user = new Utilisateur();

        user.setEmail(dto.getEmail());
        user.setNom(dto.getNom());
        user.setTelephone(dto.getTelephone());

        Utilisateur updatedUser = userService.updateUser(id, user);
        if (updatedUser != null) {
            return ResponseEntity.ok(new UtilisateurDTO(updatedUser));
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete-user/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer id){
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace(); // ← utile en console
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur serveur : " + e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/set-admin/{id}")
    public ResponseEntity<?> makeAdmin(@PathVariable Integer id) {
        userService.assignAdminRole(id);
        return ResponseEntity.ok("Rôle ADMIN attribué au compte");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/assign-role/{userId}/{roleName}")
    public ResponseEntity<?> assignRole(@PathVariable Integer userId, @PathVariable ERole roleName) {
        userService.assignRole(userId, roleName);
        return ResponseEntity.ok("Rôle " + roleName + " attribué avec succès !");
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/remove-role/{userId}/{roleName}")
    public ResponseEntity<?> removeRole(@PathVariable Integer userId, @PathVariable ERole roleName) {
        userService.removeRole(userId, roleName);
        return ResponseEntity.ok("Rôle " + roleName + " supprimé avec succès !");
    }

}
