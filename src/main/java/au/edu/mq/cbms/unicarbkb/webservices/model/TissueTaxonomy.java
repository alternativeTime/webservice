package au.edu.mq.cbms.unicarbkb.webservices.model;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Set;


/**
 * The persistent class for the tissue_taxonomy database table.
 * 
 */
@Entity
@Table(schema = "core", name="tissue_taxonomy")
public class TissueTaxonomy implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer tissueTaxonomyId;
	private Timestamp dateLastModified;
	private String description;
	private String meshId;
	private String tissueTaxon;
	private Set<BiologicalContext> biologicalContexts;
	private TissueTaxonomy tissueTaxonomy;
	private Set<TissueTaxonomy> tissueTaxonomies;

    public TissueTaxonomy() {
    }


	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="tissue_taxonomy_id", unique=true, nullable=false)
	public Integer getTissueTaxonomyId() {
		return this.tissueTaxonomyId;
	}

	public void setTissueTaxonomyId(Integer tissueTaxonomyId) {
		this.tissueTaxonomyId = tissueTaxonomyId;
	}


	@Column(name="date_last_modified", nullable=false)
	public Timestamp getDateLastModified() {
		return this.dateLastModified;
	}

	public void setDateLastModified(Timestamp dateLastModified) {
		this.dateLastModified = dateLastModified;
	}


	@Column(length=2147483647)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


	@Column(name="mesh_id", nullable=false, length=64)
	public String getMeshId() {
		return this.meshId;
	}

	public void setMeshId(String meshId) {
		this.meshId = meshId;
	}


	@Column(name="tissue_taxon", nullable=false, length=128)
	public String getTissueTaxon() {
		return this.tissueTaxon;
	}

	public void setTissueTaxon(String tissueTaxon) {
		this.tissueTaxon = tissueTaxon;
	}


	//bi-directional many-to-one association to BiologicalContext
	@OneToMany(mappedBy="tissueTaxonomy")
	public Set<BiologicalContext> getBiologicalContexts() {
		return this.biologicalContexts;
	}

	public void setBiologicalContexts(Set<BiologicalContext> biologicalContexts) {
		this.biologicalContexts = biologicalContexts;
	}
	

	//bi-directional many-to-one association to TissueTaxonomy
    @ManyToOne
	@JoinColumn(name="parent_tissue_taxonomy_id", nullable=false)
	public TissueTaxonomy getTissueTaxonomy() {
		return this.tissueTaxonomy;
	}

	public void setTissueTaxonomy(TissueTaxonomy tissueTaxonomy) {
		this.tissueTaxonomy = tissueTaxonomy;
	}
	

	//bi-directional many-to-one association to TissueTaxonomy
	@OneToMany(mappedBy="tissueTaxonomy")
	public Set<TissueTaxonomy> getTissueTaxonomies() {
		return this.tissueTaxonomies;
	}

	public void setTissueTaxonomies(Set<TissueTaxonomy> tissueTaxonomies) {
		this.tissueTaxonomies = tissueTaxonomies;
	}
	
}