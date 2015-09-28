package adl_2daa.ast.expression;

import java.util.List;

import adl_2daa.ast.ASTExpression;
import adl_2daa.internal.Instruction;
import adl_2daa.internal.Opcode;
import adl_2daa.tool.ADLCompiler;

public class StringConstant extends ASTExpression {

	private String value;
	
	public StringConstant(String value){
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}

	@Override
	public void compile(List<Instruction> ins, ADLCompiler compiler) {
		ins.add(new Instruction(Opcode.PUSH, new Object[]{value}));
	}

	@Override
	public void toScript(StringBuilder str, int indent) {
		str.append('"').append(value).append('"');
	}
}
