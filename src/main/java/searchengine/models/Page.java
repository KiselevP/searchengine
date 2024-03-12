package searchengine.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class Page {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "site_id", nullable = false)
    private Site site;

    @Column(name = "path", columnDefinition = "TEXT(255) NOT NULL, FULLTEXT KEY PATH_KEY (path)")
    private String path;

    @Column(nullable = false)
    private Integer code;

    @Column(nullable = false, columnDefinition = "MEDIUMTEXT")
    private String content;

}
