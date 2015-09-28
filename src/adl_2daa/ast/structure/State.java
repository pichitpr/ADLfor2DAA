package adl_2daa.ast.structure;

import java.util.List;

import adl_2daa.ast.ASTBlock;

public class State extends ASTBlock {

	private List<Sequence> sequences;
	
	public State(String identifier, List<Sequence> sequences) {
		super(identifier);
		this.sequences = sequences;
	}

	public List<Sequence> getSequences() {
		return sequences;
	}

	@Override
	protected void parseBlockContent(StringBuilder str, int contentIndent) {
		for(Sequence seq : sequences){
			seq.toScript(str, contentIndent);
			str.append('\n');
		}
	}
}
