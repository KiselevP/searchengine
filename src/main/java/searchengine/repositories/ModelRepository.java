package searchengine.repositories;

import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface ModelRepository<T> {
    Optional<T> getById(int id);

    List<T> findAll(Sort sort);

    T save(T model);

    void deleteById(int id);
}
