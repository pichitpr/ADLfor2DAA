package adl_2daa;




public interface IAction {
	/**
	 * @return is the action end?
	 */
	public boolean invoke(SequenceInterpreter itp, Object... parameter);
	
	public boolean isSpanAction();
}
