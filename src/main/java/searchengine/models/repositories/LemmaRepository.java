package searchengine.models.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import searchengine.models.Lemma;

public interface LemmaRepository extends JpaRepository<Lemma, Integer>
{
    @Modifying
    @Transactional
    @Query(value = "TRUNCATE TABLE lemma", nativeQuery = true)
    void truncateTable();
}
