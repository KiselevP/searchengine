package searchengine.models;

import javax.persistence.*;

@Entity
public class IndexSearch
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @OneToOne
    @JoinColumn(name = "page_id", unique = true, nullable = false)
    private Page pageId;
    @OneToOne
    @JoinColumn(name = "lemma_id", unique = true, nullable = false)
    private Lemma lemmaId;
    @Column(nullable = false)
    private Float lemmaRank;
}
