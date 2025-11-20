import React, { useState, useEffect} from "react";
import Accueil from './composants/Accueil';
import Connexion from './composants/Connexion';
import Inscription from './composants/Inscription';
import Event from './composants/Event';
import Reserver from './composants/Reservation';
import MonCompte from './composants/MonCompte';
import Confirmation from "./composants/Confirmation";
import EvenementsAVenir from './composants/EvenementAVenir';
import ListUtilisateurs from "./composants/ListUtilisateurs";
import { ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';




import './composants/css/global.css';


function App() {
  const [page, setPage] = useState("accueil");
  const [userRole, setUserRole] = useState(null);  // "UTILISATEUR" ou "ORGANISATEUR"
  const [userId, setUserId] = useState(null);
  const [userNom, setUserNom] = useState("");
  const [idEventDetail, setIdEventDetail] = useState(null);
  const [pageData, setPageData] = useState({});


  // Lors du chargement : vérifier si l'utilisateur est déjà connecté
  useEffect(() => {
  const token = localStorage.getItem("token");
  const role = localStorage.getItem("role");
  const id = localStorage.getItem("userId");
  const nom = localStorage.getItem("nom"); // ← CORRIGÉ ICI

  if (token && role && id && nom) {
    setUserRole(role);
    setUserId(id);
    setUserNom(nom); // ← Utilise le nom sauvegardé
  }
}, []);

  function handleLogin(role) {
  const token = localStorage.getItem("token");
  const payload = JSON.parse(atob(token.split('.')[1]));
  setUserId(payload.id);
  setUserRole(role);
  setUserNom(payload.nom || "Utilisateur"); // ← CORRIGÉ ICI
}


  function handleLogout() {
    localStorage.clear();
    setUserRole(null);
    setUserId(null);
    setUserNom("");
    setPage("accueil");
  }

  function goToPage(p, extra = null) {
  if (p === "reserver" && extra) {
    setIdEventDetail(extra);
  }
  if (extra) {
    setPageData(extra); // 👈 Important
  } else {
    setPageData({});
  }
  setPage(p);
}


  return (
    <div className="app-container">
      {/* Barre de navigation */}
      <nav className="navbar">
  <button onClick={() => goToPage("accueil")}>Accueil</button>
  <button onClick={() => goToPage("evenementsavenir")}>Événements à venir</button>
  <button onClick={() => goToPage("moncompte")}>Mon Compte</button>

  {userRole === "ORGANISATEUR" && (
    <button onClick={() => goToPage("event")}>Gestion des événements</button>
  )}
  {userRole === "ADMIN" && (
  <button onClick={() => goToPage("gestion-utilisateurs")}>Gestion Utilisateurs</button>
)}


  {userId ? (
    <>
      <span style={{ marginLeft: "20px" }}>Bonjour, {userNom}</span>
      <button onClick={handleLogout}>Déconnexion</button>
    </>
  ) : (
    <button onClick={() => goToPage("connexion")}>Connexion</button>
  )}
</nav>


      {/* Zone principale */}
      <main>
        {page === "accueil" && <Accueil goToPage={goToPage} />}
        {page === "connexion" && <Connexion Onlog={handleLogin} goToPage={goToPage} />}
        {page === "inscrire" && <Inscription goToPage={goToPage} />}
        {page === "event" && userRole === "ORGANISATEUR" && (
          <Event organisateurId={userId} />
        )}
        {page === "reserver" && idEventDetail && (
          <Reserver goToPage={goToPage} idEvent={idEventDetail} userId={userId} />
        )}
        {page === "moncompte" && userId ? (
          <MonCompte />
        ) : page === "moncompte" ? (
          <Connexion Onlog={handleLogin} goToPage={goToPage} />
        ) : null}
        {page === 'confirmation' && <Confirmation event={pageData?.event} goToPage={goToPage} />}
        {page === "evenementsavenir" && <EvenementsAVenir goToPage={goToPage} />}
        {page === "gestion-utilisateurs" && userRole === "ADMIN" && <ListUtilisateurs />}
        <ToastContainer position="top-center" autoClose={3000} />




      </main>
    </div>
  );
}

export default App;