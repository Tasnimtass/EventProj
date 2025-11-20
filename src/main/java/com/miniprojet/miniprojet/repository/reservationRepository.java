package com.miniprojet.miniprojet.repository;

import com.miniprojet.miniprojet.entity.Evenement;
import com.miniprojet.miniprojet.entity.Reservation;
import com.miniprojet.miniprojet.entity.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface reservationRepository extends JpaRepository<Reservation,Integer> {
    List<Reservation> findByUser_Id(Integer userId);
    Optional<Reservation> findByUserAndEvenement(Utilisateur user, Evenement evenement);



}
