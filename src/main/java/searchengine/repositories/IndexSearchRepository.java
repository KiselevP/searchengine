package searchengine.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import searchengine.models.IndexSearch;

public interface IndexSearchRepository extends JpaRepository<IndexSearch, Integer> {
}
