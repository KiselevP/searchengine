package searchengine.models;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

@Entity
@Data
@Builder
public class Lemma
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "site_id", nullable = false)
    private Site siteId;

    @Column(nullable = false)
    private String lemma;

    @Column(nullable = false)
    private Integer frequency;
}
