package adl_2daa.ast;

public interface Reversible {
	/**
	 * Reverse the AST element implementing this interface back to ADL
	 * and add to the StringBuilder str. indent specifies a number of tabs
	 * preceding the ADL. In ASTExpression case where there is no indentation,
	 * indent specifies if there should be (...) surrounding the expression or not.
	 * 0: No parenthesis   >0: Parenthesis required  
	 */
	public void toScript(StringBuilder str, int indent);
}
