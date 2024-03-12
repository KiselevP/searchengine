package searchengine.models;

import lombok.Builder;
import lombok.Data;

import jakarta.persistence.*;

@Entity
@Data
@Builder
public class IndexSearch
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name = "page_id", unique = true, nullable = false)
    private Page pageId;
    @OneToOne
    @JoinColumn(name = "lemma_id", unique = true, nullable = false)
    private Lemma lemmaId;
    @Column(nullable = false)
    private Float lemmaRank;
}
