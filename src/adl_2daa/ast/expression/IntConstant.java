package adl_2daa.ast.expression;

import java.util.List;

import adl_2daa.ast.ASTExpression;
import adl_2daa.internal.Instruction;
import adl_2daa.internal.Opcode;
import adl_2daa.tool.ADLCompiler;

public class IntConstant extends ASTExpression {

	private int value;
	
	public IntConstant(String value){
		this.value = Integer.parseInt(value);
	}
	
	public int getValue() {
		return value;
	}

	@Override
	public void compile(List<Instruction> ins, ADLCompiler compiler) {
		ins.add(new Instruction(Opcode.PUSH, new Object[]{value}));
	}
}
