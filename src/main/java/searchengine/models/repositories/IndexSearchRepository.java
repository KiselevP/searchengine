package searchengine.models.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import searchengine.models.IndexSearch;

public interface IndexSearchRepository extends JpaRepository<IndexSearch, Integer>
{
    @Modifying
    @Transactional
    @Query(value = "TRUNCATE TABLE index_search", nativeQuery = true)
    void truncateTable();
}
