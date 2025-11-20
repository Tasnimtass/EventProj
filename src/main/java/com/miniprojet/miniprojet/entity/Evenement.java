package com.miniprojet.miniprojet.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;

import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

@Entity
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
public class Evenement {
    @Id

    @GeneratedValue(strategy = GenerationType.IDENTITY)//pour prendre comme cle primaire et auto increment

    private Integer id;
    private String titre;

    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime date;

    private String lieu;

    private int placesDisp;

    @ManyToOne
    @JoinColumn(name = "organisateur_id", nullable = false)
    private Utilisateur organisateur;


    @OneToMany(mappedBy = "evenement", cascade = CascadeType.ALL, orphanRemoval = true)

    private List<Reservation> reservations = new ArrayList<>();

    private String imageUrl;

    


    public Evenement() {
    }

    public Evenement(String titre, String description, LocalDateTime date, String lieu, int placesDisp) {


        this.titre = titre;

        this.description = description;

        this.date = date;

        this.lieu = lieu;

        this.placesDisp = placesDisp;

    }

    public int getId() {

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

    public List<Reservation> getReservations() {

        return reservations;

    }

    public void setReservations(List<Reservation> reservations) {

        this.reservations = reservations;

    }
    public Utilisateur getOrganisateur() {
        return organisateur;
    }

    public void setOrganisateur(Utilisateur organisateur) {
        this.organisateur = organisateur;
    }
    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Evenement that = (Evenement) o;
        return id.equals(that.id);
    }

}
