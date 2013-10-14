package au.edu.mq.cbms.unicarbkb.webservices.model.ws;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement (name = "query", namespace = "http://www.unicarbkb.org/core")
@XmlAccessorType (XmlAccessType.FIELD)
public class IdListRequest {
	@XmlElement(name="structure")
	private List<Structure> structures;

	/**
	 * @return the structures
	 */
	public List<Structure> getStructures() {
		return structures;
	}

	/**
	 * @param structures the structures to set
	 */
	public void setStructures(List<Structure> structures) {
		this.structures = structures;
	}
}
