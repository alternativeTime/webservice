package au.edu.mq.cbms.unicarbkb.webservices.model;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the glycan_sequence_to_biological_context database table.
 * 
 */
@Entity
@Table(schema = "core", name="glycan_sequence_to_biological_context")
public class GlycanSequenceToBiologicalContext implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer glycanSequenceToBiologicalContextId;
	private Integer contributorId;
	private Timestamp dateEntered;
	private BiologicalContext biologicalContext;
	private GlycanSequence glycanSequence;

    public GlycanSequenceToBiologicalContext() {
    }


	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="glycan_sequence_to_biological_context_id", unique=true, nullable=false)
	public Integer getGlycanSequenceToBiologicalContextId() {
		return this.glycanSequenceToBiologicalContextId;
	}

	public void setGlycanSequenceToBiologicalContextId(Integer glycanSequenceToBiologicalContextId) {
		this.glycanSequenceToBiologicalContextId = glycanSequenceToBiologicalContextId;
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


	//bi-directional many-to-one association to BiologicalContext
    @ManyToOne
	@JoinColumn(name="biological_context_id", nullable=false)
	public BiologicalContext getBiologicalContext() {
		return this.biologicalContext;
	}

	public void setBiologicalContext(BiologicalContext biologicalContext) {
		this.biologicalContext = biologicalContext;
	}
	

	//bi-directional many-to-one association to GlycanSequence
    @ManyToOne
	@JoinColumn(name="glycan_sequence_id", nullable=false)
	public GlycanSequence getGlycanSequence() {
		return this.glycanSequence;
	}

	public void setGlycanSequence(GlycanSequence glycanSequence) {
		this.glycanSequence = glycanSequence;
	}
	
}