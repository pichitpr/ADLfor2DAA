package adl_2daa.ast.expression;

import java.util.List;

import adl_2daa.ast.ASTExpression;
import adl_2daa.internal.Instruction;
import adl_2daa.internal.OpParam;
import adl_2daa.internal.Opcode;
import adl_2daa.tool.ADLCompiler;

public class Comparison extends ASTBinary{

	public enum Comp{
		EQ("==",OpParam.EQ),
		NEQ("!=",OpParam.NEQ),
		LT("<",OpParam.LT),
		GT(">",OpParam.GT),
		LE("<=",OpParam.LE),
		GE(">=",OpParam.GE);
		
		private String tag;
		private OpParam assocParam;
		private Comp(String opTag,OpParam assocParam){
			tag = opTag;
			this.assocParam = assocParam;
		}
		
		public String getTag(){ return tag; }
		protected OpParam getOpParam(){ return assocParam; }
		
		public static Comp getByTag(String tag) throws Exception{
			for(Comp enumOp : Comp.values()){
				if(enumOp.tag.equals(tag)){
					return enumOp;
				}
			}
			throw new Exception("Enum not found for tag : "+tag);
		}
	}
	
	private Comp op;
	
	public Comparison(ASTExpression left, String op, ASTExpression right) throws Exception {
		super(left, right);
		this.op = Comp.getByTag(op);
	}
	
	public Comp getOp(){
		return this.op;
	}
	
	@Override
	public void compile(List<Instruction> ins, ADLCompiler compiler) {
		super.compile(ins, compiler);
		ins.add(new Instruction(Opcode.COMPARE,new Object[]{op.assocParam}));
	}
}
