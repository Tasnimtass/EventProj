package com.miniprojet.miniprojet.controller;

import com.miniprojet.miniprojet.DTO.EvenementDTO;
import com.miniprojet.miniprojet.entity.Evenement;
import com.miniprojet.miniprojet.entity.Reservation;
import com.miniprojet.miniprojet.entity.Utilisateur;
import com.miniprojet.miniprojet.repository.reservationRepository;
import com.miniprojet.miniprojet.service.EvenementService;
import com.miniprojet.miniprojet.service.UtilisateurService;
import com.miniprojet.miniprojet.service.reservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.miniprojet.miniprojet.DTO.ReservationDTO;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/reservations")
public class reservationController {
    @Autowired
    private reservationService resSer;

    @Autowired
    private UtilisateurService userSer;


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/get-reservations")
    public ResponseEntity<List<ReservationDTO>> getRes() {
        try {
            List<Reservation> reservationList = resSer.getReservations();
            if (reservationList.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            List<ReservationDTO> reservationDTOList = reservationList.stream()
                    .map(ReservationDTO::new)
                    .toList();

            return new ResponseEntity<>(reservationDTOList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/get-reservation-by-id/{id}")
    public ResponseEntity<ReservationDTO> getReservationById(@PathVariable Integer id) {
        Optional<Reservation> reservation = resSer.getReservationById(id);
        if (reservation.isPresent()) {
            return new ResponseEntity<>(new ReservationDTO(reservation.get()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update-reservation/{id}")
    public ResponseEntity<ReservationDTO> updateReservation(@PathVariable Integer id, @RequestBody Reservation reservation)
    {
    Reservation updatedReservation = resSer.updateReservation(id, reservation);
        if (updatedReservation != null) {
            return new ResponseEntity<>(new ReservationDTO(updatedReservation), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create-reservation/{idUser}/{idEvent}")
    public ResponseEntity<?> createReservation(
            @RequestBody Reservation reservation,
            @PathVariable Integer idUser,
            @PathVariable Integer idEvent) {
        try {
            Reservation savedReservation = resSer.createReservation(reservation, idUser, idEvent);
            Map<String, Object> response = new HashMap<>();
            response.put("reservation", new ReservationDTO(savedReservation));
            Optional<Utilisateur> user=userSer.getUserById(idUser);
            response.put("message", "E-mail envoyé à : " + user.get().getEmail());

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete-reservation/{id}")
    public ResponseEntity<HttpStatus> deleteReservation(@PathVariable Integer id){
        resSer.deleteReservation(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/utilisateur/{userId}/evenements-reserves")
    public ResponseEntity<?> getEvenementsReservesParUtilisateur(@PathVariable Integer userId) {
        try {
            List<EvenementDTO> evenements = resSer.getEvenementsReservesParUtilisateur(userId);
            if (evenements.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(evenements);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur : " + e.getMessage());
        }
    }


}
