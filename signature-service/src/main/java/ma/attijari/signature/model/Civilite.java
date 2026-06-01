package ma.attijari.signature.model;

public enum Civilite {
    MADAME("Madame"),
    MONSIEUR("Monsieur");

    private final String libelle;

    Civilite(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}
