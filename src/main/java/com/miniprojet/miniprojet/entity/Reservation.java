package com.miniprojet.miniprojet.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Utilisateur user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "evenement_id", nullable = false)
    private Evenement evenement;

    private int nbrPlaces;

    @Column(name = "date_res")
    @CreationTimestamp //datesystem
    private LocalDateTime dateRes;

    public Reservation() {}

    public Reservation(Utilisateur user, Evenement evenement, int nbrPlaces, LocalDateTime dateRes) {
        this.user = user;
        this.evenement = evenement;
        this.nbrPlaces = nbrPlaces;
        this.dateRes = dateRes;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Utilisateur getUser() {
        return user;
    }

    public void setUser(Utilisateur user) {
        this.user = user;
    }

    public Evenement getEvenement() {
        return evenement;
    }

    public void setEvenement(Evenement evenement) {
        this.evenement = evenement;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reservation that = (Reservation) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
