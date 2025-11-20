import React, { useState, useEffect } from "react";
import axiosInstance from "./gestiontoken/token";
import './css/Reservation.css'
import { toast } from "react-toastify";

function Reserver({ goToPage, idEvent, userId }) {
  const [event, setEvent] = useState(null);
  const [showReservation, setShowReservation] = useState(false);
  const [places, setPlaces] = useState(1);

  useEffect(() => {
    const currentUserId = userId || parseInt(localStorage.getItem('userId'), 10);
    console.log("Utilisateur actuel :", currentUserId);

    if (!currentUserId) {
      toast.warn("Veuillez vous connecter pour effectuer une réservation.");
      goToPage('connexion');
      return;
    }

    setShowReservation(false); // Réinitialiser formulaire

    // Charger l'événement
    axiosInstance.get(`/api/events/get-event-by-id/${idEvent}`)
      .then((response) => setEvent(response.data))
      .catch((error) => console.error("Erreur lors de la récupération :", error));
  }, [idEvent, userId, goToPage]); // Pas besoin d'ajouter currentUserId ici

  function handleReservation() {
    const currentUserId = userId || parseInt(localStorage.getItem('userId'), 10);
    
    if (!currentUserId) {
      toast.warn("Utilisateur non connecté");
      goToPage('connexion');
      return;
    }

    const numPlaces = parseInt(places, 10);

    if (isNaN(numPlaces) || numPlaces <= 0 || (event && numPlaces > event.placesDisp)) {
      toast.error("Veuillez entrer un nombre valide de places.");
      return;
    }

    axiosInstance.post(`/api/reservations/create-reservation/${currentUserId}/${idEvent}`, {
      nbrPlaces: numPlaces
    })
    .then((response) => {
  toast.success("Réservation confirmée !");
  const message = response.data.message;
  if (message) {
    toast.info(message); // "E-mail envoyé à : ..."
  }
  goToPage('confirmation', { event });
})
    .catch((error) => {
      console.error("Erreur lors de la réservation :", error);
      toast.error("Erreur lors de la réservation.");
    });
  }

  return (
    <div>
      <h2>Détails de l'événement</h2>
      {event ? (
        <div>
          <p><strong>Titre :</strong> {event.titre}</p>
          <p><strong>Lieu :</strong> {event.lieu}</p>
          <p><strong>Date :</strong> {event.date}</p>
          <p><strong>Places disponibles :</strong> {event.placesDisp}</p>
          <p><strong>Description :</strong> {event.description}</p>

          {!showReservation ? (
            <button onClick={() => setShowReservation(true)}>Réserver</button>
          ) : (
            <div>
              <input
                type="number"
                min="1"
                max={event.placesDisp}
                value={places}
                onChange={(e) => setPlaces(e.target.value)}
              />
              <button onClick={handleReservation}>Valider</button>
            </div>
          )}
        </div>
      ) : (
        <p>Chargement...</p>
      )}
    </div>
  );
}

export default Reserver;
