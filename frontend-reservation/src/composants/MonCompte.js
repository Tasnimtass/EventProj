import React, { useEffect, useState } from 'react';
import axiosInstance from './gestiontoken/token';
import './css/MonCompte.css'
import { toast } from 'react-toastify';

const MonCompte = () => {
  const [user, setUser] = useState(null);
  const [form, setForm] = useState({ nom: '', email: '', telephone: '' });
  const [editMode, setEditMode] = useState(false);
  const [evenements, setEvenements] = useState([]);

  useEffect(() => {
    const token = localStorage.getItem('token');
    if (!token) return;

    let payload;
    try {
      payload = JSON.parse(atob(token.split('.')[1]));
    } catch (err) {
      console.error("Token invalide :", err);
      return;
    }

    const userId = payload?.id;
    if (!userId) return;

    // Récupération infos utilisateur
    axiosInstance.get(`/api/users/get-user-by-id/${userId}`)
      .then(res => {
        setUser(res.data);
        setForm({
          nom: res.data.nom,
          email: res.data.email,
          telephone: res.data.telephone
        });
      })
      .catch(err => console.error("Erreur récupération utilisateur :", err));

    // Récupération des événements réservés
    axiosInstance.get(`/api/reservations/utilisateur/${userId}/evenements-reserves`)
  .then(res => {
    console.log("Événements reçus :", res.data);
    setEvenements(res.data);
  })

  }, []);

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleUpdate = () => {
    if (!user?.id) return;
    axiosInstance.put(`/api/users/update-user/${user.id}`, form)
      .then(res => {
        toast.success("Informations mises à jour !");
        setUser(res.data);
        setEditMode(false);
      })
      .catch(err => console.error("Erreur modification :", err));
  };

  const handleLogout = () => {
    localStorage.clear();
    window.location.href = '/';
  };

  if (!user) return <div>Chargement des informations...</div>;

  return (
    <div style={{ padding: '20px', maxWidth: '600px', margin: 'auto' }}>
      <h2 style={{ textAlign: 'center' }}>Mon Profil</h2>

      {!editMode ? (
        <>
          <p><strong>Nom :</strong> {user.nom}</p>
          <p><strong>Email :</strong> {user.email}</p>
          <p><strong>Téléphone :</strong> {user.telephone}</p>
          <div style={{ marginTop: '20px', textAlign: 'center' }}>
            <button onClick={() => setEditMode(true)} style={{ marginRight: '10px' }}>Modifier</button>
            <button onClick={handleLogout}>Déconnexion</button>
          </div>
        </>
      ) : (
        <>
          <label>Nom:</label>
          <input name="nom" value={form.nom} onChange={handleChange} /><br />
          <label>Email:</label>
          <input name="email" value={form.email} onChange={handleChange} /><br />
          <label>Téléphone:</label>
          <input name="telephone" value={form.telephone} onChange={handleChange} /><br />
          <div style={{ marginTop: '20px' }}>
            <button onClick={handleUpdate} style={{ marginRight: '10px' }}>Enregistrer</button>
            <button onClick={() => setEditMode(false)}>Annuler</button>
          </div>
        </>
      )}

      <h3 style={{ marginTop: '30px', textAlign: 'center' }}>Mes Événements Réservés</h3>

      {evenements.length > 0 ? (
  <ul>
    {evenements
      .filter(ev => typeof ev === 'object' && ev !== null && ev.date) // On garde que les vrais objets avec une date
      .map((ev, index) => {
        const date = new Date(ev.date + 'Z'); // Ajoute 'Z' pour forcer UTC


        return (
          <li key={ev.id || index} style={{ marginBottom: '15px' }}>
           <div> <img src={ev.imageUrl} alt="Affiche événement" style={{ maxWidth: "180px", marginTop: "10px" }} /></div>
            <strong>{ev.titre}</strong><br />
            {date.toLocaleString('fr-FR', {
              weekday: 'long',
              year: 'numeric',
              month: 'long',
              day: 'numeric',
              hour: '2-digit',
              minute: '2-digit'
            })}<br />
            <em>{ev.lieu}</em>
            
          </li>
        );
      })}
  </ul>
) : (
  <p style={{ textAlign: 'center' }}>Aucun événement réservé.</p>
)}

    </div>
  );
};

export default MonCompte;
