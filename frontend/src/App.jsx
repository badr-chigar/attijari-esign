import { useEffect, useState } from "react";
import { api } from "./api.js";
import SignataireTable from "./components/SignataireTable.jsx";
import OtpModal from "./components/OtpModal.jsx";

// Jeu de démonstration utilisé si le backend n'est pas joignable.
const DEMO = [
  { id: 1, civilite: "MONSIEUR", civiliteLibelle: "Monsieur", nomComplet: "Karim Benjelloun", fonction: "Directeur Général", email: "k.benjelloun@attijari.ma", statut: "SIGNE" },
  { id: 2, civilite: "MADAME", civiliteLibelle: "Madame", nomComplet: "Salma El Fassi", fonction: "Directrice Financière", email: "s.elfassi@attijari.ma", statut: "EN_ATTENTE" },
  { id: 3, civilite: "MONSIEUR", civiliteLibelle: "Monsieur", nomComplet: "Younes Tazi", fonction: "Responsable Juridique", email: "y.tazi@attijari.ma", statut: "EN_ATTENTE" },
  { id: 4, civilite: "MADAME", civiliteLibelle: "Madame", nomComplet: "Imane Bennani", fonction: "Secrétaire Générale", email: "i.bennani@attijari.ma", statut: "EN_ATTENTE" },
  { id: 5, civilite: "MONSIEUR", civiliteLibelle: "Monsieur", nomComplet: "Réda Alaoui", fonction: "Membre du Directoire", email: "r.alaoui@attijari.ma", statut: "EN_ATTENTE" },
];

export default function App() {
  const [signataires, setSignataires] = useState([]);
  const [offline, setOffline] = useState(false);
  const [actif, setActif] = useState(null); // signataire en cours de signature

  const charger = () => {
    api
      .listeSignataires()
      .then((data) => { setSignataires(data); setOffline(false); })
      .catch(() => { setSignataires(DEMO); setOffline(true); });
  };

  useEffect(charger, []);

  const onSigne = (signataireMaj) => {
    setSignataires((prev) =>
      prev.map((s) => (s.id === signataireMaj.id ? signataireMaj : s))
    );
    setActif(null);
  };

  // En mode hors-ligne, on simule la signature localement.
  const onSigneDemo = (id, civilite) => {
    setSignataires((prev) =>
      prev.map((s) =>
        s.id === id
          ? { ...s, statut: "SIGNE", civilite, civiliteLibelle: civilite === "MADAME" ? "Madame" : "Monsieur" }
          : s
      )
    );
    setActif(null);
  };

  const signes = signataires.filter((s) => s.statut === "SIGNE").length;
  const total = signataires.length;
  const progression = total ? Math.round((signes / total) * 100) : 0;

  return (
    <div className="page">
      <header className="topbar">
        <div className="brand">
          <span className="brand-mark">A</span>
          <div>
            <strong>Attijariwafa Bank</strong>
            <span className="brand-sub">Parapheur électronique</span>
          </div>
        </div>
        <div className="user">Espace gestionnaire</div>
      </header>

      <main className="container">
        <div className="doc-head">
          <div>
            <h1>Convention de partenariat — 2026</h1>
            <p className="ref">Réf. DOC-2026-0471 · 5 signataires requis</p>
          </div>
          <div className="progress-box">
            <div className="progress-label">
              {signes}/{total} signés
            </div>
            <div className="progress-bar">
              <div className="progress-fill" style={{ width: progression + "%" }} />
            </div>
          </div>
        </div>

        {offline && (
          <div className="banner">
            Mode démonstration (backend non connecté) — la signature est simulée localement.
          </div>
        )}

        <SignataireTable
          signataires={signataires}
          onSelect={(s) => s.statut !== "SIGNE" && setActif(s)}
        />
      </main>

      {actif && (
        <OtpModal
          signataire={actif}
          offline={offline}
          onClose={() => setActif(null)}
          onSigne={onSigne}
          onSigneDemo={onSigneDemo}
        />
      )}
    </div>
  );
}
