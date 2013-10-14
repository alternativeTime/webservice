package au.edu.mq.cbms.unicarbkb.webservices.model.ws;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement (name = "query")
@XmlAccessorType (XmlAccessType.FIELD)
public class SubStructureRequest {
//	@XmlAttribute(required = true)
//	private String completeInformation;
	@XmlAttribute(required = true)
	private boolean reducingEnd;
	@XmlAttribute(required = true)
	private boolean terminal;
	@XmlAttribute(required = true)
	private boolean exactMatch;
	@XmlAttribute(required = true)
	private boolean othereResiduces;
	@XmlAttribute(required = true)
	private boolean ignoreReducingAlditol;
	@XmlElement(name="glycansequence", required = true)
	private List<String> sequencects;
	
//	public String getCompleteInformation() {
//		return completeInformation;
//	}
//	public void setCompleteInformation(String completeInformation) {
//		this.completeInformation = completeInformation;
//	}
	public boolean isReducingEnd() {
		return reducingEnd;
	}
	public void setReducingEnd(boolean reducingEnd) {
		this.reducingEnd = reducingEnd;
	}
	public boolean isTerminal() {
		return terminal;
	}
	public void setTerminal(boolean terminal) {
		this.terminal = terminal;
	}
	public boolean isExactMatch() {
		return exactMatch;
	}
	public void setExactMatch(boolean exactMatch) {
		this.exactMatch = exactMatch;
	}
	public boolean isOthereResiduces() {
		return othereResiduces;
	}
	public void setOthereResiduces(boolean othereResiduces) {
		this.othereResiduces = othereResiduces;
	}
	public boolean isIgnoreReducingAlditol() {
		return ignoreReducingAlditol;
	}
	public void setIgnoreReducingAlditol(boolean ignoreReducingAlditol) {
		this.ignoreReducingAlditol = ignoreReducingAlditol;
	}
	public List<String> getSequencects() {
		return sequencects;
	}
	public void setSequencects(List<String> sequencects) {
		this.sequencects = sequencects;
	}
}
