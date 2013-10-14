

package au.edu.mq.cbms.unicarbkb.webservices.model.ws;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@XmlRootElement (name = "queryResult", namespace = "http://www.unicarbkb.org/core")
@XmlAccessorType (XmlAccessType.FIELD)
public class IdListResponse {
	@XmlAttribute
	private String id;
	@XmlAttribute
	private String baseURL;
	@XmlAttribute(required = true)	
	private int numberofStructure;
	@XmlElement(name="structure")
	private List<Structure> structures;
	public String getId() {
		return id;
	}
	public void setId(String id) { 
		this.id = id;
	}
	public String getBaseURL() {
		return baseURL;
	}
	public void setBaseURL(String baseURL) {
		this.baseURL = baseURL;
	}
	public int getNumberofStructure() {
		return numberofStructure;
	}
	public void setNumberofStructure(int numberofStructure) {
		this.numberofStructure = numberofStructure;
	}
	public List<Structure> getStructures() {
		return structures;
	}
	public void setStructures(List<Structure> structures) {
		this.structures = structures;
	}


	
	
}
