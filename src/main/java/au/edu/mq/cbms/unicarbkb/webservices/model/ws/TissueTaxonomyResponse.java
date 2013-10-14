package au.edu.mq.cbms.unicarbkb.webservices.model.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement (name = "tissueTaxonomy")
@XmlAccessorType (XmlAccessType.FIELD)
public class TissueTaxonomyResponse {
	@XmlAttribute
	protected String meshId;
	@XmlAttribute
	protected String name;
	/**
	 * @return the meshId
	 */
	public String getMeshId() {
		return meshId;
	}
	/**
	 * @param meshId the meshId to set
	 */
	public void setMeshId(String meshId) {
		this.meshId = meshId;
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
