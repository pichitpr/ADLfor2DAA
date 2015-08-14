package adl_2daa.internal;


public class Instruction {
	
	public Opcode opcode;
	public Object[] param;
	
	public Instruction(Opcode opcode,Object[] parameter){
		this.opcode = opcode;
		this.param = parameter;
	}
	
	/*
	public Instruction(DataInputStream in) throws IOException{
		read(in);
	}
	
	public void save(DataOutputStream out) throws IOException{
		out.writeByte(opcode.ordinal());
		switch(opcode){
		case NOP:
		case POP:
			break;
		case JUMP:
		case BNEQ:
			out.writeInt((Integer)param[0]); break;
		case ASSIGN:
			out.writeUTF(param[0].toString()); break;
		case U_OP:
		case B_OP:
		case COMPARE:
			out.writeByte(((OpParam)param[0]).ordinal()); break;
		case FUNC:
			out.writeInt((Integer)param[0]);
			out.writeByte((Integer)param[1]);
			break;
		case PUSH:
			StackDatatype type = (StackDatatype)param[0];
			out.writeByte(type.ordinal());
			switch(type){
			case INT:
				out.writeInt((Integer)param[1]); break;
			case FLOAT:
				out.writeFloat((Float)param[1]); break;
			case STRING:
			case VAR:
				out.writeUTF(param[1].toString()); break;
			}
		}
	}
	
	public void read(DataInputStream in) throws IOException{
		opcode = Opcode.values()[in.readByte()];
		switch(opcode){
		case NOP:
		case POP:
			break;
		case JUMP:
		case BNEQ:
			param = new Object[]{in.readInt()}; break;
		case ASSIGN:
			param = new Object[]{in.readUTF()}; break;
		case U_OP:
		case B_OP:
		case COMPARE:
			param = new Object[]{OpParam.values()[in.readByte()]}; break;
		case FUNC:
			param = new Object[]{in.readInt(), in.readByte()}; break;
		case PUSH:
			StackDatatype type = StackDatatype.values()[in.readByte()];
			switch(type){
			case INT:
				param = new Object[]{type, in.readInt()}; break;
			case FLOAT:
				param = new Object[]{type, in.readFloat()}; break;
			case STRING:
			case VAR:
				param = new Object[]{type, in.readUTF()}; break;
			}
			 break;
		}
	}
	*/
	
	
	@Override
	public String toString(){
		String str = (opcode == null ? "Null" : opcode.toString())+": ";
		if(param == null){
			str += "Null";
		}else{
			for(int i=0; i<param.length; i++){
				str += (i == 0 ? "" : ",")+(param[i] == null ? "Null" : 
					((param[i] instanceof String) ? "\""+param[i].toString()+"\"" : param[i].toString()) );
			}
		}
		return str;
	}
}
