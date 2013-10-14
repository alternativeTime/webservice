package au.edu.mq.cbms.unicarbkb.webservices.model.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
/**
 * JUnit Test class which makes a request to the RESTful of substructure web service.
 * 
 * @author Jingyu ZHANG jingyuzhang2008@gmail.com
 * 
 */
@XmlRootElement (name = "structure")
@XmlAccessorType (XmlAccessType.FIELD)
public class Structure {
	@XmlAttribute (required = true)
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
