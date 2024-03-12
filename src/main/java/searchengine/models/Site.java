package searchengine.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.*;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class Site
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "ENUM('INDEXING', 'INDEXED', 'FAILED')")
    private IndexingStatus indexingStatus;

    @Column(nullable = false)
    private LocalDateTime statusTime;

    @Column(columnDefinition = "TEXT")
    private String lastError;

    @Column(nullable = false)
    private String url;

    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany
    private List<Page> pages;

    @OneToMany
    private List<Lemma> lemmas;
}
