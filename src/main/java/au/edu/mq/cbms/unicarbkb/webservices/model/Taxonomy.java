package au.edu.mq.cbms.unicarbkb.webservices.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Set;


/**
 * The persistent class for the taxonomy database table.
 * 
 */
@Entity
@Table(schema = "core", name="taxonomy")
public class Taxonomy implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer taxonomyId;
	private Integer ncbiId;
	private String rank;
	private String taxon;
	private Set<BiologicalContext> biologicalContexts;
	private Taxonomy taxonomy;
	private Set<Taxonomy> taxonomies;

    public Taxonomy() {
    }


	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="taxonomy_id", unique=true, nullable=false)
	public Integer getTaxonomyId() {
		return this.taxonomyId;
	}

	public void setTaxonomyId(Integer taxonomyId) {
		this.taxonomyId = taxonomyId;
	}


	@Column(name="ncbi_id", nullable=false)
	public Integer getNcbiId() {
		return this.ncbiId;
	}

	public void setNcbiId(Integer ncbiId) {
		this.ncbiId = ncbiId;
	}


	@Column(nullable=false, length=16)
	public String getRank() {
		return this.rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}


	@Column(nullable=false, length=128)
	public String getTaxon() {
		return this.taxon;
	}

	public void setTaxon(String taxon) {
		this.taxon = taxon;
	}


	//bi-directional many-to-one association to BiologicalContext
	@OneToMany(mappedBy="taxonomy")
	public Set<BiologicalContext> getBiologicalContexts() {
		return this.biologicalContexts;
	}

	public void setBiologicalContexts(Set<BiologicalContext> biologicalContexts) {
		this.biologicalContexts = biologicalContexts;
	}
	

//	//bi-directional many-to-one association to Taxonomy
//    @ManyToOne
//	@JoinColumn(name="parent_taxonomy_id", nullable=false)
//	public Taxonomy getTaxonomy() {
//		return this.taxonomy;
//	}
//
//	public void setTaxonomy(Taxonomy taxonomy) {
//		this.taxonomy = taxonomy;
//	}
//	
//
//	//bi-directional many-to-one association to Taxonomy
//	@OneToMany(mappedBy="taxonomy")
//	public Set<Taxonomy> getTaxonomies() {
//		return this.taxonomies;
//	}
//
//	public void setTaxonomies(Set<Taxonomy> taxonomies) {
//		this.taxonomies = taxonomies;
//	}
	
}