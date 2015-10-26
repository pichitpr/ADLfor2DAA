package adl_2daa.ast.expression;

import java.util.List;

import adl_2daa.ast.ASTExpression;
import adl_2daa.internal.Instruction;
import adl_2daa.internal.OpParam;
import adl_2daa.internal.Opcode;
import adl_2daa.tool.ADLCompiler;

public class ASTUnary extends ASTExpression{

	public enum UnaryOp{
		NEG("-",OpParam.SUB),
		NOT("!",OpParam.NEQ);
		
		private String tag;
		private OpParam assocParam;
		private UnaryOp(String opTag,OpParam assocParam){
			tag = opTag;
			this.assocParam = assocParam;
		}
		
		public String getTag(){ return tag; }
		protected OpParam getOpParam(){ return assocParam; }
		
		public static UnaryOp getByTag(String tag) throws Exception{
			for(UnaryOp enumOp : UnaryOp.values()){
				if(enumOp.tag.equals(tag)){
					return enumOp;
				}
			}
			throw new Exception("Enum not found for tag : "+tag);
		}
	}
	
	public ASTExpression node;
	public UnaryOp op;

	public ASTUnary(String op, ASTExpression node) throws Exception {
		this(UnaryOp.getByTag(op), node);
	}
	
	public ASTUnary(UnaryOp op, ASTExpression node){
		super();
		this.op = op;
		this.node = node;
	}

	public ASTExpression getNode(){
		return node;
	}
	
	@Override
	public void compile(List<Instruction> ins, ADLCompiler compiler) {
		node.compile(ins, compiler);
		ins.add(new Instruction(Opcode.U_OP,new Object[]{op.assocParam}));
	}

	@Override
	public void toScript(StringBuilder str, int indent) {
		for(UnaryOp op : UnaryOp.values()){
			if(this.op == op){
				str.append(op.tag);
				break;
			}
		}
		node.toScript(str, 1);
	}
}
