package adl_2daa;

import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import adl_2daa.internal.Instruction;
import adl_2daa.internal.OpParam;
import adl_2daa.internal.Opcode;

public class SequenceInterpreter {

	public boolean verbose = false;
	public boolean verboseDetail = false;
	
	private List<Instruction> sequenceCode;
	private int address;
	private Stack<Object> datastack;
	private HashMap<Integer,Object> register;
	
	private HashMap<String,Object> spannedActionInfo;
	private InterpreterExtraData additionalData;
	private String lastErrorMessage;
	
	public SequenceInterpreter(InterpreterExtraData additionalData){
		this.address = 0;
		this.datastack = new Stack<Object>();
		this.register = new HashMap<Integer,Object>();
		this.spannedActionInfo = new HashMap<String,Object>();
		this.additionalData = additionalData;
		this.lastErrorMessage = null;
	}
	
	public HashMap<String,Object> getSpannedActionInfo(){
		return spannedActionInfo;
	}
	
	public void reset(){
		address = 0;
		datastack.clear();
		register.clear();
		spannedActionInfo.clear();
		additionalData.onInterpreterReset();
	}
	
	public void reload(List<Instruction> sequenceCode){
		reset();
		this.sequenceCode = sequenceCode;
	}
	
	public void interpret() throws Exception{
		Instruction ins;
		while(true){
			lastErrorMessage = null;
			ins = sequenceCode.get(address);
			interpretInstruction(ins);
			if(lastErrorMessage != null){
				throw new Exception(lastErrorMessage);
			}
			if(ins.opcode == Opcode.BREAK) break;
			if(address >= sequenceCode.size()){
				additionalData.onLastInstructionInterpreted();
				address = 0;
				break;
			}
		}
		additionalData.onInterpreterPause();
	}
	
	public InterpreterExtraData getAdditionalData(){
		return additionalData;
	}
	
	private void reportError(String err){
		lastErrorMessage = err;
	}
	
	private void interpretInstruction(Instruction ins){
		Opcode opcode = ins.opcode;
		Object[] param = ins.param;
		
		if(verboseDetail){
			//System.out.println(address+"@ "+ins);
			System.out.println(opcode);
		}
		
		int memIndex;
		switch(opcode){
		case NOP:
			address++; 
			break;
		case POP:
			datastack.pop();
			address++; 
			break;
		case ASSIGN:
			Object storingData = datastack.pop();
			memIndex = (Integer)param[0];
			register.put(memIndex, storingData);
			address++;
			break;
		case LOAD:
			memIndex = (Integer)param[0];
			datastack.push(register.get(memIndex));
			address++;
			break;
		case JUMP:
			address = (Integer)param[0]; 
			break;
		case BNEQ:
			boolean b = (Boolean)datastack.pop();
			if(b == false){
				address = (Integer)param[0]; 
			}else{
				address++;
			}
			break;
		case U_OP:
			interpretUnaryOp(param); 
			break;
		case B_OP:
			interpretBinaryOp(param);
			break;
		case COMPARE:
			interpretComparison(param);
			break;
		case ACTION:
			interpretAction(param);
			break;
		case FUNC:
			interpretFunction(param);
			break;
		case PUSH:
			datastack.push(param[0]);
			address++;
			break;
		case BREAK:
			address++;
			break;
		case STARTACTION:
		case ENDACTION:
			spannedActionInfo.clear();
			additionalData.onSpannedActionEnd();
			address++;
			break;
		default:
			reportError("Unknown opcode "+opcode);
		}
	}
	
	private void interpretUnaryOp(Object[] param){
		Object o = datastack.pop();
		
		if(o instanceof Boolean){
			boolean b = (Boolean)o;
			datastack.push(!b);
			address++;
		}else if(o instanceof Integer){
			int i = (Integer)o;
			if((OpParam)param[0] == OpParam.SUB){
				datastack.push(-i);
			}else{
				datastack.push(i == 0);
			}
			address++;
		}else if(o instanceof Float){
			float f = (Float)o;
			if((OpParam)param[0] == OpParam.SUB){
				datastack.push(-f);
			}else{
				datastack.push(f == 0);
			}
			address++;
		}else{
			reportError("Incompatible data "+o+" for unary operation "+param[0]);
		}
	}
	
