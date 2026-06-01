package ma.attijari.signature.repository;

import ma.attijari.signature.model.Signataire;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SignataireRepository extends JpaRepository<Signataire, Long> {
    List<Signataire> findAllByOrderByOrdreAsc();
}
