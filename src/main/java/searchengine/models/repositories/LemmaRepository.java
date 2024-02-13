package searchengine.models.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import searchengine.models.Lemma;

public interface LemmaRepository extends JpaRepository<Lemma, Integer> {
}
