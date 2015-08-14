package adl_2daa;

import java.util.HashMap;

public class AgentModel {

	private String identifier;
	private BehaviorSequence init,des;
	private BehaviorSequence[][] states;
	private HashMap<String,Integer> stateIndexMap;
	
	public AgentModel(String identifier){
		this.identifier = identifier;
		init = new BehaviorSequence("init", BehaviorSequence.EMPTY);
		des = new BehaviorSequence("des", BehaviorSequence.EMPTY);
		states = new BehaviorSequence[1][1];
		states[0][0] = new BehaviorSequence("seq0", BehaviorSequence.EMPTY);
	}
	
	public String getIdentifier(){
		return identifier;
	}
	
	public BehaviorSequence getInitBlock(){
		return init;
	}
	
	public BehaviorSequence getDesBlock(){
		return des;
	}
	
	public BehaviorSequence[] getState(String identifier){
		if(stateIndexMap.containsKey(identifier)){
			return states[stateIndexMap.get(identifier)];
		}
		return null;
	}
	
	public BehaviorSequence[] getState(int stateIndex){
		return states[stateIndex];
	}
	
	public int getStateIndex(String identifier){
		if(!stateIndexMap.containsKey(identifier))
			return -1;
		return stateIndexMap.get(identifier);
	}
	
	public void setInit(BehaviorSequence state){
		this.init = state;
	}
	
	public void setDes(BehaviorSequence state){
		this.des = state;
	}
	
	public void setStates(BehaviorSequence[][] states){
		this.states = states;
	}
	
	public void setStateIndexMap(HashMap<String,Integer> map){
		this.stateIndexMap = map;
	}
	
	public int getRequiredInterpreterNumber(){
		int num = 1;
		for(int i=0; i<states.length; i++){
			if(this.states[i].length > num)
				num = this.states[i].length;
		}
		return num;
	}
}
