package adl_2daa.ast.expression;

import java.util.List;

import adl_2daa.ast.ASTExpression;
import adl_2daa.internal.Instruction;
import adl_2daa.internal.OpParam;
import adl_2daa.internal.Opcode;
import adl_2daa.tool.ADLCompiler;

public class Bitwise extends ASTBinary{

	public enum BitOp{
		B_AND("&",OpParam.B_AND),
		B_OR("|",OpParam.B_OR),
		B_XOR("^",OpParam.B_XOR),
		SHIFT_L("<<",OpParam.B_LSHIFT),
		SHIFT_R(">>",OpParam.B_RSHIFT);
		
		private String tag;
		private OpParam assocParam;
		private BitOp(String opTag,OpParam assocParam){
			tag = opTag;
			this.assocParam = assocParam;
		}
		
		public String getTag(){ return tag; }
		protected OpParam getOpParam(){ return assocParam; }
		
		public static BitOp getByTag(String tag) throws Exception{
			for(BitOp enumOp : BitOp.values()){
				if(enumOp.tag.equals(tag)){
					return enumOp;
				}
			}
			throw new Exception("Enum not found for tag : "+tag);
		}
	}
	
	public BitOp op;
	
	public Bitwise(ASTExpression left, String op, ASTExpression right) throws Exception {
		this(left, BitOp.getByTag(op), right);
	}
	
	public Bitwise(ASTExpression left, BitOp op, ASTExpression right){
		super(left, right);
		this.op = op;
	}
	
	@Override
	public void compile(List<Instruction> ins, ADLCompiler compiler) {
		super.compile(ins, compiler);
		ins.add(new Instruction(Opcode.B_OP,new Object[]{op.assocParam}));
	}

	@Override
	protected void parseBinaryOp(StringBuilder str) {
		for(BitOp op : BitOp.values()){
			if(this.op == op){
				str.append(op.tag);
				break;
			}
		}
	}
}
