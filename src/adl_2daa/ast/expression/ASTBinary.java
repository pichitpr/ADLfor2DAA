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
}
