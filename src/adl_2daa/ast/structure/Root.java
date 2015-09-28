package adl_2daa.ast.structure;

import java.util.List;

import adl_2daa.ast.Reversible;

public class Root implements Reversible{

	private List<Agent> relatedAgents;

	public Root(List<Agent> relatedAgents) {
		this.relatedAgents = relatedAgents;
	}

	public List<Agent> getRelatedAgents() {
		return relatedAgents;
	}
	
	@Override
	public void toScript(StringBuilder str, int indent){
		for(Agent agent : relatedAgents){
			agent.toScript(str, 0);
			str.append('\n');
		}
	}
}
