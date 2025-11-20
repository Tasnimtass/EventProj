package com.miniprojet.miniprojet.service;

import com.miniprojet.miniprojet.entity.ERole;
import com.miniprojet.miniprojet.entity.Role;
import com.miniprojet.miniprojet.entity.Utilisateur;
import com.miniprojet.miniprojet.repository.UtilisateurRepository;
import com.miniprojet.miniprojet.repository.roleRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UtilisateurService {

    @Autowired
    private UtilisateurRepository userRep;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private roleRepository roleRep;

    public List<Utilisateur> getUsers(){
    return userRep.findAll();
    }

    public Utilisateur getUser(String email){
        return userRep.findByEmail(email);
    }

    public Utilisateur createUser(Utilisateur user) {
        try {
            if (userRep.findByEmail(user.getEmail()) != null) {
                throw new RuntimeException("Cet email est déjà utilisé.");
            }
            String hashedPassword = passwordEncoder.encode(user.getMdp());
            Role userRole = roleRep.findByName(ERole.USER)
                    .orElseThrow(() -> new RuntimeException("Role USER not found"));

            user.setRoles(Collections.singleton(userRole));
            user.setMdp(hashedPassword);

            return userRep.save(user);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la création de l'utilisateur", e);
        }
    }

    public Optional<Utilisateur> getUserById(Integer id){//Optional<Utilisateur> permet de gérer proprement les cas où l'utilisateur pourrait ne pas être trouvé,
        return userRep.findById(id);
    }

    public void deleteUser(Integer id) {
        if (!userRep.existsById(id)) {
            throw new RuntimeException("Utilisateur avec ID " + id + " non trouvé.");
        }
        userRep.deleteById(id);
    }


    public Utilisateur updateUser(Integer id, Utilisateur newUser){
        Optional<Utilisateur> oldUser =userRep.findById(id);
        if(oldUser.isPresent()){
            Utilisateur updateUtilisateur =oldUser.get();
            updateUtilisateur.setNom(newUser.getNom());
            updateUtilisateur.setEmail(newUser.getEmail());
            updateUtilisateur.setMdp(updateUtilisateur.getMdp());
            updateUtilisateur.setTelephone(newUser.getTelephone());
            updateUtilisateur.setRoles(updateUtilisateur.getRoles());
            return userRep.save(updateUtilisateur);
        }
        else{
            return null;
        }
    }

    public void assignAdminRole(Integer userId) {
        Utilisateur user = userRep.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));

        Role adminRole = roleRep.findByName(ERole.ADMIN)
                .orElseThrow(() -> new EntityNotFoundException("Role ADMIN not found"));

        user.getRoles().add(adminRole);
        userRep.save(user);
    }

    public void assignRole(Integer userId, ERole roleName) {
        Optional<Utilisateur> userOpt = userRep.findById(userId);
        Utilisateur user = userOpt.orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));

        Optional<Role> roleOpt = roleRep.findByName(roleName);
        Role role = roleOpt.orElseThrow(() -> new EntityNotFoundException("Role not found: " + roleName));

        if (!user.getRoles().contains(role)) {
            user.getRoles().add(role);
            userRep.save(user);
        }
    }
    public void removeRole(Integer userId, ERole roleName) {
        Optional<Utilisateur> userOptional = userRep.findById(userId);
        if (userOptional.isPresent()) {
            Utilisateur user = userOptional.get();
            user.getRoles().removeIf(role -> role.getName().equals(roleName));
            userRep.save(user);
        }
    }


}
