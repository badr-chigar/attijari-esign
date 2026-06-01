package ma.attijari.otp.dto;

import javax.validation.constraints.NotBlank;

public class OtpDtos {

    public static class GenerateRequest {
        @NotBlank
        public String reference;
        /** Canal d'envoi simulé : SMS ou EMAIL. */
        public String canal = "SMS";
    }

    public static class GenerateResponse {
        public String reference;
        public String canal;
        public int expireDansSecondes;
        /** En production le code n'est jamais renvoyé ; exposé ici pour la démo. */
        public String codeDemo;

        public GenerateResponse(String reference, String canal, int expire, String codeDemo) {
            this.reference = reference;
            this.canal = canal;
            this.expireDansSecondes = expire;
            this.codeDemo = codeDemo;
        }
    }

    public static class VerifyRequest {
        @NotBlank
        public String reference;
        @NotBlank
        public String code;
    }

    public static class VerifyResponse {
        public boolean valide;
        public String message;

        public VerifyResponse(boolean valide, String message) {
            this.valide = valide;
            this.message = message;
        }
    }
}
