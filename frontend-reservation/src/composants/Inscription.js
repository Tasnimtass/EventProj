import axios from "axios";
import React, { useState } from "react";
import './css/Inscription.css'
import { toast } from "react-toastify";

function Inscrire({ goToPage }) {
    const [formData, setFormData] = useState({
        nom: "",
        email: "",
        mdp: "",
        telephone: "",
    });
    const [errorMessage, setErrorMessage] = useState("");

    // Fonction de soumission du formulaire
    async function handleSubmit(e) {
         e.preventDefault();
        try {
            const response = await axios.post("http://localhost:1111/api/users/create-user", formData);
            if (response.status === 201) {
                toast.success("Utilisateur créé avec succès !");
                goToPage('connexion');
            }
        } catch (error) {
            setErrorMessage("Erreur lors de l'inscription");
        }
    }

    return (
        <div>
            {/* Formulaire d'inscription */}
            <form onSubmit={handleSubmit}>
                <div>
                    <label>Nom:</label>
                    <input
                        type="text"
                        name="nom"
                        value={formData.nom}
                        onChange={(e) => setFormData({ ...formData, nom: e.target.value })}
                        required
                    />
                </div>
                <div>
                    <label>Email:</label>
                    <input
                        type="email"
                        name="email"
                        value={formData.email}
                        onChange={(e) => setFormData({ ...formData, email: e.target.value })}
                        required
                    />
                </div>
                <div>
                    <label>Mot de passe:</label>
                    <input
                        type="password"
                        name="mdp"
                        value={formData.mdp}
                        onChange={(e) => setFormData({ ...formData, mdp: e.target.value })}
                        required
                    />
                </div>
                <div>
                    <label>Téléphone:</label>
                    <input
                        type="tel"
                        name="telephone"
                        value={formData.telephone}
                        onChange={(e) => setFormData({ ...formData, telephone: e.target.value })}
                        required
                    />
                </div>
                <div>
                    <button type="submit">S'inscrire</button>
                </div>
            </form>

            {/* Affichage du message d'erreur s'il y en a */}
            {errorMessage && <p style={{ color: "red" }}>{errorMessage}</p>}
        </div>
    );
}

export default Inscrire;
