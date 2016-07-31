package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class Automaton {
	@XmlElement
	private List<State> state;
	@XmlElement
	private List<Transition> transition;

	public List<State> getState() {
		return state;
	}

	public void setState(List<State> state) {
		this.state = state;
	}

	public List<Transition> getTransition() {
		return transition;
	}

	public void setTransition(List<Transition> transition) {
		this.transition = transition;
	}

	
	
	public List<Transition> getTransitionsFromStateBySymbol(State state, String symbol) {
		List<Transition> trasitions = this.getTransition();
		List<Transition> ret = new ArrayList<Transition>();
		for (Transition transition : trasitions) {
			if(transition.getFrom().equals(state.getId()) && transition.getRead().equals(symbol))
				ret.add(transition);
		}
		
		return ret;
			
	}
	
	public State getInitialState(){
		for (State state : this.getState()) {
			if(state.getInitialState() != null)
				return state;
		}
		return null;
	}
	
	
	public List<String> getAlphabet() {
		List<String> alphabet = new ArrayList<String>();
		for (Transition transistion : this.getTransition()) {
			String symbol = transistion.getRead();
			if(!alphabet.contains(symbol))
				alphabet.add(symbol);
		} 
		
		return alphabet;
	}
	
	public State findStateById(String id) {
		for (State state : this.getState()) {
			if(id.equals(state.getId()))
				return state;
		}
		return null;
	}
	
	public State findStateByName(String name) {
		for (State state : this.getState()) {
			if(name.equals(state.getName()))
				return state;
		}
		return null;
	}
	
	
	
	
	public List<Transition> getAllTransitionsFromStateBySymbol(List<String> originStateNames,
			String symbol) {
		
		List<Transition> transitions = new ArrayList<Transition>();
		
		for (String stateName : originStateNames) {
			State originState = this.findStateByName(stateName); 
			transitions.addAll(this.getTransitionsFromStateBySymbol(originState, symbol));
		}
		
		return transitions;

	}
	
	public int lastIdofState() {
		List<String> idList = new ArrayList<String>();
	
		for (State state : this.getState() ) {
			idList.add(state.getId());
		}
		
		Collections.sort(idList);
		
		return Integer.parseInt(idList.get(idList.size()-1));
	}
	
	@Override
	public String toString() {
		return "ClassPojo [state = " + state + ", transition = " + transition + "]";
	}

	
}