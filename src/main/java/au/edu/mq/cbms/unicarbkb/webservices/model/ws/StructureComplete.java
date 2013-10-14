package au.edu.mq.cbms.unicarbkb.webservices.model.ws;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.log4j.Logger;

import au.edu.mq.cbms.unicarbkb.webservices.model.BiologicalContext;
import au.edu.mq.cbms.unicarbkb.webservices.model.GlycanSequence;
import au.edu.mq.cbms.unicarbkb.webservices.model.GlycanSequenceToBiologicalContext;

/**
 * StructureComplete class 
 * 
 * @author Jingyu ZHANG 
 * 
 */
@XmlRootElement (name = "structure1")
@XmlAccessorType (XmlAccessType.FIELD)
public class StructureComplete {
	private static Logger logger = Logger.getLogger("StructureComplete");
	@XmlAttribute
	private String id;
	@XmlElement(name="glycanSequence")
	private String glycanSequence;
	@XmlElement(name="biologicalSource")
	private BiologicalSource biologicalSource;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getGlycanSequence() {
		return glycanSequence;
	}
	public void setGlycanSequence(String glycanSequence) {
		this.glycanSequence = glycanSequence;
	}
	public BiologicalSource getBiologicalSource() {
		return biologicalSource;
	}
	public void setBiologicalSource(BiologicalSource biologicalSource) {
		this.biologicalSource = biologicalSource;
	}

	public static void composeOneStructure(List<StructureComplete> aList,
			List<GlycanSequence> results) {
		StructureComplete aStructure = new StructureComplete();
		if (results == null) {
			logger.info(" result is empty");
			aStructure.setId("");
		} else if (results.size() == 0) {
			aStructure.setId("");
		} else {
			aStructure.setId(results.get(0).getGlycanSequenceId() + "");
			aStructure.setGlycanSequence(results.get(0).getSequenceCt());
			// for (GlycanSequence aGlycanSequence:results){
			GlycanSequence aGlycanSequence = results.get(0);
			List<BiologicalContextResponse> aBiologicalContextResponseList = new ArrayList<BiologicalContextResponse>();
			for (GlycanSequenceToBiologicalContext aGlycanSequenceToBiologicalContext : aGlycanSequence
					.getGlycanSequenceToBiologicalContexts()) {
				logger.info("biocontext:"
						+ aGlycanSequenceToBiologicalContext
								.getGlycanSequenceToBiologicalContextId());
				addOneBiologicalContextResponse(aBiologicalContextResponseList,
						aGlycanSequenceToBiologicalContext);
			}
			BiologicalSource aBiologicalSource = new BiologicalSource();
			aBiologicalSource
					.setBiologicalContextResponses(aBiologicalContextResponseList);
			aStructure.setBiologicalSource(aBiologicalSource);
		}
		aList.add(aStructure);
	}

	private static void addOneBiologicalContextResponse(
			List<BiologicalContextResponse> aBiologicalContextResponseList,
			GlycanSequenceToBiologicalContext aGlycanSequenceToBiologicalContext) {
		BiologicalContextResponse aBiologicalContextResponse = new BiologicalContextResponse();
		// set TaxonomyResponse
		BiologicalContext aBiologicalContext = aGlycanSequenceToBiologicalContext
				.getBiologicalContext();
		TaxonomyResponse aTaxonomyResponse = new TaxonomyResponse();
		aTaxonomyResponse.setNcbiId(aBiologicalContext.getTaxonomy()
				.getNcbiId() + "");
		aTaxonomyResponse.setName(aBiologicalContext.getTaxonomy().getTaxon());
		// set TissueTaxonomyResponse
		TissueTaxonomyResponse aTissueTaxonomyResponse = new TissueTaxonomyResponse();
		aTissueTaxonomyResponse.setMeshId(aBiologicalContext
				.getTissueTaxonomy().getMeshId());
		logger.info("mesh ID:"
				+ aBiologicalContext.getTissueTaxonomy().getMeshId());
		aTissueTaxonomyResponse.setName(aBiologicalContext.getTissueTaxonomy()
				.getTissueTaxon());
		aBiologicalContextResponse.setTaxonomy(aTaxonomyResponse);
		aBiologicalContextResponse.setTissueTaxonomy(aTissueTaxonomyResponse);
		aBiologicalContextResponseList.add(aBiologicalContextResponse);
	}
}
