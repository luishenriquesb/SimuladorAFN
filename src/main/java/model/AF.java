package model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "structure")
@XmlAccessorType(XmlAccessType.FIELD)
public class AF {

	@XmlElement
	private Automaton automaton;

	@XmlElement
    private String type;

    public Automaton getAutomaton ()
    {
        return automaton;
    }

    public void setAutomaton (Automaton automaton)
    {
        this.automaton = automaton;
    }

    public String getType ()
    {
        return type;
    }

    public void setType (String type)
    {
        this.type = type;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [automaton = "+automaton+", type = "+type+"]";
    }
	
}
