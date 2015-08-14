package adl_2daa.ast;

public abstract class ASTBlock {

	protected String identifier;
	
	protected ASTBlock(String identifier){
		this.identifier = identifier;
	}
	
	public String getIdentifier(){
		return identifier;
	}
}
