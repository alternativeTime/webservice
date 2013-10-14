package au.edu.mq.cbms.unicarbkb.webservices.model.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement (name = "taxonomy")
@XmlAccessorType (XmlAccessType.FIELD)
public class TaxonomyResponse {
	@XmlAttribute
	private String ncbiId;
	@XmlAttribute
	private String name;
	/**
	 * @return the ncbiId
	 */
	public String getNcbiId() {
		return ncbiId;
	}
	/**
	 * @param ncbiId the ncbiId to set
	 */
	public void setNcbiId(String ncbiId) {
		this.ncbiId = ncbiId;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	
	
}
