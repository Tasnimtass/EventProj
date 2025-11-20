package com.miniprojet.miniprojet.DTO;

import com.miniprojet.miniprojet.entity.Utilisateur;

import java.util.List;
import java.util.stream.Collectors;

public class UtilisateurDTO {
    private Integer id;
    private String nom;
    private String email;
    private String telephone;
    private List<String> roles;

    public UtilisateurDTO(Utilisateur user) {
        this.id = user.getId();
        this.nom = user.getNom();
        this.email = user.getEmail();
        this.telephone = user.getTelephone();
        this.roles = user.getRoles().stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toList());
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getNom() {
        return nom;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getTelephone() {
        return telephone;
    }
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
    public List<String> getRoles() {
        return roles;
    }
    public void setRoles(List<String> roles) {
        this.roles = roles;
    }


}
