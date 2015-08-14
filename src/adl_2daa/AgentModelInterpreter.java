package adl_2daa;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public abstract class AgentModelInterpreter {
	
	protected AgentModel agent;
	protected int currentState,usedInterpreter;
	protected Set<Integer> pendingNextState;
	protected HashMap<Integer,Integer> varTable;
	protected SequenceInterpreter[] stateItp;
	protected boolean errorOccurs;
	
	public AgentModelInterpreter(AgentModel theAgent){
		this.agent = theAgent;
		this.pendingNextState = new HashSet<Integer>();
		this.varTable = new HashMap<Integer, Integer>();
		this.stateItp = new SequenceInterpreter[this.agent.getRequiredInterpreterNumber()];
		this.errorOccurs = false;
	}
	
	public HashMap<Integer,Integer> getVarTable(){
		return varTable;
	}
	
	public void onSpawn(){
		if(errorOccurs) return;
		if(agent.getInitBlock() != null){
			stateItp[0] = new SequenceInterpreter(createInterpreterExtraData());
			stateItp[0].reload(agent.getInitBlock().getBehaviorCode());
			try {
				stateItp[0].interpret();
				changeState(0);
			} catch (Exception e) {
				System.out.println(agent.getIdentifier()+"#init\n"+e.getMessage());
				e.printStackTrace();
				errorOccurs = true;
			}
		}
	}
	
	public void pendingNextState(String identifier){
		int stateIndex = agent.getStateIndex(identifier);
		if(stateIndex == -1){
			System.out.println(agent.getIdentifier()+"#\nUnknown state "+identifier);
			errorOccurs = true;
			return;
		}
		pendingNextState.add(stateIndex);
	}
	
	public void update(float delta){
		if(errorOccurs) return;
		try {
			for(SequenceInterpreter itp : stateItp){
				if(itp != null){
					itp.interpret();
				}
			}
		} catch (Exception e) {
			System.out.println(agent.getIdentifier()+"#"+currentState+"\n"+e.getMessage());
			e.printStackTrace();
			errorOccurs = true;
		}
	}
	
	public void postUpdate(){
		if(errorOccurs) return;
		if(pendingNextState.size() > 0){
			int nextState = (Integer)pendingNextState.toArray()[
			    (int)(Math.random()*pendingNextState.size())
			];
			if(nextState != currentState){
				changeState(nextState);
			}
		}
	}
	
	public void onDespawn(){
		if(errorOccurs) return;
		if(agent.getDesBlock() != null){
			if(stateItp[0] == null)
				stateItp[0] = new SequenceInterpreter(createInterpreterExtraData());
			stateItp[0].reload(agent.getDesBlock().getBehaviorCode());
			try {
				stateItp[0].interpret();
			} catch (Exception e) {
				System.out.println(agent.getIdentifier()+"#des\n"+e.getMessage());
				e.printStackTrace();
				errorOccurs = true;
			}
		}
	}
	
	private void changeState(int nextState){
		currentState = nextState;
		BehaviorSequence[] sequences = agent.getState(currentState);
		usedInterpreter = sequences.length;
		for(int i=0; i<usedInterpreter; i++){
			if(stateItp[i] == null){
				stateItp[i] = new SequenceInterpreter(createInterpreterExtraData());
			}
			stateItp[i].reload(sequences[i].getBehaviorCode());
		}
		pendingNextState.clear();
	}
	
	protected abstract InterpreterExtraData createInterpreterExtraData();
}