	private void interpretBinaryOp(Object[] param){
		Object leftSide = datastack.pop();
		Object rightSide = datastack.pop();
		OpParam op = (OpParam)param[0];
		
		switch(op){
		case AND: case OR:
			boolean bl = (Boolean)leftSide;
			boolean br = (Boolean)rightSide;
			datastack.push(op == OpParam.AND ? (bl && br) : (bl || br) );
			address++;
			break;
		case ADD: case SUB: case MUL: case DIV: case MOD:
			interpretMathOp(leftSide, op, rightSide);
			break;
		case B_AND: case B_OR: case B_XOR: case B_LSHIFT: case B_RSHIFT:
			interpretBitwiseOp(
					((Number)leftSide).intValue(), op, ((Number)rightSide).intValue()
					);
			break;
		default:
			reportError("Unknown binary operation "+op);
		}
	}
	
	private void interpretMathOp(Object leftSide, OpParam op, Object rightSide){
		if(leftSide instanceof Float || rightSide instanceof Float){
			float left = ((Number)leftSide).floatValue();
			float right = ((Number)rightSide).floatValue();
			switch(op){
			case ADD: left += right; break;
			case SUB: left -= right; break; 
			case MUL: left *= right; break;
			case DIV: left /= right; break;
			case MOD: left %= right; break;
			default:
			}
			datastack.push(left);
		}else{
			int left = (Integer)leftSide;
			int right = (Integer)rightSide;
			switch(op){
			case ADD: left += right; break;
			case SUB: left -= right; break; 
			case MUL: left *= right; break;
			case DIV: left /= right; break;
			case MOD: left %= right; break;
			default:
			}
			datastack.push(left);
		}
		address++;
	}
	
	private void interpretBitwiseOp(int left, OpParam op, int right){
		switch(op){
		case B_AND: left &= right; break;
		case B_OR: left |= right; break;
		case B_XOR: left ^= right; break;
		case B_LSHIFT: left <<= right; break;
		case B_RSHIFT: left >>= right; break;
		default:
		}
		datastack.push(left);
		address++;
	}
	
	private void interpretComparison(Object[] param){
		Object leftSide = datastack.pop();
		Object rightSide = datastack.pop();
		OpParam op = (OpParam)param[0];
		
		if(leftSide instanceof Number){
			float left = ((Number)leftSide).floatValue();
			float right = ((Number)rightSide).floatValue();
			boolean val  = false;
			switch(op){
			case EQ: val = left == right; break;
			case NEQ: val = left != right; break;
			case GT: val = left > right; break;
			case GE: val = left >= right; break;
			case LT: val = left < right; break;
			case LE: val = left <= right; break;
			default:
			}
			datastack.push(val);
		}else{
			if(op == OpParam.NEQ)
				datastack.push(!leftSide.equals(rightSide));
			else
				datastack.push(leftSide.equals(rightSide));
		}
		address++;
	}
	
	private void interpretAction(Object[] param){
		if(verbose)
			System.out.println(param[0].toString());
		IAction action = Registry.getAction(param[0].toString());
		if(action == null){
			reportError("Call unregistered function "+param[0].toString());
			return;
		}
		int paramsNum = (Integer)param[1];
		Object[] obj = new Object[paramsNum];
		for(int i=0; i<paramsNum; i++){
			obj[i] = datastack.pop();
		}
		boolean isActionEnd = action.invoke(this, obj);
		datastack.push(isActionEnd);
		address++;
	}
	
	private void interpretFunction(Object[] param){
		if(verbose)
			System.out.println(param[0].toString());
		IFunction function = Registry.getFunction(param[0].toString());
		if(function == null){
			reportError("Call unregistered function "+param[0].toString());
			datastack.push(0);
			return;
		}
		int paramsNum = (Integer)param[1];
		Object[] obj = new Object[paramsNum];
		for(int i=0; i<paramsNum; i++){
			obj[i] = datastack.pop();
		}
		
		Object returnValue = null;
		boolean singleQuery = (Boolean)param[2];
		if(singleQuery && spannedActionInfo.containsKey(""+address)){
			returnValue = spannedActionInfo.get(""+address);
		}else{
			returnValue = function.invoke(this , obj);
			spannedActionInfo.put(""+address, returnValue);
		}
		
		if(returnValue == null){
			datastack.push(0);
		}else{
			datastack.push(returnValue);
		}
		address++;
	}
}
