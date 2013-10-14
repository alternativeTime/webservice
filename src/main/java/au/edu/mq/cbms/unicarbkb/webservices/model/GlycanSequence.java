package au.edu.mq.cbms.unicarbkb.webservices.model;

import java.io.Serializable;
import javax.persistence.*;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.sql.Timestamp;
import java.math.BigDecimal;
import java.util.Set;


/**
 * The persistent class for the glycan_sequence database table.
 * 
 */
@Entity
@Table(schema = "core", name="glycan_sequence")
public class GlycanSequence implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer glycanSequenceId;
	private String composition;
	private Integer contributorId;
	private Timestamp dateContributed;
	private Timestamp dateEntered;
	private BigDecimal massAverage;
	private BigDecimal massMonoisotopic;
	private Short residueCount;
	private String sequenceCt;
	private String sequenceCtCondensed;
	private String sequenceGlycam;
	private String sequenceGws;
	private String sequenceIupac;
	private Set<GlycanSequenceToBiologicalContext> glycanSequenceToBiologicalContexts;

    public GlycanSequence() {
    }


	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="glycan_sequence_id", unique=true, nullable=false)
	public Integer getGlycanSequenceId() {
		return this.glycanSequenceId;
	}

	public void setGlycanSequenceId(Integer glycanSequenceId) {
		this.glycanSequenceId = glycanSequenceId;
	}


	@Column(length=64)
	public String getComposition() {
		return this.composition;
	}

	public void setComposition(String composition) {
		this.composition = composition;
	}


	@Column(name="contributor_id", nullable=false)
	public Integer getContributorId() {
		return this.contributorId;
	}

	public void setContributorId(Integer contributorId) {
		this.contributorId = contributorId;
	}


	@Column(name="date_contributed", nullable=false)
	public Timestamp getDateContributed() {
		return this.dateContributed;
	}

	public void setDateContributed(Timestamp dateContributed) {
		this.dateContributed = dateContributed;
	}


	@Column(name="date_entered", nullable=false)
	public Timestamp getDateEntered() {
		return this.dateEntered;
	}

	public void setDateEntered(Timestamp dateEntered) {
		this.dateEntered = dateEntered;
	}


	@Column(name="mass_average", precision=131089)
	public BigDecimal getMassAverage() {
		return this.massAverage;
	}

	public void setMassAverage(BigDecimal massAverage) {
		this.massAverage = massAverage;
	}


	@Column(name="mass_monoisotopic", precision=131089)
	public BigDecimal getMassMonoisotopic() {
		return this.massMonoisotopic;
	}

	public void setMassMonoisotopic(BigDecimal massMonoisotopic) {
		this.massMonoisotopic = massMonoisotopic;
	}


	@Column(name="residue_count")
	public Short getResidueCount() {
		return this.residueCount;
	}

	public void setResidueCount(Short residueCount) {
		this.residueCount = residueCount;
	}


	@Column(name="sequence_ct", nullable=false, length=65535)
	public String getSequenceCt() {
		return this.sequenceCt;
	}

	public void setSequenceCt(String sequenceCt) {
		this.sequenceCt = sequenceCt;
	}


	@Column(name="sequence_ct_condensed", length=65535)
	public String getSequenceCtCondensed() {
		return this.sequenceCtCondensed;
	}

	public void setSequenceCtCondensed(String sequenceCtCondensed) {
		this.sequenceCtCondensed = sequenceCtCondensed;
	}


	@Column(name="sequence_glycam", length=2147483647)
	public String getSequenceGlycam() {
		return this.sequenceGlycam;
	}

	public void setSequenceGlycam(String sequenceGlycam) {
		this.sequenceGlycam = sequenceGlycam;
	}


	@Column(name="sequence_gws", nullable=false, length=65535)
	public String getSequenceGws() {
		return this.sequenceGws;
	}

	public void setSequenceGws(String sequenceGws) {
		this.sequenceGws = sequenceGws;
	}


	@Column(name="sequence_iupac", length=65535)
	public String getSequenceIupac() {
		return this.sequenceIupac;
	}

	public void setSequenceIupac(String sequenceIupac) {
		this.sequenceIupac = sequenceIupac;
	}


	//bi-directional many-to-one association to GlycanSequenceToBiologicalContext
	@OneToMany(mappedBy="glycanSequence")
	@Fetch(FetchMode.JOIN) 
	public Set<GlycanSequenceToBiologicalContext> getGlycanSequenceToBiologicalContexts() {
		return this.glycanSequenceToBiologicalContexts;
	}

	public void setGlycanSequenceToBiologicalContexts(Set<GlycanSequenceToBiologicalContext> glycanSequenceToBiologicalContexts) {
		this.glycanSequenceToBiologicalContexts = glycanSequenceToBiologicalContexts;
	}
	
}