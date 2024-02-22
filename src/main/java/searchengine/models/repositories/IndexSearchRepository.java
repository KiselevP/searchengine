package searchengine.models.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import searchengine.models.IndexSearch;

public interface IndexSearchRepository extends JpaRepository<IndexSearch, Integer> {
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

    @Modifying
    @Query(
            value = "TRUNCATE `index_search`",
            nativeQuery = true
    )
    void deleteAllElements();
}
