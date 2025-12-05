import React, { useEffect, useState, useCallback } from "react";
import axiosInstance from "./gestiontoken/token";
import dayjs from "dayjs";
import './css/Event.css';
import { toast } from "react-toastify";

function Event({ organisateurId }) {
  const [listEvents, setListEvents] = useState([]);
  const [editingEvent, setEditingEvent] = useState(null);

  const [formUpdateData, setformUpdateData] = useState({
    titre: "",
    lieu: "",
    date: "",
    placesDisp: "",
    description: "",
    imageUrl: "",
    ville: "",
  });

  const [addEvent, setAddEvent] = useState(false);
  const [formAjoutData, setformAjoutData] = useState({
    titre: "",
    lieu: "",
    date: "",
    placesDisp: "",
    description: "",
    imageUrl: "",
    ville: "",
  });

  const [searchQueryTitle, setsearchQuerytitle] = useState(""); 
  const [weatherData, setWeatherData] = useState({});

  const ListEvent = useCallback(async () => {
    try {
      const response = await axiosInstance("/api/events/get-events");
      const events = Array.isArray(response.data) ? response.data : [];
      setListEvents(events);

      events.forEach(async (event) => {
        if (event.ville) {
          const weather = await fetchWeather(event.ville);
          setWeatherData(prev => ({ ...prev, [event.id]: weather }));
        }
      });
    } catch (error) {
      console.error("Erreur lors de la récupération :", error);
    }
  }, []);

  useEffect(() => {
    ListEvent();
  }, [ListEvent]);

  async function fetchWeather(ville) {
    try {
      const response = await axiosInstance.get(`/api/weather/${ville}`);
      return response.data;
    } catch (error) {
      console.error("Erreur météo :", error.response?.data || error.message);
      return null;
    }
  }

  async function ajouterEvent() {
    try {
      if (!formAjoutData.titre || !formAjoutData.date || !formAjoutData.lieu) {
        toast.warning("Merci de remplir tous les champs obligatoires");
        return;
      }

      const eventToSend = {
        ...formAjoutData,
        placesDisp: parseInt(formAjoutData.placesDisp, 10),
        date: dayjs(formAjoutData.date).format("YYYY-MM-DDTHH:mm:ss"), // ajout des secondes
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
          ville: "",
        });
        setAddEvent(false);
        ListEvent();
      }
    } catch (error) {
      console.error("Erreur lors de l'ajout", error.response?.data || error.message);
      toast.error("Erreur lors de l'ajout de l'événement !");
    }
  } 

  async function supprimerEvent(id) {
    try {
      await axiosInstance.delete(`/api/events/delete-event/${id}`);
      toast.success("Événement supprimé !");
      ListEvent(); 
    } catch (error) {
      console.log("Erreur suppression ", error);
    }
  }

  function modifierEvent(event) {
    setEditingEvent(event.id);
    setformUpdateData({
      ...event,
      date: event.date ? dayjs(event.date).format("YYYY-MM-DDTHH:mm") : "",
    });
  }

  async function validerModification(id) {
    try {
      const dataToSend = {
        ...formUpdateData,
        date: dayjs(formUpdateData.date).format("YYYY-MM-DDTHH:mm:ss"), // backend veut secondes
      };
      const response = await axiosInstance.put(`/api/events/update-event/${id}`, dataToSend);
      if (response.status === 200) {
        toast.success("Événement modifié !");
        setEditingEvent(null);
        ListEvent();
      }
    } catch (error) {
      console.log("Erreur modification ", error.response?.data || error.message);
    }
  }

  const filteredEvents = listEvents.filter(
    (e) => typeof e.titre === "string" && e.titre.toLowerCase().includes(searchQueryTitle.toLowerCase())
  );

  return (
    <div>
      <h2>Gestion des Événements</h2>

      <input
        type="text"
        placeholder="Rechercher par titre..."
        value={searchQueryTitle}
        onChange={(e) => setsearchQuerytitle(e.target.value)}
      />

      <button onClick={() => setAddEvent(!addEvent)}>
        {addEvent ? "Annuler" : "Ajouter un événement"}
      </button>

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
          <input
            type="text"
            placeholder="Ville"
            value={formAjoutData.ville}
            onChange={(e) => setformAjoutData({ ...formAjoutData, ville: e.target.value })}
          />
          <button onClick={ajouterEvent}>Valider</button>
        </div>
      )}

      <h3>Liste des Événements</h3>
      {filteredEvents.map((event) => (
        <div key={event.id} style={{ border: "1px solid black", margin: "10px", padding: "10px" }}>
          {editingEvent === event.id ? (
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
                value={formUpdateData.date}
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
              <input
                type="text"
                placeholder="Ville"
                value={formUpdateData.ville}
                onChange={(e) => setformUpdateData({ ...formUpdateData, ville: e.target.value })}
              />
              <button onClick={() => validerModification(event.id)}>Enregistrer</button>
              <button onClick={() => setEditingEvent(null)}>Annuler</button>
            </div>
          ) : (
            <div>
              {event.imageUrl && (
                <div>
                  <img src={event.imageUrl} alt="Affiche événement" style={{ maxWidth: "300px", marginTop: "10px" }} />
                </div>
              )}
              <p><strong>Titre :</strong> {event.titre}</p>
              <p><strong>Lieu :</strong> {event.lieu}</p>
              <p><strong>Ville :</strong> {event.ville}</p>
              <p><strong>Date :</strong> {dayjs(event.date).format("DD/MM/YYYY HH:mm:ss")}</p>
              <p><strong>Places disponibles :</strong> {event.placesDisp}</p>
              <p><strong>Description :</strong> {event.description}</p>

              {weatherData[event.id] && (
                <p><strong>Météo :</strong> {weatherData[event.id].description} à {weatherData[event.id].temp}°C</p>
              )}

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
