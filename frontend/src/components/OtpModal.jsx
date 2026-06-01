import { useState } from "react";
import { api } from "../api.js";

// Étapes : choix civilité -> saisie OTP -> confirmation.
export default function OtpModal({ signataire, offline, onClose, onSigne, onSigneDemo }) {
  const [etape, setEtape] = useState("civilite");
  const [civilite, setCivilite] = useState(signataire.civilite);
  const [code, setCode] = useState("");
  const [codeDemo, setCodeDemo] = useState(null);
  const [erreur, setErreur] = useState(null);
  const [chargement, setChargement] = useState(false);

  const demanderOtp = async () => {
    setChargement(true);
    setErreur(null);
    try {
      const r = await api.demanderOtp(signataire.id, "SMS");
      setCodeDemo(r.codeDemo);
      setEtape("otp");
    } catch {
      // hors-ligne : OTP de démonstration fixe
      setCodeDemo("000000");
      setEtape("otp");
    } finally {
      setChargement(false);
    }
  };

  const valider = async () => {
    setChargement(true);
    setErreur(null);
    if (offline) {
      // En démo, le code attendu est celui affiché.
      if (code === codeDemo) {
        onSigneDemo(signataire.id, civilite);
      } else {
        setErreur("Code incorrect.");
        setChargement(false);
      }
      return;
    }
    try {
      const r = await api.signer(signataire.id, code, civilite);
      if (r.signe) onSigne(r.signataire);
      else setErreur(r.message);
    } catch {
      setErreur("Erreur de communication avec le service de signature.");
    } finally {
      setChargement(false);
    }
  };

  return (
    <div className="overlay" onClick={onClose}>
      <div className="modal" onClick={(e) => e.stopPropagation()}>
        <button className="close" onClick={onClose}>×</button>
        <h2>Signature électronique</h2>
        <p className="modal-sub">
          {signataire.nomComplet} · {signataire.fonction}
        </p>

        {etape === "civilite" && (
          <>
            <label className="lbl">Civilité du signataire</label>
            <div className="civ-choice">
              <button
                className={"civ " + (civilite === "MADAME" ? "civ-on" : "")}
                onClick={() => setCivilite("MADAME")}
              >
                Madame
              </button>
              <button
                className={"civ " + (civilite === "MONSIEUR" ? "civ-on" : "")}
                onClick={() => setCivilite("MONSIEUR")}
              >
                Monsieur
              </button>
            </div>
            <button className="btn-primary" disabled={chargement} onClick={demanderOtp}>
              {chargement ? "Envoi…" : "Recevoir le code par SMS"}
            </button>
          </>
        )}

        {etape === "otp" && (
          <>
            <p className="otp-info">
              Un code à 6 chiffres a été envoyé au <strong>{civilite === "MADAME" ? "Madame" : "Monsieur"} {signataire.nomComplet}</strong>.
            </p>
            {codeDemo && (
              <p className="otp-demo">Code démo : <code>{codeDemo}</code></p>
            )}
            <input
              className="otp-input"
              inputMode="numeric"
              maxLength={6}
              placeholder="------"
              value={code}
              onChange={(e) => setCode(e.target.value.replace(/\D/g, ""))}
              autoFocus
            />
            {erreur && <p className="err">{erreur}</p>}
            <button
              className="btn-primary"
              disabled={chargement || code.length !== 6}
              onClick={valider}
            >
              {chargement ? "Vérification…" : "Valider et signer"}
            </button>
          </>
        )}
      </div>
    </div>
  );
}
