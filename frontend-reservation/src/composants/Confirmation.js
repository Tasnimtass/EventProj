import React from "react";
import './css/Confirmation.css'
function Confirmation({ event, goToPage }) {
  if (!event) {
    return <p>Aucune information d'événement disponible.</p>;
  }

  return (
    <div style={{ padding: "20px" }}>
      <h2> Réservation Confirmée !</h2>
      <p>Merci pour votre réservation. Voici les détails de l'événement :</p>

      <div style={{ marginBottom: "20px" }}>
        <ul>
          <li><strong>Titre :</strong> {event.titre}</li>
          <li><strong>Lieu :</strong> {event.lieu}</li>
          <li><strong>Date :</strong> {new Date(event.date).toLocaleString()}</li>
          <li><strong>Description :</strong> {event.description}</li>
        </ul>
      </div>

      <div>
        <button onClick={() => goToPage('accueil')} style={{ marginRight: "10px" }}>
          Retour à l'accueil
        </button>
        <button onClick={() => goToPage('evenementsavenir')}>
          Voir les autres événements
        </button>
      </div>
    </div>
  );
}

export default Confirmation;
