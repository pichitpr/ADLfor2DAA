package adl_2daa.ast.statement;

import java.util.List;

import adl_2daa.IAction;
import adl_2daa.Registry;
import adl_2daa.ast.ASTExpression;
import adl_2daa.ast.ASTStatement;
import adl_2daa.internal.Instruction;
import adl_2daa.internal.Opcode;
import adl_2daa.tool.ADLCompiler;

public class Action extends ASTStatement{

	private String name;
	private ASTExpression[] params;
	
	public Action(String name, List<ASTExpression> params) {
		super();
		this.name = name;
		this.params = new ASTExpression[params.size()];
		this.params = params.toArray(this.params);
	}
	
	public String getName() {
		return name;
	}

	public ASTExpression[] getParams() {
		return params;
	}

	@Override
	public void compile(List<Instruction> ins, ADLCompiler compiler) {
		IAction action = Registry.getAction(name);
		if(action == null){
			System.out.println("No action registered - "+name);
		}
		
		int endLabel = compiler.getCurrentLabel();
		ins.add(new Instruction(Opcode.STARTACTION,null));
		if(action.isSpanAction()){
			ins.add(new Instruction(Opcode.ILABEL, new Object[]{endLabel}));
		}
		for(int i=params.length-1; i>=0; i--){
			params[i].compile(ins, compiler);
		}
		ins.add(new Instruction(Opcode.ACTION, new Object[]{name,params.length}));
		if(action.isSpanAction()){
			ins.add(new Instruction(Opcode.BREAK,null));
			ins.add(new Instruction(Opcode.IBNEQ, new Object[]{endLabel}));
		}
		ins.add(new Instruction(Opcode.ENDACTION, null));
	}
}
