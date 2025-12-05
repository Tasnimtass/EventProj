package com.miniprojet.miniprojet.service;

import com.miniprojet.miniprojet.DTO.EvenementDTO;
import com.miniprojet.miniprojet.entity.Evenement;
import com.miniprojet.miniprojet.entity.Reservation;
import com.miniprojet.miniprojet.entity.Utilisateur;
import com.miniprojet.miniprojet.repository.eventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class EvenementService {
    @Autowired
    private eventRepository evRep;

    @Autowired
    private UtilisateurService userSer;

    public List<EvenementDTO> getEventDTOs() {
        List<Evenement> eventList = evRep.findAll();
        if (eventList.isEmpty()) {
            throw new NoSuchElementException("Aucun événement trouvé");
        }
        return eventList.stream()
                .map(EvenementDTO::new)
                .toList();
    }


    public Optional<Evenement> getEventById(Integer id){
        return evRep.findById(id);
    }

    public Evenement createEvent(Evenement event, Integer userID) {
        Optional<Utilisateur> organisateurOpt = userSer.getUserById(userID);
        if (organisateurOpt.isPresent()) {
            event.setOrganisateur(organisateurOpt.get());
            return evRep.save(event);
        } else {
            throw new RuntimeException("Organisateur avec ID " + userID + " non trouvé");
        }
    }

    public void deleteEvent(Integer id){
        evRep.deleteById(id);
    }

    public Evenement updateEvent(Integer id,Evenement newEvent){
        Optional<Evenement> oldEvent =evRep.findById(id);
        if(oldEvent.isPresent()){
            Evenement updateEvent =oldEvent.get();
            updateEvent.setTitre(newEvent.getTitre());
            updateEvent.setDate(newEvent.getDate());
            updateEvent.setPlacesDisp(newEvent.getPlacesDisp());
            updateEvent.setLieu(newEvent.getLieu());
            updateEvent.setVille(newEvent.getVille());
            updateEvent.setDescription(newEvent.getDescription());
            updateEvent.setImageUrl(newEvent.getImageUrl());
            return evRep.save(updateEvent);

        }
        else{
            return null;
        }
    }
}
