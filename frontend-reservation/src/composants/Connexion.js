import axios from "axios";
import React, { useState } from "react";
import './css/Connexion.css';
import { toast } from 'react-toastify';

function Connexion({ Onlog, goToPage }) {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [errorMessage, setErrorMessage] = useState("");

  async function handleConnexion(e) {
    e.preventDefault();

    try {
      const response = await axios.post("http://localhost:1111/api/users/connexion", 
        { email: email, mdp: password }, 
        {
          headers: {
            'Content-Type': 'application/json'
          }
        }
      );

      if (response.status === 200) {
        const token = response.data.token;

        // Décode le token pour obtenir le userId et le rôle
        const payloadBase64 = token.split('.')[1];
        const payload = JSON.parse(atob(payloadBase64));

        console.log("Payload JWT : ", payload);  // ← DEBUG ICI

        const userId = payload.id;
        const roles = payload.roles || [];
        const role = roles.includes("ORGANISATEUR") ? "ORGANISATEUR" : (roles[0] || "UTILISATEUR");
        const nom=payload.nom;

        // Sauvegarde dans localStorage
        localStorage.setItem('token', token);
        localStorage.setItem('userId', userId);
        localStorage.setItem('role', role);
        localStorage.setItem('nom',nom);

        toast.success("Connexion réussie !");
        Onlog(role); // dit à App.js qu'on est connecté
        goToPage('accueil'); // redirige vers l'accueil
        setErrorMessage("");
      }
    } catch (error) {
      if (error.response && error.response.status === 401) {
        setErrorMessage("Email ou mot de passe incorrect");
      } else {
        toast.error("Erreur lors de la connexion :", error);
        setErrorMessage("Erreur lors de la connexion");
      }
    }
  }

  return (
    <div className="connexion-container">
      <h2>Se connecter</h2>
      <form onSubmit={handleConnexion}>
        <div>
          <label>Email :</label>
          <input
            type="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
          />
        </div>
        <div>
          <label>Mot de passe :</label>
          <input
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />
        </div>

        {errorMessage && <p style={{ color: 'red' }}>{errorMessage}</p>}

        <button type="submit">Se connecter</button>
      </form>

      <a
        href="./inscription.js"
        className="ins"
        onClick={(e) => {
          e.preventDefault();
          goToPage('inscrire');
        }}
      >
        S'inscrire..?
      </a>
    </div>
  );
}

export default Connexion;
