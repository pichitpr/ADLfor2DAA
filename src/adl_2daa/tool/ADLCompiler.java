package adl_2daa.tool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import adl_2daa.AgentModel;
import adl_2daa.BehaviorSequence;
import adl_2daa.ast.ASTStatement;
import adl_2daa.ast.structure.Agent;
import adl_2daa.ast.structure.Sequence;
import adl_2daa.ast.structure.State;
import adl_2daa.internal.Instruction;
import adl_2daa.internal.Opcode;

public class ADLCompiler {
	
	/*
	public static void compile(String filepath,String destpath) throws Exception{
		File f = new File(filepath);
		String code = "",tmp;
		Reader r = new InputStreamReader(new FileInputStream(f), "UTF-8");
		BufferedReader in = new BufferedReader(r);
		while((tmp = in.readLine()) != null){
			code += tmp+"\n";
		}
		in.close();
		
		Tokenizer tokenizer = new Tokenizer(code);
		Parser parser = new Parser(tokenizer);
		List<Statement> stList = new ArrayList<Statement>();
		Statement st;
		while((st = parser.nextStatement()) != null){
			stList.add(st);
		}
		
		Compiler compiler = new Compiler(stList);
		compiler.compile();
		
		f = new File(destpath);
		DataOutputStream out = new DataOutputStream(new FileOutputStream(f));
		for(Instruction ins : compiler.generatedIns){
			ins.save(out);
		}
		out.close();
	}*/
	
	public static AgentModel compile(Agent astAgent){
		AgentModel model = new AgentModel(astAgent.getIdentifier());
		ADLCompiler compiler;
		
		if(astAgent.getInit() != null){
			System.out.println("==== Init");
			compiler = new ADLCompiler(astAgent.getInit().getStatements());
			compiler.compile();
			//compiler.debug();
			model.setInit(new BehaviorSequence("init", compiler.generatedIns));
		}
		if(astAgent.getDes() != null){
			System.out.println("==== Des");
			compiler = new ADLCompiler(astAgent.getDes().getStatements());
			compiler.compile();
			//compiler.debug();
			model.setDes(new BehaviorSequence("des", compiler.generatedIns));
		}
		
		BehaviorSequence[][] states = new BehaviorSequence[astAgent.getStates().size()][];
		HashMap<String,Integer> stateIndexMap = new HashMap<String,Integer>();
		int i=0,j;
		for(State state : astAgent.getStates()){
			System.out.println("==== State "+state.getIdentifier());
			stateIndexMap.put(state.getIdentifier(), i);
			states[i] = new BehaviorSequence[state.getSequences().size()];
			j=0;
			for(Sequence sequence : state.getSequences()){
				System.out.println("==== Seq "+sequence.getIdentifier());
				compiler = new ADLCompiler(sequence.getStatements());
				compiler.compile();
				//compiler.debug();
				states[i][j] = new BehaviorSequence(sequence.getIdentifier(), compiler.generatedIns);
				j++;
			}
			i++;
		}
		model.setStates(states);
		model.setStateIndexMap(stateIndexMap);
		
		return model;
	}
	
	private List<ASTStatement> parsed;
	private List<Instruction> generatedIns;
	private int labelIndex = 0;
	private int memIndex = 0;
	
	public ADLCompiler(List<ASTStatement> statements){
		this.parsed = statements;
		generatedIns = new ArrayList<Instruction>();
	}
	
	public void compile(){
		//Compile
		for(ASTStatement st : parsed){
			st.compile(generatedIns,this);
		}
		//Remove internal opcode
		int i=0;
		Instruction ins;
		HashMap<Integer,Integer> map = new HashMap<Integer,Integer>();
		//First pass -- create labelName <> index mapping
		generatedIns.add(new Instruction(Opcode.NOP,null)); //Prevent error from ILABEL being the last Opcode
		while(i < generatedIns.size()){
			ins = generatedIns.get(i);
			if(ins.opcode == Opcode.ILABEL){
				generatedIns.remove(i);
				map.put((Integer)ins.param[0], i);
				i--;
			}
			i++;
		}
		
		//Second pass -- remove all Ixxxx Opcode and remap all jumping address
		i=0;
		while(i < generatedIns.size()){
			ins = generatedIns.get(i);
			if(ins.opcode == Opcode.IBNEQ){
				ins.opcode = Opcode.BNEQ;
				ins.param = new Object[]{map.get((Integer)ins.param[0])};
			}else if(ins.opcode == Opcode.IJUMP){
				ins.opcode = Opcode.JUMP;
				ins.param = new Object[]{map.get((Integer)ins.param[0])};
			}
			i++;
		}
	}
	
	public int getCurrentLabel(){
		int i = labelIndex;
		labelIndex++;
		return i;
	}
	
	public void resetLabel(){
		labelIndex = 0;
	}
	
	public int getCurrentMemIndex(){
		int i = memIndex;
		memIndex++;
		return i;
	}
	
	public void resetMemIndex(){
		memIndex = 0;
	}
	
	public List<Instruction> getCompiledCode(){
		return generatedIns;
	}
	
	public void debug(){
		for(Instruction i : generatedIns){
			if(i == null) System.out.println("Null");
			else System.out.println(i);
		}
	}
}
