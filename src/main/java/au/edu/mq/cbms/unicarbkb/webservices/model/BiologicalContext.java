package au.edu.mq.cbms.unicarbkb.webservices.model;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Set;


/**
 * The persistent class for the biological_context database table.
 * 
 */
@Entity
@Table(schema = "core", name="biological_context")
public class BiologicalContext implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer biologicalContextId;
	private Integer contributorId;
	private Timestamp dateEntered;
	private Integer diseasePatternId;
	private Integer perturbationPatternId;
	private Taxonomy taxonomy;
	private TissueTaxonomy tissueTaxonomy;
	private Set<GlycanSequenceToBiologicalContext> glycanSequenceToBiologicalContexts;

    public BiologicalContext() {
    }


	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="biological_context_id", unique=true, nullable=false)
	public Integer getBiologicalContextId() {
		return this.biologicalContextId;
	}

	public void setBiologicalContextId(Integer biologicalContextId) {
		this.biologicalContextId = biologicalContextId;
	}


	@Column(name="contributor_id")
	public Integer getContributorId() {
		return this.contributorId;
	}

	public void setContributorId(Integer contributorId) {
		this.contributorId = contributorId;
	}


	@Column(name="date_entered", nullable=false)
	public Timestamp getDateEntered() {
		return this.dateEntered;
	}

	public void setDateEntered(Timestamp dateEntered) {
		this.dateEntered = dateEntered;
	}


	@Column(name="disease_pattern_id")
	public Integer getDiseasePatternId() {
		return this.diseasePatternId;
	}

	public void setDiseasePatternId(Integer diseasePatternId) {
		this.diseasePatternId = diseasePatternId;
	}


	@Column(name="perturbation_pattern_id")
	public Integer getPerturbationPatternId() {
		return this.perturbationPatternId;
	}

	public void setPerturbationPatternId(Integer perturbationPatternId) {
		this.perturbationPatternId = perturbationPatternId;
	}


	//bi-directional many-to-one association to Taxonomy
    @ManyToOne
	@JoinColumn(name="taxonomy_id", nullable=false)
	public Taxonomy getTaxonomy() {
		return this.taxonomy;
	}

	public void setTaxonomy(Taxonomy taxonomy) {
		this.taxonomy = taxonomy;
	}
	

	//bi-directional many-to-one association to TissueTaxonomy
    @ManyToOne
	@JoinColumn(name="tissue_taxonomy_id", nullable=false)
	public TissueTaxonomy getTissueTaxonomy() {
		return this.tissueTaxonomy;
	}

	public void setTissueTaxonomy(TissueTaxonomy tissueTaxonomy) {
		this.tissueTaxonomy = tissueTaxonomy;
	}
	

	//bi-directional many-to-one association to GlycanSequenceToBiologicalContext
	@OneToMany(mappedBy="biologicalContext")
	public Set<GlycanSequenceToBiologicalContext> getGlycanSequenceToBiologicalContexts() {
		return this.glycanSequenceToBiologicalContexts;
	}

	public void setGlycanSequenceToBiologicalContexts(Set<GlycanSequenceToBiologicalContext> glycanSequenceToBiologicalContexts) {
		this.glycanSequenceToBiologicalContexts = glycanSequenceToBiologicalContexts;
	}
	
}