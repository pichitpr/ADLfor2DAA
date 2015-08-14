package adl_2daa.ast.structure;

import java.util.List;

import adl_2daa.ast.ASTBlock;

public class Agent extends ASTBlock{

	private Sequence init,des;
	private List<State> states;
	
	public Agent(String identifier, Sequence init, Sequence des,
			List<State> states) {
		super(identifier);
		this.init = init;
		this.des = des;
		this.states = states;
	}

	public Sequence getInit() {
		return init;
	}

	public Sequence getDes() {
		return des;
	}

	public List<State> getStates() {
		return states;
	}
}
