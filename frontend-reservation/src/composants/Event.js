import React, { useEffect, useState } from "react";
import axiosInstance from "./gestiontoken/token";
import dayjs from "dayjs";
import './css/Event.css';
import { toast } from "react-toastify";

function Event({ organisateurId }) {
  // listevent
  const [listEvents, setListEvents] = useState([]);

  const [editingEvent, setEditingEvent] = useState(null); // Pour gérer l'événement en cours de modification
  const [formUpdateData, setformUpdateData] = useState({
    titre: "",
    lieu: "",
    date: "",
    placesDisp: "",
    description: "",
    imageUrl: "",
  }); // Données du formulaire pour mise à jour

  // ajout
  const [addEvent, setAddEvent] = useState(false); // Pour afficher ou cacher le formulaire d'ajout d'event
  const [formAjoutData, setformAjoutData] = useState({
    titre: "",
    lieu: "",
    date: "",
    placesDisp: "",
    description: "",
    imageUrl: "",
  }); // Données pour un nouvel event

  // searchMat
  const [searchQueryTitle, setsearchQuerytitle] = useState(""); // Recherche par titre

  // récupération des événements
  useEffect(() => {
    ListEvent();
  }, []);

  
  useEffect(() => {
    const token = localStorage.getItem("token");
    console.log("Mon token:", token);
    if (token) {
      const payload = JSON.parse(atob(token.split('.')[1]));
      console.log("Payload JWT décodé:", payload);
    }
  }, []);

 async function ListEvent() {
  try {
    const response = await axiosInstance("/api/events/get-events");
    const events = response.data;

    const validEvents = events.filter(e => typeof e === "object" && e !== null && "titre" in e);
    setListEvents(validEvents);
    console.log("Événements filtrés :", validEvents);
  } catch (error) {
    console.error("Erreur lors de la récupération :", error);
  }
}

  // Ajouter un event 
async function ajouterEvent() {
  try {
    if (!formAjoutData.titre || !formAjoutData.date || !formAjoutData.lieu) {
      toast.warning("Merci de remplir tous les champs obligatoires");
      return;
    }

    const eventToSend = {
      titre: formAjoutData.titre,
      description: formAjoutData.description,
      lieu: formAjoutData.lieu,
      placesDisp: parseInt(formAjoutData.placesDisp, 10),
      date: dayjs(formAjoutData.date).format("YYYY-MM-DDTHH:mm"),
      imageUrl: formAjoutData.imageUrl,
    };

    const response = await axiosInstance.post("/api/events/create-event", eventToSend);

    if (response.status === 201) {
      toast.success("Événement ajouté !");
      setformAjoutData({
        titre: "",
        lieu: "",
        date: "",
        placesDisp: "",
        description: "",
        imageUrl: "",
      });
      setAddEvent(false);
      ListEvent();
    }
  } catch (error) {
    console.error("Erreur lors de l'ajout", error.response?.data || error.message);
    toast.error("Erreur lors de l'ajout de l'événement !");
  }
} 

  // Supprimer un event
  async function supprimerEvent(id) {
    try {
      await axiosInstance.delete(`/api/events/delete-event/${id}`);
      toast.success("Événement supprimé !");
      ListEvent(); // Rafraîchir la liste après suppression
    } catch (error) {
      console.log("Erreur suppression ", error);
    }
  }

  // Remplir le formulaire de modification
  function modifierEvent(event) {
    setEditingEvent(event.id);
    setformUpdateData({
  titre: event.titre || "",
  lieu: event.lieu || "",
  date: event.date || "",
  placesDisp: event.placesDisp || "",
  description: event.description || "",
  imageUrl: event.imageUrl || "",
});
  }

  async function validerModification(id) {
  try {
    console.log('ID:', id);

    // Corrige le format de la date si nécessaire
    const dateWithSeconds =
      formUpdateData.date.length === 16
        ? formUpdateData.date + ":00"
        : formUpdateData.date;

    const dataToSend = {
      ...formUpdateData,
      date: dateWithSeconds,
    };

    const response = await axiosInstance.put(`/api/events/update-event/${id}`, dataToSend);

    if (response.status === 200) {
      toast.success("Événement modifié !");
      setEditingEvent(null);
      ListEvent(); // Rafraîchir la liste après modification
    }
  } catch (error) {
    console.log("Erreur modification ", error);
  }
}


  // Recherche simple par titre
  const filteredEvents = listEvents.filter(
  (e) => typeof e.titre === "string" && e.titre.toLowerCase().includes(searchQueryTitle.toLowerCase())
);

  return (
    <div>
      <h2>Gestion des Événements</h2>

      {/* Recherche */}
      <input
        type="text"
        placeholder="Rechercher par titre..."
        value={searchQueryTitle}
        onChange={(e) => setsearchQuerytitle(e.target.value)}
      />

      {/* Bouton pour afficher formulaire ajout */}
      <button onClick={() => setAddEvent(!addEvent)}>
        {addEvent ? "Annuler" : "Ajouter un événement"}
      </button>

      {/* Formulaire ajout */}
      {addEvent && (
        <div>
          <h3>Ajouter un Événement</h3>
          <input
            type="text"
            placeholder="Titre"
            value={formAjoutData.titre}
            onChange={(e) => setformAjoutData({ ...formAjoutData, titre: e.target.value })}
          />
          <input
            type="text"
            placeholder="Lieu"
            value={formAjoutData.lieu}
            onChange={(e) => setformAjoutData({ ...formAjoutData, lieu: e.target.value })}
          />
          <input
            type="datetime-local"
            value={formAjoutData.date}
            onChange={(e) => setformAjoutData({ ...formAjoutData, date: e.target.value })}
          />
          <input
            type="number"
            placeholder="Places disponibles"
            value={formAjoutData.placesDisp}
            onChange={(e) => setformAjoutData({ ...formAjoutData, placesDisp: e.target.value })}
          />
          <textarea
            placeholder="Description"
            value={formAjoutData.description}
            onChange={(e) => setformAjoutData({ ...formAjoutData, description: e.target.value })}
          />
          <input
            type="text"
            placeholder="URL de l'image"
            value={formAjoutData.imageUrl}
            onChange={(e) => setformAjoutData({ ...formAjoutData, imageUrl: e.target.value })}
          />

          <button onClick={ajouterEvent}>Valider</button>


        </div>
      )}

      {/* Liste des événements */}
      <h3>Liste des Événements</h3>
      {filteredEvents.map((event) => (
        <div key={event.id} style={{ border: "1px solid black", margin: "10px", padding: "10px" }}>
          {editingEvent === event.id ? (
            // Formulaire modification
            <div>

              <input
                type="text"
                value={formUpdateData.titre}
                onChange={(e) => setformUpdateData({ ...formUpdateData, titre: e.target.value })}
              />
              <input
                type="text"
                value={formUpdateData.lieu}
                onChange={(e) => setformUpdateData({ ...formUpdateData, lieu: e.target.value })}
              />
              <input
                type="datetime-local"
                value={dayjs(formUpdateData.date).format("YYYY-MM-DDTHH:mm:ss")}
                onChange={(e) => setformUpdateData({ ...formUpdateData, date: e.target.value })}
              />
              <input
                type="number"
                value={formUpdateData.placesDisp}
                onChange={(e) => setformUpdateData({ ...formUpdateData, placesDisp: e.target.value })}
              />
              <textarea
                value={formUpdateData.description}
                onChange={(e) => setformUpdateData({ ...formUpdateData, description: e.target.value })}
              />
              <input
                type="text"
                placeholder="URL de l'image"
                value={formUpdateData.imageUrl}
                onChange={(e) => setformUpdateData({ ...formUpdateData, imageUrl: e.target.value })}
              />
              <button onClick={() => validerModification(event.id)}>Enregistrer</button>
              <button onClick={() => setEditingEvent(null)}>Annuler</button>
            </div>
          ) : (
            // Affichage normal
            <div>
              {console.log("Image URL de l'événement :", event.imageUrl)}
              {event.imageUrl && (
                <div>
                  <img src={event.imageUrl} alt="Affiche événement" style={{ maxWidth: "300px", marginTop: "10px" }} />
                </div>
              )}
              <p><strong>Titre :</strong> {event.titre}</p>
              <p><strong>Lieu :</strong> {event.lieu}</p>
              <p><strong>Date :</strong> {dayjs(event.date).format("DD/MM/YYYY HH:mm")}</p>
              <p><strong>Places disponibles :</strong> {event.placesDisp}</p>
              <p><strong>Description :</strong> {event.description}</p>
              <div>
                <button onClick={() => modifierEvent(event)}>Modifier</button>
                <button onClick={() => supprimerEvent(event.id)}>Supprimer</button>
              </div>
            </div>
          )}
        </div>
      ))}
    </div>
  );
}

export default Event;
