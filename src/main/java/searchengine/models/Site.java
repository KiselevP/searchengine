package searchengine.models;

import lombok.Data;

import javax.persistence.*;
import java.time.*;

@Entity
@Data
public class Site
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('INDEXING', 'INDEXED', 'FAILED') NOT NULL")
    private IndexingStatus indexingStatus;
    @Column(nullable = false)
    private LocalDateTime statusTime;
    @Column(columnDefinition = "TEXT")
    private String lastError;
    @Column(nullable = false)
    private String url;
    @Column(nullable = false, unique = true)
    private String name;
}
