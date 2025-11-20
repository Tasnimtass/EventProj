package com.miniprojet.miniprojet.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.miniprojet.miniprojet.entity.Evenement;

import java.time.LocalDateTime;

public class EvenementDTO {
    private Integer id;
    private String titre;
    private String description;
    private String imageUrl;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime date;

    private String lieu;
    private int placesDisp;
    private String organisateurNom;

    public EvenementDTO(Evenement evenement) {
        this.id = evenement.getId();
        this.titre = evenement.getTitre();
        this.description = evenement.getDescription();
        this.date = evenement.getDate();
        this.lieu = evenement.getLieu();
        this.placesDisp = evenement.getPlacesDisp();
        this.organisateurNom = evenement.getOrganisateur().getNom();
        this.imageUrl = evenement.getImageUrl();
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getTitre() {
        return titre;
    }
    public void setTitre(String titre) {
        this.titre = titre;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public LocalDateTime getDate() {
        return date;
    }
    public void setDate(LocalDateTime date) {
        this.date = date;
    }
    public String getLieu() {
        return lieu;
    }
    public void setLieu(String lieu) {
        this.lieu = lieu;
    }
    public int getPlacesDisp() {
        return placesDisp;
    }
    public void setPlacesDisp(int placesDisp) {
        this.placesDisp = placesDisp;
    }
    public String getOrganisateurNom() {
        return organisateurNom;
    }
    public void setOrganisateurNom(String organisateurNom) {
        this.organisateurNom = organisateurNom;
    }
    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}

