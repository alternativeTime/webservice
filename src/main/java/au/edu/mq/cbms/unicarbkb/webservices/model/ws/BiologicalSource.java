package au.edu.mq.cbms.unicarbkb.webservices.model.ws;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement (name = "biologicalSource")
@XmlAccessorType (XmlAccessType.FIELD)
public class BiologicalSource {
	@XmlElement(name="biologicalContextResponse")
	private List<BiologicalContextResponse> biologicalContextResponses;

	/**
	 * @return the biologicalContextResponses
	 */
	public List<BiologicalContextResponse> getBiologicalContextResponses() {
		return biologicalContextResponses;
	}

	/**
	 * @param biologicalContextResponses the biologicalContextResponses to set
	 */
	public void setBiologicalContextResponses(
			List<BiologicalContextResponse> biologicalContextResponses) {
		this.biologicalContextResponses = biologicalContextResponses;
	}
}
