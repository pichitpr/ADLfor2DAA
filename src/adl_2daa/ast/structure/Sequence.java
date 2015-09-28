package adl_2daa.ast.structure;

import java.util.List;

import adl_2daa.ast.ASTStatement;
import adl_2daa.ast.ASTBlock;

public class Sequence extends ASTBlock{

	private List<ASTStatement> statements;
	
	public Sequence(String identifier, List<ASTStatement> statements) {
		super(identifier);
		this.statements = statements;
	}

	public List<ASTStatement> getStatements() {
		return statements;
	}

	@Override
	protected void parseBlockContent(StringBuilder str, int contentIndent) {
		for(ASTStatement st : statements){
			st.toScript(str, contentIndent);
			str.append('\n');
		}
	}
}
