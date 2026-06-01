package ma.attijari.signature.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * Client HTTP vers le micro-service OTP (communication inter-services).
 *
 * Le signature-service ne génère ni ne vérifie lui-même les codes :
 * il délègue au otp-service via REST.
 */
@Component
public class OtpClient {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${otp.service.url:http://localhost:8081}")
    private String otpServiceUrl;

    /** Demande la génération d'un OTP pour un signataire. */
    @SuppressWarnings("unchecked")
    public String demanderCode(String reference, String canal) {
        Map<String, String> body = new HashMap<>();
        body.put("reference", reference);
        body.put("canal", canal);
        Map<String, Object> resp = restTemplate.postForObject(
                otpServiceUrl + "/api/otp/generate", body, Map.class);
        return resp == null ? null : (String) resp.get("codeDemo");
    }

    /** Vérifie un code auprès du otp-service. */
    @SuppressWarnings("unchecked")
    public boolean verifierCode(String reference, String code) {
        Map<String, String> body = new HashMap<>();
        body.put("reference", reference);
        body.put("code", code);
        Map<String, Object> resp = restTemplate.postForObject(
                otpServiceUrl + "/api/otp/verify", body, Map.class);
        return resp != null && Boolean.TRUE.equals(resp.get("valide"));
    }
}
