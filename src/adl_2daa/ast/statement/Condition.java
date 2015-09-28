package adl_2daa.ast.statement;

import java.util.List;

import adl_2daa.ast.ASTExpression;
import adl_2daa.ast.ASTStatement;
import adl_2daa.internal.Instruction;
import adl_2daa.internal.Opcode;
import adl_2daa.tool.ADLCompiler;

public class Condition extends ASTStatement{

	private ASTExpression condition;
	private List<ASTStatement> ifblock,elseblock;
	
	public Condition(ASTExpression condition, List<ASTStatement> ifblock,
			List<ASTStatement> elseblock) {
		super();
		this.condition = condition;
		this.ifblock = ifblock;
		this.elseblock = elseblock;
	}
	
	public ASTExpression getCondition() {
		return condition;
	}

	public List<ASTStatement> getIfblock() {
		return ifblock;
	}

	public List<ASTStatement> getElseblock() {
		return elseblock;
	}

	@Override
	public void compile(List<Instruction> ins, ADLCompiler compiler) {
		condition.compile(ins, compiler);
		int endLabel = compiler.getCurrentLabel();
		int endLabel2 = compiler.getCurrentLabel();
		ins.add(new Instruction(Opcode.IBNEQ, new Object[]{endLabel}));
		for(ASTStatement st : ifblock)
			st.compile(ins, compiler);
		ins.add(new Instruction(Opcode.IJUMP, new Object[]{endLabel2}));
		ins.add(new Instruction(Opcode.ILABEL, new Object[]{endLabel}));
		if(elseblock != null){
			for(ASTStatement st : elseblock)
				st.compile(ins, compiler);
		}
		ins.add(new Instruction(Opcode.ILABEL, new Object[]{endLabel2}));
	}

	@Override
	public void toScript(StringBuilder str, int indent) {
		for(int i=1; i<=indent; i++) str.append('\t');
		str.append("if").append('(');
		condition.toScript(str, 0);
		str.append(')').append('{').append('\n');
		for(ASTStatement st : ifblock){
			st.toScript(str, indent+1);
			str.append('\n');
		}
		for(int i=1; i<=indent; i++) str.append('\t');
		str.append('}');
		
		if(elseblock == null) return;
		str.append(" else ").append('{').append('\n');
		for(ASTStatement st : elseblock){
			st.toScript(str, indent+1);
			str.append('\n');
		}
		for(int i=1; i<=indent; i++) str.append('\t');
		str.append('}');
		
	}
}
