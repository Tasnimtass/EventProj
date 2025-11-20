import React, { useState, useEffect } from "react";
import axiosInstance from "./gestiontoken/token";
import './css/EvenementAVenir.css'

function EvenementsAVenir({ goToPage }) {
 const [evenements, setEvenements] = useState([]);
  const [titreRecherche, setTitreRecherche] = useState("");

  useEffect(() => {
    axiosInstance.get("/api/events/get-events")
      .then(response => setEvenements(response.data))
      .catch(error => console.error("Erreur lors de la récupération :", error));
  }, []);

  // Filtrage par titre
  const evenementsFiltres = evenements.filter(evt =>
    evt.titre.toLowerCase().includes(titreRecherche.toLowerCase())
  );

  return (
    <div className="accueil-container">
      <header className="header">
        <div className="logo-title">
          <img src="./logoevent.png" alt="Logo" className="logo" />
          <h1>EventMan_App</h1>
        </div>
      </header>

      <div className="search-bar">
        <input
          type="text"
          placeholder="Rechercher un événement par titre..."
          value={titreRecherche}
          onChange={(e) => setTitreRecherche(e.target.value)}
        />
      </div>

      <div className="events-container">
        {evenementsFiltres.map(evt => (
          evt.id && (
            <div key={evt.id} className="event-card">
                <div>
                  <img src={evt.imageUrl} alt={evt.titre}style={{ maxWidth: "300px", marginTop: "10px" }} />
                </div>
              <h3>{evt.titre}</h3>
              <p><b>Date:</b> {new Date(evt.date).toLocaleDateString()}</p>
              <p><b>Lieu:</b> {evt.lieu}</p>
              <button className="butApp" onClick={() => goToPage('reserver', evt.id)}>Reserver</button>
            </div>
          )
        ))}
      </div>
    </div>
  );
}
export default EvenementsAVenir;
