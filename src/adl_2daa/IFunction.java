package adl_2daa;




/**
 * User-defined function must implement this interface and register to Interpreter
 */
public interface IFunction {
	
	public Object invoke(SequenceInterpreter itp, Object... parameter);
}
