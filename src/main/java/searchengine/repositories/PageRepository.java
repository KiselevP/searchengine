package searchengine.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import searchengine.models.Page;

import java.util.Optional;

public interface PageRepository extends JpaRepository<Page, Long>, ModelRepository<Page> {

    Optional<Page> findPageByPath(String path);

    void deleteBySiteId(Long site_id);

}
