package com.miniprojet.miniprojet.DTO;

import com.miniprojet.miniprojet.entity.Reservation;

import java.time.LocalDateTime;

public class ReservationDTO {
    private Integer id ;
    private int nbrPlaces;
    private LocalDateTime dateRes;
    private String utilisateurNom;
    private String evenementTitre;
    public ReservationDTO() {}


    public ReservationDTO(Reservation reservation) {
        this.id = reservation.getId();
        this.nbrPlaces = reservation.getNbrPlaces();
        this.dateRes = reservation.getDateRes();

        if (reservation.getUser() != null) {
            this.utilisateurNom = reservation.getUser().getNom();
        }

        if (reservation.getEvenement() != null) {
            this.evenementTitre = reservation.getEvenement().getTitre();
        } else {
            this.evenementTitre = "Événement supprimé ou non trouvé";
        }
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public int getNbrPlaces() {
        return nbrPlaces;
    }
    public void setNbrPlaces(int nbrPlaces) {
        this.nbrPlaces = nbrPlaces;
    }
    public LocalDateTime getDateRes() {
        return dateRes;
    }
    public void setDateRes(LocalDateTime dateRes) {
        this.dateRes = dateRes;
    }
    public String getUtilisateurNom() {
        return utilisateurNom;
    }
    public void setUtilisateurNom(String utilisateurNom) {
        this.utilisateurNom = utilisateurNom;
    }
    public String getEvenementTitre() {
        return evenementTitre;
    }
    public void setEvenementTitre(String evenementTitre) {
        this.evenementTitre = evenementTitre;
    }

}
