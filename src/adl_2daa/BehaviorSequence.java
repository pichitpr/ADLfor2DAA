package adl_2daa;

import java.util.ArrayList;
import java.util.List;

import adl_2daa.internal.Instruction;
import adl_2daa.internal.Opcode;

public class BehaviorSequence {

	public static final List<Instruction> EMPTY;
	static{
		EMPTY = new ArrayList<Instruction>();
		EMPTY.add(new Instruction(Opcode.NOP,null));
	}
	
	private String identifier;
	private List<Instruction> behaviorCode;
	
	public BehaviorSequence(String identifier, List<Instruction> behaviorCode){
		this.identifier = identifier;
		this.behaviorCode = behaviorCode;
	}
	
	public String getIdentifier(){
		return identifier;
	}
	
	public List<Instruction> getBehaviorCode(){
		return behaviorCode;
	}
}

