package au.edu.mq.cbms.unicarbkb.webservices.model.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * BiologicalContext class 
 * 
 * @author Jingyu ZHANG 
 * 
 */
@XmlRootElement (name = "biologicalContext")
@XmlAccessorType (XmlAccessType.FIELD)
public class BiologicalContextResponse {
	@XmlElement
	protected TaxonomyResponse taxonomy; 
	@XmlElement
	protected TissueTaxonomyResponse tissueTaxonomy;
	/**
	 * @return the taxonomy
	 */
	public TaxonomyResponse getTaxonomy() {
		return taxonomy;
	}
	/**
	 * @param taxonomy the taxonomy to set
	 */
	public void setTaxonomy(TaxonomyResponse taxonomy) {
		this.taxonomy = taxonomy;
	}
	/**
	 * @return the tissueTaxonomy
	 */
	public TissueTaxonomyResponse getTissueTaxonomy() {
		return tissueTaxonomy;
	}
	/**
	 * @param tissueTaxonomy the tissueTaxonomy to set
	 */
	public void setTissueTaxonomy(TissueTaxonomyResponse tissueTaxonomy) {
		this.tissueTaxonomy = tissueTaxonomy;
	}
}
