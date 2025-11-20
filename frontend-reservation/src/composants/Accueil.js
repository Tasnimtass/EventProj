import React, { useState, useEffect } from "react";
import axios from "axios";
import './css/accueil.css';




function Accueil({ goToPage }) {
  const [evenements, setEvenements] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    axios("http://localhost:1111/api/events/get-events")
      .then(response => {
        // Si la réponse est vide (204 ou tableau vide)
        if (response.data && Array.isArray(response.data)) {
          setEvenements(response.data);
        } else {
          setEvenements([]); // vide
        }
      })
      .catch(error => {
        console.error("Erreur lors de la récupération :", error);
        setEvenements([]); // erreur → vide
      })
      .finally(() => setLoading(false));
  }, []);

  const premiersEvenements = evenements.slice(0, 3);

  return (
    <div className="accueil-container">
      <header className="header">
        <div className="logo-title">
          <img src="./logoevent.png" alt="Logo" className="logo" />
          <h1>EventMan_App</h1>
        </div>
      </header>

      <div className="events-container">
        {loading ? (
          <p>Chargement des événements...</p>
        ) : premiersEvenements.length === 0 ? (
          <p>Aucun événement disponible pour le moment.</p>
        ) : (
          premiersEvenements.map(evt => (
            evt.id && (
              <div key={evt.id} className="event-card">
                <h3>{evt.titre}</h3>
                <div>
                  <img src={evt.imageUrl} alt="Affiche événement" style={{ maxWidth: "300px", marginTop: "10px" }} />
                </div>
                <p><b>Date:</b> {new Date(evt.date).toLocaleDateString()}</p>
                <p><b>Lieu:</b> {evt.lieu}</p>
                
                <button className="butApp" onClick={() => goToPage('reserver', evt.id)}>Réserver</button>
              </div>
            )
          ))
        )}
      </div>

      <div style={{ textAlign: "center", marginTop: "20px" }}>
        <button className="butApp" onClick={() => goToPage("evenementsavenir")}>
          Voir tous les événements
        </button>
      </div>
    </div>
  );
}

export default Accueil;
