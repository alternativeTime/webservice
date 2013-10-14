package au.edu.mq.cbms.unicarbkb.webservices.model.ws;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Structure class 
 * 
 * @author Jingyu ZHANG 
 * 
 */
@XmlRootElement (name = "queryResult", namespace = "http://www.unicarbkb.org/core")
@XmlAccessorType (XmlAccessType.FIELD)
public class CompleteListResponse {
	@XmlAttribute 
	private String id;
	@XmlAttribute
	private String baseURL;
	@XmlAttribute(required = true)	
	private int numberofStructure;
	@XmlElement(name="structure")
	private List<StructureComplete> StructureComplete;
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the baseURL
	 */
	public String getBaseURL() {
		return baseURL;
	}
	/**
	 * @param baseURL the baseURL to set
	 */
	public void setBaseURL(String baseURL) {
		this.baseURL = baseURL;
	}
	/**
	 * @return the numberofStructure
	 */
	public int getNumberofStructure() {
		return numberofStructure;
	}
	/**
	 * @param numberofStructure the numberofStructure to set
	 */
	public void setNumberofStructure(int numberofStructure) {
		this.numberofStructure = numberofStructure;
	}
	/**
	 * @return the structureComplete
	 */
	public List<StructureComplete> getStructureComplete() {
		return StructureComplete;
	}
	/**
	 * @param structureComplete the structureComplete to set
	 */
	public void setStructureComplete(List<StructureComplete> structureComplete) {
		StructureComplete = structureComplete;
	}
	public static CompleteListResponse composeCompleteList(
			List<StructureComplete> aList, CompleteListResponse aIdList) {
		aIdList.setBaseURL("http://www.unicarbkb.org/");
		aIdList.setNumberofStructure(aList.size());
		aIdList.setStructureComplete(aList);
		return aIdList;
	}
}
