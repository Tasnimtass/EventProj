package com.miniprojet.miniprojet.service;

import com.miniprojet.miniprojet.DTO.EvenementDTO;
import com.miniprojet.miniprojet.entity.Evenement;
import com.miniprojet.miniprojet.entity.Reservation;
import com.miniprojet.miniprojet.entity.ReservationEmail;
import com.miniprojet.miniprojet.entity.Utilisateur;
import com.miniprojet.miniprojet.repository.reservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class reservationService {

    @Autowired
    private reservationRepository resRep;

    @Autowired
    private UtilisateurService userSer;

    @Autowired
    private EvenementService eventSer;

    @Autowired
    private MessageChannel reservationEmailChannel;



    public Optional<Reservation> getReservationById(Integer id){
        return resRep.findById(id);
    }
    public List<Reservation> getReservations(){
        return resRep.findAll();
    }
    public void deleteReservation(Integer id){
        resRep.deleteById(id);
    }

    public Reservation createReservation(Reservation reservation, Integer idUser, Integer idEvent) {
        Optional<Utilisateur> user = userSer.getUserById(idUser);
        Optional<Evenement> event = eventSer.getEventById(idEvent);
        if (user.isEmpty() || event.isEmpty()) {
            throw new RuntimeException("Utilisateur ou événement avec cet ID n'existe pas.");
        }
        if (reservation.getNbrPlaces() > event.get().getPlacesDisp() || reservation.getNbrPlaces() < 1) {
            throw new RuntimeException("Nombre de places invalide (disponibles: " + event.get().getPlacesDisp() + ")");
        }
        event.get().setPlacesDisp(event.get().getPlacesDisp() - reservation.getNbrPlaces());
        reservation.setUser(user.get());
        reservation.setEvenement(event.get());
        reservation.setDateRes(LocalDateTime.now());
        Reservation saved = resRep.save(reservation);
        Utilisateur utilisateur = user.get();
        Evenement evenement = event.get();
        ReservationEmail email = new ReservationEmail(
                utilisateur.getEmail(),
                "Confirmation de réservation",
                "Bonjour " + utilisateur.getNom() + ",\n\nVotre réservation pour l'événement '" + evenement.getTitre() + "' a bien été enregistrée.\nNombre de places : " + reservation.getNbrPlaces() + "."
        );
        reservationEmailChannel.send(MessageBuilder.withPayload(email).build());//playload c'est le contenu qui est email puis il est nevoyee a la channel reservation email
        return saved;
    }

    public Reservation updateReservation(Integer id, Reservation newReservation) {
        Optional<Reservation> oldReservation = resRep.findById(id);

        if (oldReservation.isPresent()) {
            Reservation updateReservation = oldReservation.get();
            Evenement evenement = updateReservation.getEvenement();
            if (newReservation.getNbrPlaces() <= evenement.getPlacesDisp()) {

                updateReservation.setNbrPlaces(newReservation.getNbrPlaces());
                updateReservation.setDateRes(newReservation.getDateRes());
                evenement.setPlacesDisp(evenement.getPlacesDisp() - newReservation.getNbrPlaces());

                resRep.save(updateReservation);
                eventSer.updateEvent(evenement.getId(),evenement);  // Mise à jour de l'événement avec le nouveau nombre de places disponibles

                return updateReservation;
            } else {
                // Si le nombre de places demandées dépasse les places disponibles
                throw new RuntimeException("Nombre de places demandé dépasse les places disponibles dans l'événement.");
            }
        } else {
            // Si la réservation n'existe pas
            return null;
        }
    }
    public List<EvenementDTO> getEvenementsReservesParUtilisateur(Integer userId) {
        List<Reservation> reservations = resRep.findByUser_Id(userId);
        return reservations.stream()
                .map(Reservation::getEvenement)
                .distinct()
                .map(EvenementDTO::new)
                .toList();
    }





}
