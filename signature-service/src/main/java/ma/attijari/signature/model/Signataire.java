package ma.attijari.signature.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "signataire")
public class Signataire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** MADAME ou MONSIEUR. */
    @Enumerated(EnumType.STRING)
    private Civilite civilite;

    private String nom;
    private String prenom;
    private String fonction;
    private String email;
    private String telephone;

    /** EN_ATTENTE (point rouge) ou SIGNE (point vert). */
    @Enumerated(EnumType.STRING)
    private StatutSignature statut = StatutSignature.EN_ATTENTE;

    private LocalDateTime dateSignature;

    /** Ordre dans le parapheur. */
    private int ordre;

    public Signataire() {}

    public Signataire(Civilite civilite, String nom, String prenom, String fonction,
                      String email, String telephone, int ordre) {
        this.civilite = civilite;
        this.nom = nom;
        this.prenom = prenom;
        this.fonction = fonction;
        this.email = email;
        this.telephone = telephone;
        this.ordre = ordre;
    }

    public void marquerSigne() {
        this.statut = StatutSignature.SIGNE;
        this.dateSignature = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public Civilite getCivilite() { return civilite; }
    public void setCivilite(Civilite civilite) { this.civilite = civilite; }
    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }
    public String getFonction() { return fonction; }
    public String getEmail() { return email; }
    public String getTelephone() { return telephone; }
    public StatutSignature getStatut() { return statut; }
    public LocalDateTime getDateSignature() { return dateSignature; }
    public int getOrdre() { return ordre; }
}
