import React, { useEffect, useState } from "react";
import axiosInstance from "./gestiontoken/token";
import './css/ListUtilisateurs.css'
import { toast } from "react-toastify";

function ListUtilisateurs() {
  const [utilisateurs, setUtilisateurs] = useState([]);
  const [searchEmail, setSearchEmail] = useState("");
  const [roleSelection, setRoleSelection] = useState({}); // { userId: "ROLE_USER" }

  useEffect(() => {
    fetchUtilisateurs();
  }, []);



  async function fetchUtilisateurs() {
    try {
      const response = await axiosInstance.get("/api/users/get-users");
      setUtilisateurs(response.data);
    } catch (error) {
      console.error("Erreur lors du chargement des utilisateurs :", error);
    }
  }

  function handleRoleChange(userId, role) {
    setRoleSelection((prev) => ({
      ...prev,
      [userId]: role,
    }));
  }

  async function assignRole(userId) {
    const role = roleSelection[userId];
    if (!role) {
      toast.warn("Veuillez sélectionner un rôle.");
      return;
    }

    try {
      await axiosInstance.put(`/api/users/assign-role/${userId}/${role}`);
      toast.success(`Rôle ${role} attribué avec succès !`);
      fetchUtilisateurs(); // Rafraîchir la liste
    } catch (error) {
      console.error("Erreur lors de l'assignation du rôle :", error);
      toast.error("Erreur lors de l'assignation du rôle.");
    }
  }

  const removeRoleFromUser = async (userId, role) => {
        try {  await axiosInstance.put(`/api/users/remove-role/${userId}/${role}`,);
            toast.success(`Rôle ${role} supprimé avec succès !`);
            fetchUtilisateurs();
        } catch (error) {
            console.error("Erreur lors de la suppression du rôle :", error);
            toast.error("Échec de la suppression du rôle.");
        }
        };

        const filteredUtilisateurs = utilisateurs.filter((user) =>
            user.email.toLowerCase().includes(searchEmail.toLowerCase())
        );

  return (
    <div>
      <h2>Gestion des Utilisateurs</h2>

      {/* Recherche par email */}
      <input
        type="text"
        placeholder="Rechercher par email..."
        value={searchEmail}
        onChange={(e) => setSearchEmail(e.target.value)}
      />

      {/* Liste des utilisateurs */}
      {filteredUtilisateurs.map((user) => (
        <div
          key={user.id}
          style={{ border: "1px solid black", margin: "10px", padding: "10px" }}
        >
          <p><strong>Nom :</strong> {user.nom}</p>
          <p><strong>Email :</strong> {user.email}</p>
          <p><strong>Téléphone :</strong> {user.telephone}</p>
          <p><strong>Rôles actuels :</strong> {user.roles.join(", ")}</p>

          {/* Choix du rôle */}
          <select
            value={roleSelection[user.id] || ""}
            onChange={(e) => handleRoleChange(user.id, e.target.value)}
          >
            <option value="">-- Choisir un rôle --</option>
            <option value="USER">USER</option>
            <option value="ORGANISATEUR">ORGANISATEUR</option>
          </select>
          <button onClick={() => assignRole(user.id)}>Attribuer</button>
          <button onClick={() => removeRoleFromUser(user.id, "ORGANISATEUR")}>
            Supprimer rôle ORGANISATEUR
            </button>
            

        </div>
      ))}
    </div>
  );
}

export default ListUtilisateurs;
