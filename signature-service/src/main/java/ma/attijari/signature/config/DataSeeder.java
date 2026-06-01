package ma.attijari.signature.config;

import ma.attijari.signature.model.Civilite;
import ma.attijari.signature.model.Signataire;
import ma.attijari.signature.repository.SignataireRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSeeder implements CommandLineRunner {

    private final SignataireRepository repo;

    public DataSeeder(SignataireRepository repo) {
        this.repo = repo;
    }

    @Override
    public void run(String... args) {
        if (repo.count() > 0) return;
        repo.save(new Signataire(Civilite.MONSIEUR, "Benjelloun", "Karim",
                "Directeur Général", "k.benjelloun@attijari.ma", "+212661000001", 1));
        repo.save(new Signataire(Civilite.MADAME, "El Fassi", "Salma",
                "Directrice Financière", "s.elfassi@attijari.ma", "+212661000002", 2));
        repo.save(new Signataire(Civilite.MONSIEUR, "Tazi", "Younes",
                "Responsable Juridique", "y.tazi@attijari.ma", "+212661000003", 3));
        repo.save(new Signataire(Civilite.MADAME, "Bennani", "Imane",
                "Secrétaire Générale", "i.bennani@attijari.ma", "+212661000004", 4));
        repo.save(new Signataire(Civilite.MONSIEUR, "Alaoui", "Réda",
                "Membre du Directoire", "r.alaoui@attijari.ma", "+212661000005", 5));
    }
}
