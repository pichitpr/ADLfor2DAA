package adl_2daa.ast.statement;

import java.util.List;

import adl_2daa.ast.ASTExpression;
import adl_2daa.ast.ASTStatement;
import adl_2daa.internal.Instruction;
import adl_2daa.internal.OpParam;
import adl_2daa.internal.Opcode;
import adl_2daa.tool.ADLCompiler;

public class Loop extends ASTStatement{

	private ASTExpression loopCount;
	private List<ASTStatement> content;
	
	public Loop(ASTExpression loopCount, List<ASTStatement> content) {
		super();
		this.loopCount = loopCount;
		this.content = content;
	}

	public ASTExpression getLoopCount() {
		return loopCount;
	}

	public List<ASTStatement> getContent() {
		return content;
	}

	@Override
	public void compile(List<Instruction> ins, ADLCompiler compiler) {
		loopCount.compile(ins, compiler);
		int startLabel = compiler.getCurrentLabel();
		int endLabel = compiler.getCurrentLabel();
		int memIndex = compiler.getCurrentMemIndex();
		ins.add(new Instruction(Opcode.ILABEL, new Object[]{startLabel}));
		ins.add(new Instruction(Opcode.ASSIGN, new Object[]{memIndex}));
		ins.add(new Instruction(Opcode.PUSH, new Object[]{0}));
		ins.add(new Instruction(Opcode.LOAD, new Object[]{memIndex}));
		ins.add(new Instruction(Opcode.COMPARE, new Object[]{OpParam.GT}));
		ins.add(new Instruction(Opcode.IBNEQ, new Object[]{endLabel}));
		for(ASTStatement st : content)
			st.compile(ins, compiler);
		ins.add(new Instruction(Opcode.PUSH, new Object[]{1}));
		ins.add(new Instruction(Opcode.LOAD, new Object[]{memIndex}));
		ins.add(new Instruction(Opcode.B_OP, new Object[]{OpParam.SUB}));
		ins.add(new Instruction(Opcode.IJUMP, new Object[]{startLabel}));
		ins.add(new Instruction(Opcode.ILABEL, new Object[]{endLabel}));
	}

	@Override
	public void toScript(StringBuilder str, int indent) {
		for(int i=1; i<=indent; i++) str.append('\t');
		str.append("loop").append('(');
		loopCount.toScript(str, 0);
		str.append(')').append('{').append('\n');
		for(ASTStatement st : content){
			st.toScript(str, indent+1);
			str.append('\n');
		}
		for(int i=1; i<=indent; i++) str.append('\t');
		str.append('}');
	}
}
