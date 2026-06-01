package ma.attijari.otp.service;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Génère, stocke (en mémoire) et vérifie les codes OTP à usage unique.
 *
 * Un code est associé à une référence (l'identifiant du signataire),
 * expire au bout de {@link #TTL_SECONDS} secondes, et est invalidé
 * après une vérification réussie ou après trop de tentatives.
 */
@Service
public class OtpService {

    private static final int TTL_SECONDS = 120;
    private static final int MAX_TENTATIVES = 3;
    private static final SecureRandom RANDOM = new SecureRandom();

    private final ConcurrentHashMap<String, OtpEntry> store = new ConcurrentHashMap<>();

    /** Génère un code à 6 chiffres pour la référence donnée et le renvoie. */
    public String genererCode(String reference) {
        String code = String.format("%06d", RANDOM.nextInt(1_000_000));
        store.put(reference, new OtpEntry(code, Instant.now().plusSeconds(TTL_SECONDS)));
        return code;
    }

    /** Vérifie le code soumis. Renvoie un résultat décrivant l'issue. */
    public VerificationResult verifier(String reference, String codeSoumis) {
        OtpEntry entry = store.get(reference);
        if (entry == null) {
            return new VerificationResult(false, "Aucun code en attente pour ce signataire.");
        }
        if (Instant.now().isAfter(entry.expiration)) {
            store.remove(reference);
            return new VerificationResult(false, "Le code a expiré, veuillez en demander un nouveau.");
        }
        if (entry.tentatives >= MAX_TENTATIVES) {
            store.remove(reference);
            return new VerificationResult(false, "Nombre maximum de tentatives atteint.");
        }
        entry.tentatives++;
        if (entry.code.equals(codeSoumis)) {
            store.remove(reference);
            return new VerificationResult(true, "Code vérifié avec succès.");
        }
        int restantes = MAX_TENTATIVES - entry.tentatives;
        return new VerificationResult(false, "Code incorrect. Tentatives restantes : " + restantes);
    }

    private static class OtpEntry {
        final String code;
        final Instant expiration;
        int tentatives = 0;

        OtpEntry(String code, Instant expiration) {
            this.code = code;
            this.expiration = expiration;
        }
    }

    public static class VerificationResult {
        public final boolean valide;
        public final String message;

        public VerificationResult(boolean valide, String message) {
            this.valide = valide;
            this.message = message;
        }
    }
}
