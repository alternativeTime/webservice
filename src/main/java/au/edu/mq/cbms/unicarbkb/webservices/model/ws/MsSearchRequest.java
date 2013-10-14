package au.edu.mq.cbms.unicarbkb.webservices.model.ws;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement (name = "query", namespace = "http://www.unicarbkb.org/core")
@XmlAccessorType (XmlAccessType.FIELD)
public class MsSearchRequest {
	@XmlAttribute(required = true)
	private int algorithm;
	@XmlAttribute(required = true)
	private int numberofresult;
	@XmlElement(name="mass")
	private List<Mass> masses;
	/**
	 * @return the numberofresult
	 */
	public int getNumberofresult() {
		return numberofresult;
	}
	/**
	 * @param numberofresult the numberofresult to set
	 */
	public void setNumberofresult(int numberofresult) {
		this.numberofresult = numberofresult;
	}
	/**
	 * @return the algorithm
	 */
	public int getAlgorithm() {
		return algorithm;
	}
	/**
	 * @param algorithm the algorithm to set
	 */
	public void setAlgorithm(int algorithm) {
		this.algorithm = algorithm;
	}
	/**
	 * @return the masses
	 */
	public List<Mass> getMasses() {
		return masses;
	}
	/**
	 * @param masses the masses to set
	 */
	public void setMasses(List<Mass> masses) {
		this.masses = masses;
	}
}
