package com.miniprojet.miniprojet.controller;

import com.miniprojet.miniprojet.entity.Evenement;
import com.miniprojet.miniprojet.entity.Utilisateur;
import com.miniprojet.miniprojet.service.EvenementService;
import com.miniprojet.miniprojet.service.UtilisateurService;
import com.miniprojet.miniprojet.service.reservationService;
import org.springframework.beans.factory.annotation.Autowired;
import com.miniprojet.miniprojet.DTO.EvenementDTO;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.security.Principal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/api/events")
public class eventController {

    @Autowired
    private EvenementService eventSer;

    @Autowired
    private UtilisateurService userSer;


    @GetMapping("/get-events")
    public ResponseEntity<List<EvenementDTO>> getEvents() {
        try {
            List<EvenementDTO> dtoList = eventSer.getEventDTOs();
            return new ResponseEntity<>(dtoList, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/get-event-by-id/{id}")
    public ResponseEntity<EvenementDTO> getEventById(@PathVariable Integer id) {
        Optional<Evenement> event = eventSer.getEventById(id);
        return event.map(value -> new ResponseEntity<>(new EvenementDTO(value), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


    @PreAuthorize("hasRole('ORGANISATEUR')")
    @PutMapping("/update-event/{id}")
    public ResponseEntity<EvenementDTO> updateEvent(@PathVariable Integer id, @RequestBody Evenement event) {
        Evenement updatedEvent = eventSer.updateEvent(id, event);
        if (updatedEvent != null) {
            return new ResponseEntity<>(new EvenementDTO(updatedEvent), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @PreAuthorize("hasRole('ORGANISATEUR')")
    @PostMapping("/create-event")
    public ResponseEntity<EvenementDTO> createEvent(@RequestBody Evenement event, Principal principal) {//Elle représente l'utilisateur authentifié dans la requête HTTP.
        try {
            String email = principal.getName();
            Utilisateur organisateur = userSer.getUser(email);

            Evenement savedEvent = eventSer.createEvent(event, organisateur.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(new EvenementDTO(savedEvent));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }


    @PreAuthorize("hasRole('ORGANISATEUR')")
    @DeleteMapping("/delete-event/{id}")
    public ResponseEntity<HttpStatus> deleteEvent(@PathVariable Integer id) {
        eventSer.deleteEvent(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
