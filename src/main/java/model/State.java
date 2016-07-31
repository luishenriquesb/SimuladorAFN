package model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class State {

	@XmlAttribute
	private String id;

	@XmlAttribute
	private String name;

	@XmlElement(name = "initial")
	private String initialState;
	
	@XmlElement(name = "final")
	private String finalState;
	
	@XmlElement
	private String y;
	
	@XmlElement
	private String x;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getInitialState() {
		return initialState;
	}

	public void setInitialState(String initialState) {
		this.initialState = initialState;
	}

	public String getFinalState() {
		return finalState;
	}

	public void setFinalState(String finalState) {
		this.finalState = finalState;
	}

	public String getY() {
		return y;
	}

	public void setY(String y) {
		this.y = y;
	}

	public String getX() {
		return x;
	}

	public void setX(String x) {
		this.x = x;
	}

	



}
