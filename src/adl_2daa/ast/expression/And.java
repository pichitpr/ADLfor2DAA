package adl_2daa.ast.expression;

import java.util.List;

import adl_2daa.ast.ASTExpression;
import adl_2daa.internal.Instruction;
import adl_2daa.internal.OpParam;
import adl_2daa.internal.Opcode;
import adl_2daa.tool.ADLCompiler;

public class And extends ASTBinary{

	public And(ASTExpression left, ASTExpression right) {
		super(left, right);
	}

	@Override
	public void compile(List<Instruction> ins, ADLCompiler compiler) {
		super.compile(ins, compiler);
		ins.add(new Instruction(Opcode.B_OP,new Object[]{OpParam.AND}));
	}

	@Override
	protected void parseBinaryOp(StringBuilder str) {
		str.append("&&");
	}

}
