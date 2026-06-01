package ma.attijari.signature.dto;

import ma.attijari.signature.model.Signataire;

import javax.validation.constraints.NotBlank;

public class Dtos {

    /** Vue d'un signataire renvoyée au front (table du parapheur). */
    public static class SignataireDto {
        public Long id;
        public String civilite;       // MADAME / MONSIEUR
        public String civiliteLibelle; // Madame / Monsieur
        public String nomComplet;
        public String fonction;
        public String email;
        public String statut;          // EN_ATTENTE / SIGNE
        public String dateSignature;

        public static SignataireDto from(Signataire s) {
            SignataireDto d = new SignataireDto();
            d.id = s.getId();
            d.civilite = s.getCivilite().name();
            d.civiliteLibelle = s.getCivilite().getLibelle();
            d.nomComplet = s.getPrenom() + " " + s.getNom();
            d.fonction = s.getFonction();
            d.email = s.getEmail();
            d.statut = s.getStatut().name();
            d.dateSignature = s.getDateSignature() == null ? null : s.getDateSignature().toString();
            return d;
        }
    }

    public static class DemandeOtpResponse {
        public Long signataireId;
        public String canal;
        public String message;
        public String codeDemo; // démo uniquement

        public DemandeOtpResponse(Long id, String canal, String message, String codeDemo) {
            this.signataireId = id;
            this.canal = canal;
            this.message = message;
            this.codeDemo = codeDemo;
        }
    }

    public static class SignatureRequest {
        @NotBlank
        public String code;
        /** Civilité confirmée au moment de la signature (Madame/Monsieur). */
        public String civilite;
    }

    public static class SignatureResponse {
        public boolean signe;
        public String message;
        public SignataireDto signataire;

        public SignatureResponse(boolean signe, String message, SignataireDto signataire) {
            this.signe = signe;
            this.message = message;
            this.signataire = signataire;
        }
    }
}
