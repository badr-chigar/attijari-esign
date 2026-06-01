package ma.attijari.otp.controller;

import ma.attijari.otp.dto.OtpDtos.*;
import ma.attijari.otp.service.OtpService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/otp")
@CrossOrigin(origins = "*")
public class OtpController {

    private final OtpService otpService;

    public OtpController(OtpService otpService) {
        this.otpService = otpService;
    }

    /** Génère un OTP et simule son envoi par SMS/e-mail. */
    @PostMapping("/generate")
    public GenerateResponse generate(@Valid @RequestBody GenerateRequest req) {
        String code = otpService.genererCode(req.reference);
        // En production : appel passerelle SMS / e-mail ici.
        return new GenerateResponse(req.reference, req.canal, 120, code);
    }

    /** Vérifie un OTP soumis par le signataire. */
    @PostMapping("/verify")
    public VerifyResponse verify(@Valid @RequestBody VerifyRequest req) {
        OtpService.VerificationResult r = otpService.verifier(req.reference, req.code);
        return new VerifyResponse(r.valide, r.message);
    }

    @GetMapping("/health")
    public String health() {
        return "otp-service up";
    }
}
