package searchengine.models.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import searchengine.models.Lemma;

public interface LemmaRepository extends JpaRepository<Lemma, Integer> {
    @Modifying
    @Query(
            value = "SET FOREIGN_KEY_CHECKS = 0;",
            nativeQuery = true
    )
    void setForeignKeyOnZero();

    @Modifying
    @Query(
            value = "SET FOREIGN_KEY_CHECKS = 1",
            nativeQuery = true
    )
    void setForeignKeyOnOne();

    /** Очистка таблицы со сбросом счётчика id */
    @Modifying
    @Query(
            value = "TRUNCATE `lemma`",
            nativeQuery = true
    )
    void deleteAllElements();
}
