package searchengine.models.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import searchengine.models.Page;
import searchengine.models.Site;

public interface PageRepository extends JpaRepository<Page, Integer>
{
    Page findPageByPath(String path);
    Page findPageById(int id);

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
            value = "TRUNCATE `page`",
            nativeQuery = true
    )
    void deleteAllElements();
}
