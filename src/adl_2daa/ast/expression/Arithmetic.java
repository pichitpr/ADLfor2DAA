package adl_2daa.ast.expression;

import java.util.List;

import adl_2daa.ast.ASTExpression;
import adl_2daa.internal.Instruction;
import adl_2daa.internal.OpParam;
import adl_2daa.internal.Opcode;
import adl_2daa.tool.ADLCompiler;

public class Arithmetic extends ASTBinary{

	public enum MathOp{
		ADD("+",OpParam.ADD),
		SUB("-",OpParam.SUB),
		MUL("*",OpParam.MUL),
		DIV("/",OpParam.DIV),
		MOD("%",OpParam.MOD);
		
		private String tag;
		private OpParam assocParam;
		private MathOp(String opTag,OpParam assocParam){
			tag = opTag;
			this.assocParam = assocParam;
		}
		
		public String getTag(){ return tag; }
		protected OpParam getOpParam(){ return assocParam; }
		
		public static MathOp getByTag(String tag) throws Exception{
			for(MathOp enumOp : MathOp.values()){
				if(enumOp.tag.equals(tag)){
					return enumOp;
				}
			}
			throw new Exception("Enum not found for tag : "+tag);
		}
	}
	
	public MathOp op;
	
	public Arithmetic(ASTExpression left, String op, ASTExpression right) throws Exception {
		super(left, right);
		this.op = MathOp.getByTag(op);
	}
	
	@Override
	public void compile(List<Instruction> ins, ADLCompiler compiler) {
		super.compile(ins, compiler);
		ins.add(new Instruction(Opcode.B_OP,new Object[]{op.assocParam}));
	}
}
