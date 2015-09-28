package adl_2daa.ast.expression;

import java.util.List;

import adl_2daa.ast.ASTExpression;
import adl_2daa.internal.Instruction;
import adl_2daa.tool.ADLCompiler;

public abstract class ASTBinary extends ASTExpression{

	public ASTExpression left,right;

	public ASTBinary(ASTExpression left, ASTExpression right) {
		super();
		this.left = left;
		this.right = right;
	}
	
	public ASTExpression getLeft() {
		return left;
	}

	public ASTExpression getRight() {
		return right;
	}

	@Override
	public void compile(List<Instruction> ins, ADLCompiler compiler) {
		this.right.compile(ins, compiler);
		this.left.compile(ins, compiler);
	}
	
	@Override
	public void toScript(StringBuilder str, int indent){
		str.append('(');
		left.toScript(str, 0);
		str.append(' ');
		parseBinaryOp(str);
		str.append(' ');
		right.toScript(str, 0);
		str.append(')');
	}
	
	protected abstract void parseBinaryOp(StringBuilder str);
}
