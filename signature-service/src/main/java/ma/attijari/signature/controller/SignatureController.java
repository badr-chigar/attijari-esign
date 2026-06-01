package ma.attijari.signature.controller;

import ma.attijari.signature.dto.Dtos.*;
import ma.attijari.signature.model.Civilite;
import ma.attijari.signature.model.Signataire;
import ma.attijari.signature.repository.SignataireRepository;
import ma.attijari.signature.service.OtpClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/signataires")
@CrossOrigin(origins = "*")
public class SignatureController {

    private final SignataireRepository repo;
    private final OtpClient otpClient;

    public SignatureController(SignataireRepository repo, OtpClient otpClient) {
        this.repo = repo;
        this.otpClient = otpClient;
    }

    /** Liste des signataires du parapheur (ordonnés). */
    @GetMapping
    public List<SignataireDto> liste() {
        return repo.findAllByOrderByOrdreAsc()
                .stream().map(SignataireDto::from).collect(Collectors.toList());
    }

    /** Étape 1 : le signataire clique sur sa ligne -> on demande un OTP. */
    @PostMapping("/{id}/demander-otp")
    public ResponseEntity<DemandeOtpResponse> demanderOtp(@PathVariable Long id,
                                                          @RequestParam(defaultValue = "SMS") String canal) {
        Signataire s = repo.findById(id).orElse(null);
        if (s == null) return ResponseEntity.notFound().build();
        String codeDemo = otpClient.demanderCode("SIGN-" + id, canal);
        String dest = "SMS".equalsIgnoreCase(canal) ? s.getTelephone() : s.getEmail();
        return ResponseEntity.ok(new DemandeOtpResponse(
                id, canal, "Code envoyé au signataire (" + dest + ").", codeDemo));
    }

    /** Étape 2 : vérification du code + confirmation civilité -> signature. */
    @PostMapping("/{id}/signer")
    public ResponseEntity<SignatureResponse> signer(@PathVariable Long id,
                                                    @Valid @RequestBody SignatureRequest req) {
        Signataire s = repo.findById(id).orElse(null);
        if (s == null) return ResponseEntity.notFound().build();

        boolean ok = otpClient.verifierCode("SIGN-" + id, req.code);
        if (!ok) {
            return ResponseEntity.ok(new SignatureResponse(false,
                    "Code OTP invalide ou expiré.", SignataireDto.from(s)));
        }
        if (req.civilite != null && !req.civilite.isBlank()) {
            s.setCivilite(Civilite.valueOf(req.civilite));
        }
        s.marquerSigne();
        repo.save(s);
        return ResponseEntity.ok(new SignatureResponse(true,
                "Document signé électroniquement.", SignataireDto.from(s)));
    }

    @GetMapping("/health")
    public String health() {
        return "signature-service up";
    }
}
