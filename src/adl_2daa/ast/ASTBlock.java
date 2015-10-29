package adl_2daa.ast;

public abstract class ASTBlock implements Reversible{

	protected String identifier;
	
	protected ASTBlock(String identifier){
		this.identifier = identifier;
	}
	
	public String getIdentifier(){
		return identifier;
	}
	
	@Override
	public void toScript(StringBuilder str, int indent){
		for(int i=1; i<=indent; i++) str.append('\t');
		str.append('.').append(identifier).append('{').append(System.lineSeparator());
		
		parseBlockContent(str, indent+1);
		
		for(int i=1; i<=indent; i++) str.append('\t');
		str.append('}');
	}
	
	protected abstract void parseBlockContent(StringBuilder str, int contentIndent);
}
