package au.edu.mq.cbms.unicarbkb.webservices.model.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement (name = "glycansequence")
@XmlAccessorType (XmlAccessType.FIELD)
public class GlycanSequenceContent {
	@XmlElement (name = "glycansequence")
	private String content;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
