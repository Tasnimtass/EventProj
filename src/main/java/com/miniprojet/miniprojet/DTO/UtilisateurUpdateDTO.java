package com.miniprojet.miniprojet.DTO;

public class UtilisateurUpdateDTO {
    private String nom;
    private String email;
    private String telephone;

    public UtilisateurUpdateDTO() {}

    public UtilisateurUpdateDTO(String nom, String email, String telephone) {
        this.nom = nom;
        this.email = email;
        this.telephone = telephone;
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
}

