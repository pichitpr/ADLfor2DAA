package adl_2daa.ast.structure;

import java.util.List;

import adl_2daa.ast.ASTBlock;

public class Root extends ASTBlock{

	private List<Agent> relatedAgents;

	public Root(List<Agent> relatedAgents) {
		super(null);
		this.relatedAgents = relatedAgents;
	}

	public List<Agent> getRelatedAgents() {
		return relatedAgents;
	}
	
}
