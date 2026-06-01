// Couche d'accès au signature-service (qui délègue l'OTP au otp-service).
const BASE = "/api/signataires";

async function json(res) {
  if (!res.ok) throw new Error("Erreur " + res.status);
  return res.json();
}

export const api = {
  listeSignataires: () => fetch(BASE).then(json),

  demanderOtp: (id, canal = "SMS") =>
    fetch(`${BASE}/${id}/demander-otp?canal=${canal}`, { method: "POST" }).then(json),

  signer: (id, code, civilite) =>
    fetch(`${BASE}/${id}/signer`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ code, civilite }),
    }).then(json),
};
