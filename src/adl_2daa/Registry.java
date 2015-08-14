package adl_2daa;

import java.util.HashMap;

public class Registry {

	private static HashMap<String,IAction> actionMap = new HashMap<String,IAction>();
	public static void registerAction(String funcname, IAction theAction){
		actionMap.put(funcname, theAction);
	}
	
	public static IAction getAction(String funcCode){
		return actionMap.get(funcCode);
	}
	
	private static HashMap<String,IFunction> functionMap = new HashMap<String,IFunction>();
	public static void registerFunction(String funcname, IFunction theFunction){
		functionMap.put(funcname, theFunction);
	}
	
	public static IFunction getFunction(String funcCode){
		return functionMap.get(funcCode);
	}
	
}
