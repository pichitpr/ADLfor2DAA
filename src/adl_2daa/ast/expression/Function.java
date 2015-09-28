package adl_2daa.ast.expression;

import java.util.List;

import adl_2daa.ast.ASTExpression;
import adl_2daa.internal.Instruction;
import adl_2daa.internal.Opcode;
import adl_2daa.tool.ADLCompiler;

public class Function extends ASTExpression{

	private String name;
	private ASTExpression[] params;
	private boolean singleQuery = false;
	
	public Function(String name, List<ASTExpression> params, boolean singleQuery) {
		super();
		this.name = name;
		this.params = new ASTExpression[params.size()];
		this.params = params.toArray(this.params);
		this.singleQuery = singleQuery;
	}
	
	public String getName(){
		return name;
	}
	
	public ASTExpression[] getParams(){
		return params;
	}
	
	public boolean hasSingleQuery(){
		return singleQuery;
	}
	
	@Override
	public void compile(List<Instruction> ins, ADLCompiler compiler) {
		for(int i=params.length-1; i>=0; i--){
			params[i].compile(ins, compiler);
		}
		ins.add(new Instruction(Opcode.FUNC, 
				new Object[]{name,params.length,singleQuery}));
	}

	@Override
	public void toScript(StringBuilder str, int indent) {
		str.append(name).append('(');
		for(int i=0; i<params.length; i++){
			params[i].toScript(str, 0);
			if(i < params.length-1)
				str.append(',').append(' ');
		}
		str.append(')');
		if(singleQuery) str.append('$');
	}
}
